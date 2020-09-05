package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.Trail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrailRepository extends CrudRepository<Trail, Integer> {

    Trail findByName(String name);

    Trail findById(int id);

    List<Trail> findAllByOrderByNameAsc ();
    List<Trail> findAllByOrderByNameDesc ();

    List<Trail> findAllByOrderByAddressAsc();
    List<Trail> findAllByOrderByAddressDesc();

    List<Trail> findAllByOrderByMilesAsc ();
    List<Trail> findAllByOrderByMilesDesc ();
}
