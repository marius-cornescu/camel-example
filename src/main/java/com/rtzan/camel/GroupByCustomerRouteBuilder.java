/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rtzan.camel;

import com.rtzan.model.Receipt;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.inject.Inject;

/**
 * A Camel Router
 *
 * @version $
 */
public class GroupByCustomerRouteBuilder extends RouteBuilder {
    
    private static final long AGGREGATION_TIME_OUT_MS = 10 * 1000;
    
    private static final int AGGREGATION_CONCURRENT = 10;
    
    @Inject
    private GroupByCustomerFinalProcessor groupByCustomerFinalProcessor;
    
    /**
     * Lets configure the Camel routing rules using Java code...
     */
    public void configure() {

        //J--
        from("direct:input")
        .to("seda:groupByCustomer");
        
        from("seda:groupByCustomer?concurrentConsumers=" + AGGREGATION_CONCURRENT)
            .aggregate(new ProductCustomerCorrelation()).parallelProcessing()
                    .aggregationStrategy(new ClientAggregationStrategy())
                    .completionTimeout(AGGREGATION_TIME_OUT_MS)
                .log(LoggingLevel.INFO, "aggregation done for ${body.customer}")
                .to("seda:groupByCustomer-SOURCING")
            .end()
        .stop()
        ;
        
        from("seda:groupByCustomer-SOURCING?concurrentConsumers=" + AGGREGATION_CONCURRENT)
        // do sourcing
        .process(new CustomerSelectionProcessor())
        .choice()
        .when(body().isInstanceOf(Receipt.class))
            .log(LoggingLevel.INFO, "sourcing done for ${body.customer}")
            .to("direct:groupByCustomer-FINAL")
            .endChoice()
        .otherwise()
            .log(LoggingLevel.INFO, "sourcing NOT done for ${body.customer}")
            .stop()
        .endChoice()
        ;
        
        from("direct:groupByCustomer-FINAL")
        .process(groupByCustomerFinalProcessor)
        .stop()
        ;
        
        //J++
    }
}
