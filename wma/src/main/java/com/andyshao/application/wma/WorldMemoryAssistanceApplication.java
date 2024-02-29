package com.andyshao.application.wma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration;

@SpringBootApplication(exclude = {
		Neo4jAutoConfiguration.class, Neo4jDataAutoConfiguration.class, Neo4jRepositoriesAutoConfiguration.class,
		Neo4jReactiveDataAutoConfiguration.class, Neo4jReactiveRepositoriesAutoConfiguration.class
})
public class WorldMemoryAssistanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldMemoryAssistanceApplication.class, args);
	}

}
