/** Free */
package com.rtzan.camel;

import javax.annotation.PreDestroy;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.interceptor.Tracer;


@ApplicationScoped
public class TestSetup {

    @Inject
    @Uri("direct:input")
    private ProducerTemplate inputEndpoint;

    @Inject
    private CamelContext context;

    @PreDestroy
    public void shutDown() {
        try {
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
    }

    public void setupContext() throws Exception {
        setupContext(null, true);
    }

    public CamelContext getContext() {
        return context;
    }

    public ProducerTemplate getInputEndpoint() {
        return inputEndpoint;
    }

    void setupContext(RouteBuilder routeBuilder) throws Exception {
        setupContext(routeBuilder, false);
    }

    private static void resetEndpoints(MockEndpoint... mockEndpoints) {
        for (MockEndpoint mockEndpoint : mockEndpoints) {
            mockEndpoint.reset();
        }
    }

    private void setupContext(RouteBuilder routeBuilder, boolean mockClient) throws Exception {

        enableCamelTracer(true); // ENABLE THIS FOR CAMEL TRACE

        if (context.getStatus().isStarted()) {
            return;
        }

        if (routeBuilder != null) {
            context.addRoutes(routeBuilder);
        }

        try {
            mock(mockClient);
            context.start();
        } catch (Exception cause) {
            throw new RuntimeException(cause);
        }
    }

    private void mock(final boolean mockClient) throws Exception {

    }

    private void enableCamelTracer(boolean enable) {
        context.setTracing(enable);

        Tracer tracer = new Tracer();
        tracer.setLogName("CamelTracer.log");
        tracer.getDefaultTraceFormatter().setShowProperties(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(true);
        tracer.getDefaultTraceFormatter().setShowBody(true);
        tracer.getDefaultTraceFormatter().setShowNode(true);
        tracer.getDefaultTraceFormatter().setShowBreadCrumb(true);
    }
}
