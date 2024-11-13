package com.solovev.appachecamel.config;

import com.solovev.appachecamel.model.Person;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .to(createDirectRoute(HELLO_ROUTE_ID))
                .post()
                .consumes("application/json")
                .bindingMode(RestBindingMode.json)
                .type(Person.class)
                .responseMessage(201,"Sent to RMQ")
                .to(createDirectRoute(RMQ_ROUTE_ID));

        configureGetRoute();
        configurePostRoute();
    }

    private void configureGetRoute() {
        from(createDirectRoute(HELLO_ROUTE_ID))
                .routeId(HELLO_ROUTE_ID)
                .log("Responded to ping ${headers}")
                .setBody(simple("Hello world!"));
    }

    private void configurePostRoute() {
        from(createDirectRoute(RMQ_ROUTE_ID))
                .routeId(RMQ_ROUTE_ID)
                .outputType(Person.class)
                .process(this::modifyPerson)
                .marshal().json(JsonLibrary.Jackson)
                .to(ExchangePattern.InOnly,"spring-rabbitmq:{{source.rmq.exchange.name}}?routingKey={{target.rmq.camel-reply}}")
                .log("Send to queue ${body}")
                .end();

    }

    private void modifyPerson(Exchange exchange){
        Person person = exchange.getIn().getBody(Person.class);
        person.setTimeStamp(LocalDateTime.now().toString());
        exchange.getIn().setBody(person);
    }

    private String createDirectRoute(String routeName) {
        return "direct:" + routeName;
    }
}
