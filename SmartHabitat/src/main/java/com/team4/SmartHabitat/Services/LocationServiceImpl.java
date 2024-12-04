package com.team4.SmartHabitat.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.shacl.sys.C;
import org.apache.jena.sparql.function.library.leviathan.cartesian;
import org.apache.jena.sparql.function.library.leviathan.log;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.SmartHabitat.Entity.Community;
import com.team4.SmartHabitat.Entity.Preference;

@Service
public class LocationServiceImpl implements LocationService {

    private final SPARQLRepository sparqlQueryRepository;

    @Autowired
    public LocationServiceImpl(SPARQLRepository sparqlQueryRepository) {
        this.sparqlQueryRepository = sparqlQueryRepository;
    }

    @Override
    public void getService() {
        System.out.println("in dervice");
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void CalcCrimeIndex() {
        String queryCritical = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?community (COUNT(?crimes) as ?Critical) \r\n" + //
                        "WHERE \r\n" + //
                        "{ ?community a smh:Community. \r\n" + //
                        "\t?community smh:hasCrime ?crimes.\r\n" + //
                        "\t?crimes smh:hasCategory ?category.\r\n" + //
                        "\t?category a smh:Critical.\r\n" + //
                        "}\r\n" + //
                        "Group By ?community\r\n" + //
                        "Order By DESC (?Critical)";
        String queryModerate = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?community (COUNT(?crimes) as ?Moderate) \r\n" + //
                        "WHERE \r\n" + //
                        "{ ?community a smh:Community. \r\n" + //
                        "\t?community smh:hasCrime ?crimes.\r\n" + //
                        "\t?crimes smh:hasCategory ?category.\r\n" + //
                        "\t?category a smh:Moderate.\r\n" + //
                        "}\r\n" + //
                        "Group By ?community\r\n" + //
                        "Order By DESC (?Moderate)";
        String querySerious = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?community (COUNT(?crimes) as ?Serious) \r\n" + //
                        "WHERE \r\n" + //
                        "{ ?community a smh:Community. \r\n" + //
                        "\t?community smh:hasCrime ?crimes.\r\n" + //
                        "\t?crimes smh:hasCategory ?category.\r\n" + //
                        "\t?category a smh:Serious.\r\n" + //
                        "}\r\n" + //
                        "Group By ?community\r\n" + //
                        "Order By DESC (?Serious)";
        String queryAllIndex = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?community ?critIndex ?SerIndex ?modIndex where {\r\n" + //
                        "    ?community a smh:Community;\r\n" + //
                        "    \tsmh:CriticalCrimesIndex ?critIndex;\r\n" + //
                        "    \tsmh:SeriousCrimesIndex ?SerIndex;\r\n" + //
                        "    \tsmh:ModerateCrimesIndex ?modIndex.\r\n" + //
                        "}";
        String queryFinalIndex = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?community ?finalIndex where {\r\n" + //
                        "    ?community a smh:Community;\r\n" + //
                        "    \tsmh:CrimeIndexRaw ?finalIndex.\r\n" + //
                        "}";

        // Fetch Crimes Create Index and Insert into RDF
        try (var connection = sparqlQueryRepository.getConnection()) {
            TupleQuery tupleQueryCritical = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryCritical);
            TupleQuery tupleQuerySerious = connection.prepareTupleQuery(QueryLanguage.SPARQL, querySerious);
            TupleQuery tupleQueryModerate = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryModerate);
            TupleQuery tupleQueryAllIndex = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryAllIndex);
            TupleQuery tupleQueryFinalIndex = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryFinalIndex);
            try (TupleQueryResult result = tupleQueryCritical.evaluate()) {
                Map<String, Float> communityCriticalIndexMap = new HashMap<>();
                int maxCrimes = 0;
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    String critStr = (bindingSet.getValue("Critical").toString().substring(1, bindingSet.getValue("Critical").toString().indexOf("^") - 1));
                    int criticalValue = Integer.parseInt(critStr);
                    if (criticalValue > maxCrimes){
                        maxCrimes = criticalValue;
                    }
                    float criticalIndex = ((float)criticalValue / maxCrimes) * 9 + 1;
                    String community = bindingSet.getValue("community").toString();
                    communityCriticalIndexMap.put(community, criticalIndex);
                }
                for (Map.Entry<String, Float> entry : communityCriticalIndexMap.entrySet()) {
                    String community = entry.getKey();
                    float criticalIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                        "INSERT DATA { <" + community + "> smh:CriticalCrimesIndex " + criticalIndex + " }";
                    Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
                    update.execute();
                }
            }
            try (TupleQueryResult result = tupleQuerySerious.evaluate()) {
                Map<String, Float> communitySeriousIndexMap = new HashMap<>();
                int maxCrimes = 0;
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    String serStr = (bindingSet.getValue("Serious").toString().substring(1, bindingSet.getValue("Serious").toString().indexOf("^") - 1));
                    int seriousValue = Integer.parseInt(serStr);
                    if (seriousValue > maxCrimes){
                        maxCrimes = seriousValue;
                    }
                    float seriousIndex = ((float)seriousValue / maxCrimes) * 9 + 1;
                    String community = bindingSet.getValue("community").toString();
                    communitySeriousIndexMap.put(community, seriousIndex);
                }
                for (Map.Entry<String, Float> entry : communitySeriousIndexMap.entrySet()) {
                    String community = entry.getKey();
                    float seriousIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                         "INSERT DATA { <" + community + "> smh:SeriousCrimesIndex " + seriousIndex + " }";
                    Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
                    update.execute();
                }
            }
            try (TupleQueryResult result = tupleQueryModerate.evaluate()) {
                Map<String, Float> communityModerateIndexMap = new HashMap<>();
                int maxCrimes = 0;
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    String modStr = (bindingSet.getValue("Moderate").toString().substring(1, bindingSet.getValue("Moderate").toString().indexOf("^") - 1));
                    int moderateValue = Integer.parseInt(modStr);
                    if (moderateValue > maxCrimes){
                        maxCrimes = moderateValue;
                    }
                    float moderateIndex = ((float)moderateValue / maxCrimes) * 9 + 1;
                    String community = bindingSet.getValue("community").toString();
                    communityModerateIndexMap.put(community, moderateIndex);
                }
                for (Map.Entry<String, Float> entry : communityModerateIndexMap.entrySet()) {
                    String community = entry.getKey();
                    float moderateIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                         "INSERT DATA { <" + community + "> smh:ModerateCrimesIndex " + moderateIndex + " }";
                    Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
                    update.execute();
                }
            }
        
            try (TupleQueryResult result = tupleQueryAllIndex.evaluate()){
                Map<String, Float> communityFinalIndexMapping = new HashMap<>();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    String community = bindingSet.getValue("community").toString();
                    String critStr = (bindingSet.getValue("critIndex").toString().substring(1, bindingSet.getValue("critIndex").toString().indexOf("^") - 1));
                    String serStr = (bindingSet.getValue("SerIndex").toString().substring(1, bindingSet.getValue("SerIndex").toString().indexOf("^") - 1));
                    String modStr = (bindingSet.getValue("modIndex").toString().substring(1, bindingSet.getValue("modIndex").toString().indexOf("^") - 1));
                    float critIndex = Float.parseFloat(critStr);
                    float serIndex = Float.parseFloat(serStr);
                    float modIndex = Float.parseFloat(modStr);
                    float finalIndex = ((critIndex * 8) + (serIndex * 5) + (modIndex * 3));
                    communityFinalIndexMapping.put(community, finalIndex);
                }
                for (Map.Entry<String, Float> entry : communityFinalIndexMapping.entrySet()) {
                    String community = entry.getKey();
                    float finalIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                         "INSERT DATA { <" + community + "> smh:CrimeIndexRaw " + finalIndex + " }";
                    Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
                    update.execute();
                }
            }
            try (TupleQueryResult result = tupleQueryFinalIndex.evaluate()){
                float maxIndex = 0;
                Map<String, Float> communityFinalIndexMapping = new HashMap<>();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    String community = bindingSet.getValue("community").toString();
                    String finalIndex = (bindingSet.getValue("finalIndex").toString().substring(1, bindingSet.getValue("finalIndex").toString().indexOf("^") - 1));
                    float finalIndexValue = Float.parseFloat(finalIndex);
                    if (finalIndexValue > maxIndex){
                        maxIndex = finalIndexValue;
                    }
                    float finalNormalizedIndex = (finalIndexValue / maxIndex) * 10;
                    communityFinalIndexMapping.put(community, finalNormalizedIndex);
                }
                for (Map.Entry<String, Float> entry : communityFinalIndexMapping.entrySet()) {
                    String community = entry.getKey();
                    float finalIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                         "INSERT DATA { <" + community + "> smh:CrimeIndex " + finalIndex + " }";
                    Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
                    update.execute();
                }
            }
        }
    }

    @Override
    public void CalcEnvironmentIndex() {
        String queryAirQuality = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                "SELECT ?county (AVG(?airQuality) AS ?AirQualityIndex) \r\n" +
                                "WHERE {\r\n" +
                                "   ?county a smh:County;\r\n" +
                                "               smh:AirQualityIndex ?airQuality.\r\n" +
                                "}\r\n" +
                                "GROUP BY ?county";

        String queryHeatIndex = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                "SELECT ?county (AVG(?heat) AS ?HeatIndex) \r\n" +
                                "WHERE {\r\n" +
                                "   ?county a smh:County;\r\n" +
                                "               smh:HeatIndex ?heat.\r\n" +
                                "}\r\n" +
                                "GROUP BY ?county";

        String queryPrecipitation = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                    "SELECT ?county (AVG(?precipitation) AS ?PrecipitationIndex) \r\n" +
                                    "WHERE {\r\n" +
                                    "   ?county a smh:County;\r\n" +
                                    "               smh:PrecipitationIndex ?precipitation.\r\n" +
                                    "}\r\n" +
                                    "GROUP BY ?county";

        String queryUV = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                        "SELECT ?county (AVG(?uv) AS ?UVIndex) \r\n" +
                        "WHERE {\r\n" +
                        "   ?county a smh:County;\r\n" +
                        "               smh:UVIndex ?uv.\r\n" +
                        "}\r\n" +
                        "GROUP BY ?county";

        try (RepositoryConnection connection = sparqlQueryRepository.getConnection()) {
            normalizeAndInsertIndex(connection, queryAirQuality, "AirQualityIndex", "smh:AirQualityIndexNormalized");

            normalizeAndInsertIndex(connection, queryHeatIndex, "HeatIndex", "smh:HeatIndexNormalized");

            normalizeAndInsertIndex(connection, queryPrecipitation, "PrecipitationIndex", "smh:PrecipitationIndexNormalized");

            normalizeAndInsertIndex(connection, queryUV, "UVIndex", "smh:UVIndexNormalized");
        }
}

