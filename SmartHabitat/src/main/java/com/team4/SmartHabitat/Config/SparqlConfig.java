package com.team4.SmartHabitat.Config;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparqlConfig {

	@Value("${api.graphDB}")
    private String sparqlEndpointUrl;
	
	 @Bean
	    public SPARQLRepository sparqlRepository() {
	        SPARQLRepository repository = new SPARQLRepository(sparqlEndpointUrl);
	       
	        repository.init();
	        return repository;
	    }
}
