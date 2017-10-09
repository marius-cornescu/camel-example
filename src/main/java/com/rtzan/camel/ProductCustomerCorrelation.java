/** Free */
package com.rtzan.camel;

import com.rtzan.model.Product;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;


public class ProductCustomerCorrelation implements Expression {

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        return (T) exchange.getIn().getBody(Product.class).getCustomer().getName();
    }
}
