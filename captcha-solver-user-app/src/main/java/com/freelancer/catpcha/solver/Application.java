package com.freelancer.catpcha.solver;

import com.freelancer.catpcha.solver.ui.Dashboard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false).run(args);
		Dashboard dashboard = context.getBean(Dashboard.class);
		dashboard.setVisible(true);
	}

}
