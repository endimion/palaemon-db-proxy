package gr.uaegean.palaemondbproxy.repository;

import gr.uaegean.palaemondbproxy.model.Incident;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IncidentRepository extends ElasticsearchRepository<Incident, String> {

}
