package com.team4.SmartHabitat.Services;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

	 private final SPARQLRepository sparqlRepository;

	    @Autowired
	    public LocationServiceImpl(SPARQLRepository sparqlRepository) {
	        this.sparqlRepository = sparqlRepository;
	    }
	
    @Override
    public void getService() {
        System.out.println("in dervice");
        // throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
