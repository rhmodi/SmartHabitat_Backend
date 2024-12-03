package com.team4.SmartHabitat.Services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        // Fetch Crimes Create Index and Insert into RDF
        try (var connection = sparqlQueryRepository.getConnection()) {
            TupleQuery tupleQueryCritical = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryCritical);
            TupleQuery tupleQuerySerious = connection.prepareTupleQuery(QueryLanguage.SPARQL, querySerious);
            TupleQuery tupleQueryModerate = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryModerate);
            TupleQuery tupleQueryAllIndex = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryAllIndex);
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
                    float criticalIndex = ((float)criticalValue / maxCrimes) * 10;
                    String community = bindingSet.getValue("community").toString();
                    communityCriticalIndexMap.put(community, criticalIndex);
                    System.out.println(community + " -> " + criticalIndex);
                }
                for (Map.Entry<String, Float> entry : communityCriticalIndexMap.entrySet()) {
                    String community = entry.getKey();
                    float criticalIndex = entry.getValue();
                    String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                        "INSERT DATA { <" + community + "> smh:CriticalCrimesIndex " + criticalIndex + " }";
                    System.out.println(insertQuery);
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
                    float seriousIndex = ((float)seriousValue / maxCrimes) * 10;
                    String community = bindingSet.getValue("community").toString();
                    communitySeriousIndexMap.put(community, seriousIndex);
                    System.out.println(community + " -> " + seriousIndex);
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
                    float moderateIndex = ((float)moderateValue / maxCrimes) * 10;
                    String community = bindingSet.getValue("community").toString();
                    communityModerateIndexMap.put(community, moderateIndex);
                    System.out.println(community + " -> " + moderateIndex);
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
            float normalizedIndex = (entry.getValue() / maxIndex) * 10;
            String insertQuery = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                                 "INSERT DATA { <" + county + "> " + normalizedPredicate + " " + normalizedIndex + " }";

            System.out.println(insertQuery);
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, insertQuery);
            update.execute();
        }
    }
}


    public void insertEnvIndex() {

        String queryAllIndex = "PREFIX smh: <http://www.semanticweb.org/team4/ontologies/2024/10/smartHabitat#>\r\n" +
                       "INSERT { ?community smh:hasEnvironmentIndex 0 . } \r\n" +
                       "WHERE { ?community a smh:Community . }";
        
        try (var connection = sparqlQueryRepository.getConnection()) {
            Update update = connection.prepareUpdate(QueryLanguage.SPARQL, queryAllIndex);
            update.execute();
        }
    }

}
