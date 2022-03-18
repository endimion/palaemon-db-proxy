package gr.uaegean.palaemondbproxy.repository;

import gr.uaegean.palaemondbproxy.model.PameasPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PameasPersonRepository extends ElasticsearchRepository<PameasPerson, String> {

    Page<PameasPerson> findByPersonalInfoName(String name, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"personalInfo.crew\": \"true\"}}]}}")
    List<PameasPerson> findCrewMembers();


//    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
//    Page<Article> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
