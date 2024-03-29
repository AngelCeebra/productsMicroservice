package com.microservice.app.users;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.microservice.app.commons.users.models.entity.Role;
import com.microservice.app.commons.users.models.entity.User;

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

		RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
		config.exposeIdsFor(User.class, Role.class);
	}

}
