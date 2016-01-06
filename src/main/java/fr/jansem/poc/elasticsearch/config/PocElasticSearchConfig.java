package fr.jansem.poc.elasticsearch.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableAutoConfiguration
@Configuration
@ComponentScan({ "fr.jansem.poc.elasticsearch" })
@EnableElasticsearchRepositories(basePackages = "fr/jansem/poc/elasticsearch/repository")
public class PocElasticSearchConfig extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PocElasticSearchConfig.class, args);
	}

}
