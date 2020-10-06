package com.qa.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc // - enables Swagger UI at <host>:<port>/swagger-ui/index.html
public class todolistApplication {

    public static void main(String[] args) {
        SpringApplication.run(todolistApplication.class, args);
    }

}


