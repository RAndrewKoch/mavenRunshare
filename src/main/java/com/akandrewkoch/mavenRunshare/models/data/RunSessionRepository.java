package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.RunSession;
import com.akandrewkoch.mavenRunshare.models.Runner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RunSessionRepository extends CrudRepository<RunSession, Integer> {

    RunSession findByName(String name);

    RunSession findById(int id);

    List<RunSession> findAllByCreator_Id(Integer creatorId);

    List<RunSession> findAllByOrderByNameAsc();
    List<RunSession> findAllByOrderByNameDesc();

    List<RunSession> findAllByOrderByDateDescTimeDesc();

    List<RunSession> findAllByOrderByTimeAsc();
    List<RunSession> findAllByOrderByTimeDesc();

    List<RunSession> findAllByOrderByDateAsc();
    List<RunSession> findAllByOrderByDateDesc();

    List<RunSession> findAllByRunners(Runner runners);
}
