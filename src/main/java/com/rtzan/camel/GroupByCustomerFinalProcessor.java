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

import javax.enterprise.context.ApplicationScoped;

import com.rtzan.model.Cart;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class GroupByCustomerFinalProcessor implements Processor {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields 
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Cart> processedHistory = new ArrayList<>();

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods 
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("FINAL [{}]", exchange.getIn().getBody());
        processedHistory.add(exchange.getIn().getBody(Cart.class));
    }

    public List<Cart> getHistory() {
        return processedHistory;
    }
}
