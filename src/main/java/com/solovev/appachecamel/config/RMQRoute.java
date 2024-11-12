package com.solovev.appachecamel.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RMQRoute extends RouteBuilder {
    public static final String ROUTE_ID = "new_pack_handler";

    @Override
    public void configure() throws Exception {
        from("spring-rabbitmq:{{rmq.exchange.name}}" +
             "?queues={{rmq.queue.new-pack-queue}}" +
             "&autoDeclare=false")
                .routeId(ROUTE_ID)
                .log("Body: ${body}")
                .process(this::enhanceMessage)
                .to("http://{{target.url.new-pack}}?httpMethod=POST")
                .log("Forwarded message to HTTP POST at {{target.url.new-pack}}");
    }

    private void enhanceMessage(Exchange exchange) {
        String body = exchange.getIn().getBody(String.class);
        var message = exchange.getMessage();
        message.setBody(body);
        message.setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.setMessage(message);
        log.info("Message enhanced: Headers: [{}] Body: [{}]", message.getHeaders(),message.getBody());
    }

}
