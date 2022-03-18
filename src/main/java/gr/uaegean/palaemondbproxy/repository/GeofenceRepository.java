package gr.uaegean.palaemondbproxy.repository;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GeofenceRepository extends ElasticsearchRepository<Geofence, String> {

}
