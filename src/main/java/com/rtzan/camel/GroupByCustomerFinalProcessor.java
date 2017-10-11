/** Free */
package com.rtzan.camel;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.rtzan.model.Cart;
import com.rtzan.model.Receipt;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class GroupByCustomerFinalProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Receipt> processedHistory = new ArrayList<>();

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("FINAL [{}]", exchange.getIn().getBody());
        processedHistory.add(exchange.getIn().getBody(Receipt.class));
    }

    public List<Receipt> getHistory() {
        return processedHistory;
    }
}
