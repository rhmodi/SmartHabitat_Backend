import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.springframework.stereotype.Service;

@Service
public class SparqlService {

    private final Repository repository;

    public SparqlService(@Value("${graphdb.url}") String graphDbUrl) {
        this.repository = new HTTPRepository(graphDbUrl);
        this.repository.initialize();
    }

    public Repository getRepository() {
        return repository;
    }
}