private void normalizeAndInsertIndex(RepositoryConnection connection, String query, String indexName, String normalizedPredicate) {
    try (TupleQueryResult result = connection.prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate()) {
        Map<String, Float> countyIndexMap = new HashMap<>();
        float maxIndex = 0;

        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String indexStr = bindingSet.getValue(indexName).toString().substring(1, bindingSet.getValue(indexName).toString().indexOf("^") - 1);
            float indexValue = Float.parseFloat(indexStr);
            if (indexValue > maxIndex) {
                maxIndex = indexValue;
            }
            String county = bindingSet.getValue("county").toString();
            countyIndexMap.put(county, (float) indexValue);
        }
        for (Map.Entry<String, Float> entry : countyIndexMap.entrySet()) {
            String county = entry.getKey();
            float normalizedIndex = (entry.getValue() / maxIndex) * 9 + 1;
            String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                 "INSERT DATA { <" + county + "> " + normalizedPredicate + " " + normalizedIndex + " }";

            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
            update.execute();
        }
    }
}


    @Override
    public List<Map.Entry<String, Float>> CalculateOverallIndex(Preference preference) {

        String queryFetchIndicesByCommunities = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
        "SELECT ?community ?finalCrimeIndex ?heat ?uv ?precipitation ?airQuality\r\n" +
        "WHERE {\r\n" +
        "  ?community a smh:Community .\r\n" +
        "  ?community smh:isLocatedIn ?county .\r\n" +
        "  ?county a smh:County .\r\n" +
        "  ?community smh:CrimeIndexRaw ?finalCrimeIndex .\r\n" +
        "  ?county smh:HeatIndexNormalized ?heat .\r\n" +
        "  ?county smh:UVIndexNormalized ?uv .\r\n" +
        "  ?county smh:PrecipitationIndexNormalized ?precipitation .\r\n" +
        "  ?county smh:AirQualityIndexNormalized ?airQuality .\r\n" +
        "}";
        
        String City = preference.city;
        String queryFetchIndicesByCommunitiesForCity = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
        "SELECT ?community ?finalCrimeIndex ?heat ?uv ?precipitation ?airQuality\r\n" +
        "WHERE {\r\n" +
        "  ?community a smh:Community .\r\n" +
        "  ?community smh:isLocatedIn smh:" + City + " .\r\n" +
        "  ?community smh:isLocatedIn ?county .\r\n" +
        "  ?county a smh:County .\r\n" +
        "  ?community smh:CrimeIndexRaw ?finalCrimeIndex .\r\n" +
        "  ?county smh:HeatIndexNormalized ?heat .\r\n" +
        "  ?county smh:UVIndexNormalized ?uv .\r\n" +
        "  ?county smh:PrecipitationIndexNormalized ?precipitation .\r\n" +
        "  ?county smh:AirQualityIndexNormalized ?airQuality .\r\n" +
        "}";

        try (var connection = sparqlQueryRepository.getConnection()) {
            TupleQuery tupleQuery;
            if("Any".equals(City)) {
                tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryFetchIndicesByCommunities);
            }
            else {
                tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryFetchIndicesByCommunitiesForCity);
            }

            // Evaluate the query and calculate the Environment Index for each Community
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                Map<String, Float> communityOverallIndexMap = new HashMap<>();

                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    // Fetch community and indices
                    String community = bindingSet.getValue("community").toString();
                    
                    float airQuality = Float.parseFloat(bindingSet.getValue("airQuality").stringValue());

                    float finalCrimeIndex = Float.parseFloat(bindingSet.getValue("finalCrimeIndex").stringValue());

                    float precipitation = Float.parseFloat(bindingSet.getValue("precipitation").stringValue());

                    float heat = Float.parseFloat(bindingSet.getValue("heat").stringValue());

                    float uv = Float.parseFloat(bindingSet.getValue("uv").stringValue());

                    float crimeWeightage =  preference.crimePreferencePercent;
                    float environmentWeightage=  preference.environmentPreferencePercent;


                    // Calculate Environment Index
                    float environmentIndex = (preference.airQualityPriority * airQuality) +
                                            (preference.precipationPriority * precipitation) +
                                            (preference.heatMetricPriority * heat) +
                                            (preference.uvRadiationPriority * uv);

                    environmentIndex = normalizeEnvIndex(environmentIndex);

                    //calulating the habitable index with final env and crime index
                    float overallIndex = environmentIndex * (environmentWeightage/100) + finalCrimeIndex * (crimeWeightage/100);  
                    communityOverallIndexMap.put(community, overallIndex);

                }
                //sort the map in descending order
                List<Map.Entry<String, Float>> indexList = new ArrayList<>(communityOverallIndexMap.entrySet());
                indexList.sort((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()));
        
                int topN = Math.min(5, indexList.size());
                List<Map.Entry<String, Float>> top5Entries = indexList.subList(0, topN);
                return top5Entries;

            }
        }

    }

    public float normalizeEnvIndex(float currentEnvIndex) {
        float minValue = 10.0f; // Minimum possible value ((1×1)+(2×1)+(3×1)+(4×1)=10)
        float maxValue = 100.0f; // Maximum possible value ((1×10)+(2×10)+(3×10)+(4×10)=100)
        float newMin = 1.0f; // Desired normalized range minimum
        float newMax = 10.0f; // Desired normalized range maximum
    
        // Apply normalization formula
        return ((currentEnvIndex - minValue) / (maxValue - minValue)) * (newMax - newMin) + newMin;
    }

    @Override   
    public Community getCommunityDetails(Community name) {
        String City = name.IRI;
        String query = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" + //
                        "select ?name ?seriousCrimeIndex ?moderateCrimeIndex ?criticalCrimeIndex (AVG(?AQI) as ?AQI) (AVG(?heatIndex) as ?heat) (AVG(?uvIndex) as ?uv) (AVG(?PrecIndex) as ?Prec)\r\n" + //
                        "where {\r\n" + //
                        "    <"+ City +"> a smh:Community ;\r\n" + //
                        "    \tsmh:hasName ?name;\r\n" + //
                        "    \tsmh:CriticalCrimesIndex ?criticalCrimeIndex;\r\n" + //
                        "    \tsmh:ModerateCrimesIndex ?moderateCrimeIndex;\r\n" + //
                        "    \tsmh:SeriousCrimesIndex ?seriousCrimeIndex;\r\n" + //
                        "    \tsmh:isLocatedIn ?county.\r\n" + //
                        "    ?county a smh:County;\r\n" + //
                        "    \tsmh:AirQualityIndex ?AQI;\r\n" + //
                        "    \tsmh:HeatIndex ?heatIndex;\r\n" + //
                        "    \tsmh:PrecipitationIndex ?PrecIndex;\r\n" + //
                        "    \tsmh:UVIndex ?uvIndex.\r\n" + //
                        "} Group By ?name ?seriousCrimeIndex ?moderateCrimeIndex ?criticalCrimeIndex";
        try (var connection = sparqlQueryRepository.getConnection()) {
            TupleQuery queryCall = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            try(TupleQueryResult result = queryCall.evaluate()) {
                if(result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    Community community = new Community();
                    community.name = bindingSet.getValue("name").stringValue();
                    community.seriousCrimeIndex = Float.parseFloat(bindingSet.getValue("seriousCrimeIndex").stringValue());
                    community.moderateCrimeIndex = Float.parseFloat(bindingSet.getValue("moderateCrimeIndex").stringValue());
                    community.criticalCrimeIndex = Float.parseFloat(bindingSet.getValue("criticalCrimeIndex").stringValue());
                    community.airQualityIndex = Float.parseFloat(bindingSet.getValue("AQI").stringValue());
                    community.heatIndex = Float.parseFloat(bindingSet.getValue("heat").stringValue());
                    community.uvRadiationIndex = Float.parseFloat(bindingSet.getValue("uv").stringValue());
                    community.precipitationIndex = Float.parseFloat(bindingSet.getValue("Prec").stringValue());
                    return community;
                }
                else {
                    return null;
                }
            }
        }
    }

}
