package com.solovev.appachecamel.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class RESTRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
 /*       restConfiguration()
                .component("servlet")
                .inlineRoutes(true)
                .host("localhost")
                .port(8080)
                .bindingMode(RestBindingMode.json); // Adjust binding mode if needed;*/

        rest("/cole")
                .get("/helloWorld")
                .outType(String.class)
                .produces("text/plain")
                .param()
                .name("name")
                .type(RestParamType.query)
                .endParam()
                .to("direct:helloWorld");

        from("direct:helloWorld")
                .setBody(simple("Hello world, ${header.name}!"));
    }
}
