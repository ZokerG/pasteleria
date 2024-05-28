package com.example.pasteleria.service;

import com.example.pasteleria.model.Producto;
import com.example.pasteleria.repository.ProductoRepositorio;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServicio {

    private final ProductoRepositorio productoRepositorio;

    @Value("${stripe.secretKey}")
    private String secretKey;

    public StripeServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }


    public Session crearPago(double price, int cantidad) throws StripeException {
        Stripe.apiKey = this.secretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://example.com/success")
                .setCancelUrl("https://example.com/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity((long) cantidad)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) price * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Tasty Cake")
                                                                .build())
                                                .build())
                                .build())
                .build();

        return Session.create(params);
    }
}
