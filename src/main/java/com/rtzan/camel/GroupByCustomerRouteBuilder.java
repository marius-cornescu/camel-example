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

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import javax.inject.Inject;

import static org.apache.camel.builder.xml.XPathBuilder.xpath;

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
            .aggregate(body())
                .aggregationStrategy(new ClientAggregationStrategy())
                .completionTimeout(AGGREGATION_TIME_OUT_MS)
            .end()
        .log(LoggingLevel.INFO, "aggregation done")
        .log(LoggingLevel.INFO, "sourcing done")
        .process(groupByCustomerFinalProcessor)
        ;
        
        //J++
    }
}
