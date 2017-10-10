/** Free */
package com.rtzan.camel;

import java.util.ArrayList;
import java.util.List;

import com.rtzan.model.Cart;
import com.rtzan.model.Product;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientAggregationStrategy implements AggregationStrategy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Product product = newExchange.getIn().getBody(Product.class);
        Cart cart;
        if (oldExchange == null) {
            cart = new Cart(product.getCustomer());
            cart.addItem(product, 1);
            newExchange.getIn().setBody(cart);
            logger.debug("Customer [{}] has [{}] products", product.getCustomer(), cart.getCartItems().size());
            return newExchange;
        } else {
            cart = oldExchange.getIn().getBody(Cart.class);
            cart.addItem(product, 1);
            logger.debug("Customer [{}] has [{}] products", product.getCustomer(), cart.getCartItems().size());
            return oldExchange;
        }
    }

}
