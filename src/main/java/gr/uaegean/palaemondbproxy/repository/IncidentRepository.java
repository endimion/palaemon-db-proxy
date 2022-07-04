package gr.uaegean.palaemondbproxy.repository;

import gr.uaegean.palaemondbproxy.model.Incident;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface IncidentRepository extends ElasticsearchRepository<Incident, String> {

    Optional<Incident> findByIncidentId(String incidentId);
}
