/** Free */
package com.rtzan.camel;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


public class MessageCountProcessor implements Processor {

    private final String headerName;
    private final MessageCounter messageCounter;

    public MessageCountProcessor(MessageCounter messageCounter, String headerName) {
        this.headerName = headerName;
        this.messageCounter = messageCounter;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Integer processedCustomerCnt = messageCounter.increment(headerName);
        exchange.getIn().setHeader(headerName, processedCustomerCnt);
    }
}
