/** Free */
package com.rtzan.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientAggregationCompletionPredicate implements Predicate {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean matches(Exchange exchange) {
        Integer processedCustomerCnt = exchange.getIn().getHeader("PROCESSED_CUSTOMER_COUNTER", Integer.class);
        Integer totalCustomerCnt = exchange.getIn().getHeader("TOTAL_CUSTOMER_COUNT", Integer.class);

        logger.info("### Processed [{}] / [{}] customers", processedCustomerCnt, totalCustomerCnt);

        return totalCustomerCnt.equals(processedCustomerCnt);
    }
}
