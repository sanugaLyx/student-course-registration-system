/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: src/main/java/com/scrs/ScrsApplication.java
 * 🏗  LAYER: Backend — Entry Point
 * 📋 ROLE: The main execution class for the Spring Boot application.
 *          Bootstraps the embedded Tomcat server and loads the application context.
 * ─────────────────────────────────────────────────────────────
 */

package com.scrs; // [L1] Root package

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * [L2] @SpringBootApplication
 * This is a convenience annotation that adds all of the following:
 * - @Configuration: Tags the class as a source of bean definitions.
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services in the 'com.scrs' package.
 */
@SpringBootApplication // [L1] Bootstraps the application automatically
public class ScrsApplication {

    /*
     * [L2] main() method
     * Standard Java entry point. SpringApplication.run() starts the embedded web server (Tomcat by default)
     * and initializes the Spring Dependency Injection (IoC) container.
     */
    public static void main(String[] args) {
        SpringApplication.run(ScrsApplication.class, args); // [L1] Launches the Spring Boot application
    }
}
