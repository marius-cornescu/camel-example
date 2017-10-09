/** Free */
package com.rtzan.camel;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientAggregationStrategy implements AggregationStrategy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Object body = newExchange.getIn().getBody();
        List<Object> bodies;
        if (oldExchange == null) {
            bodies = new ArrayList<>();
            bodies.add(body);
            newExchange.getIn().setBody(bodies);
            return newExchange;
        } else {
            bodies = oldExchange.getIn().getBody(List.class);
            bodies.add(body);
            logger.debug("Body size is: [{}]", bodies.size());
            return oldExchange;
        }
    }

}
