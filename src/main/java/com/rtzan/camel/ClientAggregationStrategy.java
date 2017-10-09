/**
 *  Copyright Murex S.A.S., 2003-2017. All Rights Reserved.
 * 
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
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

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Product body = newExchange.getIn().getBody(Product.class);
        Cart bodies;
        if (oldExchange == null) {
            bodies = new Cart(body.getCustomer());
            bodies.addItem(body, 1);
            newExchange.getIn().setBody(bodies);
            return newExchange;
        } else {
            bodies = oldExchange.getIn().getBody(Cart.class);
            bodies.addItem(body, 1);
            logger.debug("Customer [{}] has [{}] products", body.getCustomer(), bodies.getCartItems().size());
            return oldExchange;
        }
    }

}
