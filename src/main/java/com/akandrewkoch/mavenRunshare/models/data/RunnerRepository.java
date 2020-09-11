package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.Runner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunnerRepository extends CrudRepository<Runner, Integer> {

    Runner findByCallsign(String callsign);

    Runner findById(int id);


    List<Runner> findAllByOrderByCallsignAsc();
    List<Runner> findAllByOrderByCallsignDesc();

    List<Runner> findAllByOrderByFirstNameAsc();
    List<Runner> findAllByOrderByFirstNameDesc();

    List<Runner> findAllByOrderByLastNameAsc();
    List<Runner> findAllByOrderByLastNameDesc();

    List<Runner> findAllByOrderByRunnerLevelAsc();
    List<Runner> findAllByOrderByRunnerLevelDesc();

    List<Runner> findAllByOrderByAgeAsc();
    List<Runner> findAllByOrderByAgeDesc();

    List<Runner> findAllByRunSessionPackId(Integer runSessionPack);
}
