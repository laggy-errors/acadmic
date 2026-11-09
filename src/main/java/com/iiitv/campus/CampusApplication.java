package com.iiitv.campus;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "campus")
public class CampusApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(CampusApplication.class, args);
    }
}
