package com.team4.SmartHabitat.Config;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SparqlConfig {

    @Value("${api.graphDB}")
    private String sparqlEndpointUrl;

    // @Value("${api.graphDB.update}")
    // private String sparqlUpdateEndpointUrl;

    // @Primary
    @Bean
    public SPARQLRepository sparqlQueryRepository() {
        SPARQLRepository repository = new SPARQLRepository(sparqlEndpointUrl, sparqlEndpointUrl + "/statements");
        repository.init();
        return repository;
    }

    // @Bean
    // public SPARQLRepository sparqlUpdateRepository() {
    //     SPARQLRepository updateRepository = new SPARQLRepository(sparqlUpdateEndpointUrl);
    //     updateRepository.init();
    //     return updateRepository;
    // }
}
