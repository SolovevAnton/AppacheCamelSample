# Getting Started

### Guides

The following guides illustrate how to use some features concretely:

* [Using Apache Camel with Spring Boot](https://camel.apache.org/camel-spring-boot/latest/spring-boot.html)
* [Camel with Spring rabbitMQ guide](https://camel.apache.org/components/4.8.x/spring-rabbitmq-component.html)
* [Camel with Rest](https://camel.apache.org/components/4.8.x/rest-component.html)
* [Camel rest DSL](https://camel.apache.org/manual/rest-dsl.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
