/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.fdroid;

import com.samskivert.mustache.Mustache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;

import de.k3b.fdroid.html.service.ResourceBundleMustacheContext;
import de.k3b.fdroid.html.util.MustacheEx;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * j2se-jpa-db implementation that reads from fdroid-v1-jar and updates a jpa database
 */

@EnableJpaRepositories(basePackages = {"de.k3b.fdroid"})
@ComponentScan(basePackages = {"de.k3b.fdroid"})
@EntityScan({"de.k3b.fdroid"})
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "F-Droid-Universal-Catalog",
				description = "" +
						"An 'App Catalog' to find, discover and download apps for your Android Smartphone or Android Tablet.",
				contact = @Contact(
						name = "k3b",
						url = "https://github.com/k3b/f-Droid-Universal-Catalog/wiki/"
						// email = "petros.stergioulas94@gmail.com"
				),
				license = @License(
						name = "GPL-3.0 license",
						url = "https://github.com/k3b/F-Droid-Universal-Catalog/blob/master/LICENSE")),
		servers = @Server(url = "http://localhost:8080")
)
@SuppressWarnings("unused")
public class WebServerApplication {
	private static final Logger log = LoggerFactory.getLogger(WebServerApplication.class);

	@Value("${de.k3b.fdroid.db.dir}")
	private String dbDir;

	@Value("${de.k3b.fdroid.downloads:~/.fdroid/downloads}")
	private String downloadPath;

	@Value("${spring.datasource.url}")
	private String jdbc;

	// example commandline parameters
	// F-Droid_Archive-index-v1.jar reload archive
	// -f "k3b" find apps by k3b
	public static void main(String[] args) {
		changeJdbcIfServerRunning();

		MustacheEx.addFixedProperty("t", new ResourceBundleMustacheContext(Locale.US));
		SpringApplication application = new SpringApplication(WebServerApplication.class);
		// ... customize application settings here
		application.run(args);
	}

	/// Override Spring MustacheAutoConfiguration to support fixed values
	@Bean
	public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader) {
		return MustacheEx
				.createMustacheCompiler()
				.withLoader(mustacheTemplateLoader)
				;
	}

	private static void changeJdbcIfServerRunning() {
		String serverConnect = "";
		Connection c;
		try {
			String dbName = getProperty("de.k3b.fdroid.db.name");
			serverConnect = "jdbc:hsqldb:hsql://localhost/" + dbName;
			c = DriverManager.getConnection(serverConnect);
			System.out.println(c.toString());
			c.close();
			System.out.println("Connecting to running server " + serverConnect);
			log.debug("Connecting to running server " + serverConnect);
			System.setProperty("spring.datasource.url", serverConnect);
		} catch (SQLException throwables) {
			System.out.println("No running server " + serverConnect + " found. Using local file instead");
			log.warn("No running server " + serverConnect + " found. Using local file instead");
		}
	}

	private static String getProperty(String key) {
		String dbName;
		dbName = System.getProperty(key);

		if (dbName == null) dbName = System.getenv(key);
		if (dbName == null) {
			Properties p = new Properties();
			try {
				p.load(WebServerApplication.class.getResourceAsStream("/application.properties"));
				dbName = p.getProperty(key);
			} catch (IOException ioException) {
				// ignore if not found
			}
		}
		return dbName;
	}
}
