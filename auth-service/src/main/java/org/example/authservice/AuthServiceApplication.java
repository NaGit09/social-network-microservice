package org.example.authservice;

// Spring is a comprehensive ( toàn diện ) framework for building enterprise-level Java applications.
// It provides features like dependency injection, aspect-oriented programming, and a robust ecosystem for web, 
// data access, and more.
// Spring Boot builds on top of Spring to simplify the development of production-ready Spring applications
// with minimal configuration.
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*

 */
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        // Spring Boot works by automatically configuring your application based on the dependencies
        //  you have added to your project.
        // It then runs the application by starting an embedded web server (like Tomcat) 
        // and deploying your application to it.
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
