package com.solovev.appachecamel.config;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class RESTRoute extends RouteBuilder {

    public static final String HELLO_ROUTE_ID = "hello";

    public static final String RMQ_ROUTE_ID = "rabbitmq";

    @Override
    public void configure() throws Exception {

        rest("/service")
                .get("/helloWorld")
                .outType(String.class)
                .produces("text/plain")
                .to(getDirectRoute(HELLO_ROUTE_ID))
                .post()
                .consumes("application/json")
                .produces("application/json")
                .to(getDirectRoute(RMQ_ROUTE_ID));

        configureGetRoute();
        configurePostRoute();
    }

    private void configureGetRoute() {
        from(getDirectRoute(HELLO_ROUTE_ID))
                .routeId(HELLO_ROUTE_ID)
                .log("Responded to ping ${headers}")
                .setBody(simple("Hello world!"));
    }

    private void configurePostRoute() {
        from(getDirectRoute(RMQ_ROUTE_ID))
                .routeId(RMQ_ROUTE_ID)
                .setBody(e-> e.getIn().getBody(String.class))
                .to(ExchangePattern.InOnly,"spring-rabbitmq:{{source.rmq.exchange.name}}?routingKey={{target.rmq.camel-reply}}")
                .log("Send to queue ${body}");

    }

    private String getDirectRoute(String routeName) {
        return "direct:" + routeName;
    }
}
