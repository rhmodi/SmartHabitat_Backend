package com.team4.SmartHabitat.Services;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.SmartHabitat.Entity.Preference;


@Service
public class LocationServiceImpl implements LocationService {

	private final SPARQLRepository sparqlRepository;
    public String prefixes =
    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
    "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>";

    @Autowired
    public LocationServiceImpl(SPARQLRepository sparqlRepository) {
        this.sparqlRepository = sparqlRepository;
    }
	
    @Override
    public void requestHabitatImpl(Preference preference) {
        System.out.println(preference.uvRadiationPriority);
        this.fetchLocations();
        
    }

    public void fetchLocations() {
        String query = this.prefixes + "SELECT ?location ?label WHERE { ?location smh:hasName ?label } LIMIT 10";

        try (var connection = sparqlRepository.getConnection()) {
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    System.out.println(bindingSet.getValue("location") + " -> " + bindingSet.getValue("label"));
                }
            }
        }
    }

    // public void updateEvnIndices(Preference preference) {
    //     String query2 = 
    //     "DELETE {
    //         ?subject ex:hasEnvironmentIndex ?value .
    //       }
    //       INSERT {
    //         ?subject ex:hasEnvironmentIndex ?newEnvironmentIndex .
    //       }
    //       WHERE {
    //         ?subject ex:hasEnvironmentIndex ?value ;
    //                  ex:hasHeatMetric ?heatMetric ;
    //                  ex:hasPrecipitation ?precipitation .
          
    //        BIND((?heatMetric * " + preference.heatMetricPriority +
    //        "+ ?precipitation * " + preference.precipationPriority +
    //        "+ ?uvRadiation * " + preference.uvRadiationPriority +
    //        "+ ?airQuality * " + preference.airQualityPriority +
    //        ") AS ?newEnvironmentIndex)
    //       }"
    // }

    
}
