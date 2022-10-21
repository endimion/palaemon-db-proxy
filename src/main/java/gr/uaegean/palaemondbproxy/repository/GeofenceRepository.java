package gr.uaegean.palaemondbproxy.repository;

import gr.uaegean.palaemondbproxy.model.Geofence;
import gr.uaegean.palaemondbproxy.model.PameasPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GeofenceRepository extends ElasticsearchRepository<Geofence, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"gfName\": \"true\"}}]}}")
    List<PameasPerson> findByGeofenceName();

}
