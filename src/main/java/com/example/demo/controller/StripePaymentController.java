package com.example.demo.controller;

import com.example.demo.config.token.JwtUtil;
import com.example.demo.config.token.util.TokenAuthorization;
import com.example.demo.entity.Category;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StripePaymentController {
    @Value("${Stripe.apiKey}")
    String stripeKey;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    public StripePaymentController(
            UserRepository userRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository
    ) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(
            @RequestParam("product") Product product,
            @RequestParam("user") User user
    ) {
        Stripe.apiKey = "sk_test_51EJO9QJvBeCJjfk9vsTx3Q569vJAYt2QdKqAXhFrD5rg30BuWHm8LnZkb9almSyYVoDRlG1xqS6hhePqHEw1wwuD00AvrqvwoK";
        String YOUR_DOMAIN = "http://localhost:3001";

        long priceInCents = (long) (product.getPrice() * 100);

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setCustomerEmail(user.getEmail())
                        .setSubmitType(SessionCreateParams.SubmitType.PAY)
                        .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                        .setShippingAddressCollection(
                                SessionCreateParams.ShippingAddressCollection.builder()
                                        .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.FR)
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(YOUR_DOMAIN + "?success=true")
                        .setCancelUrl(YOUR_DOMAIN + "?canceled=true")
                        .setLocale(SessionCreateParams.Locale.FR)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(product.getCurrencyCode().toString())
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(product.getTitle())
                                                        .setDescription(String.valueOf(product.getId()))
                                                        .build())
                                                .setUnitAmount(priceInCents)
                                                .build())
                                        .setQuantity(1L)
                                        .build())
                        .build();
        try {
            Session session = Session.create(params);
            String checkoutUrl = session.getUrl();
            return ResponseEntity.ok(checkoutUrl);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        Stripe.apiKey = "sk_test_51EJO9QJvBeCJjfk9vsTx3Q569vJAYt2QdKqAXhFrD5rg30BuWHm8LnZkb9almSyYVoDRlG1xqS6hhePqHEw1wwuD00AvrqvwoK";

        try {
            Event event = Webhook.constructEvent(payload, signature, "whsec_4c373e37e03b6aae3d04612b7fb22e0882d99b2cc4060e9eba8939a7df92de5c");
            String username = "";

            // Handle the checkout.session.completed event
            if ("checkout.session.completed".equals(event.getType())) {
                Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().get();
                handleCheckoutSessionCompletedEvent();
//                username = sessionEvent.getCustomerEmail();
//                System.out.println("Emailing " + username);
            }


            return new ResponseEntity<>("Youpi", HttpStatus.OK);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    private void handleCheckoutSessionCompletedEvent() {
        Stripe.apiKey = "sk_test_51EJO9QJvBeCJjfk9vsTx3Q569vJAYt2QdKqAXhFrD5rg30BuWHm8LnZkb9almSyYVoDRlG1xqS6hhePqHEw1wwuD00AvrqvwoK";

        Integer productId = 601;
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Not found product with id: " + productId));

//        Order order = new Order();
//        order.setStatus(Status.PAID);
//        order.setProduct(product);

        product.setStatus(Status.SOLD);
        productRepository.save(product);
//        orderRepository.save(order);
    }


}
