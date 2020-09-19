package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.TrailSceneryRating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrailSceneryRatingRepository extends CrudRepository<TrailSceneryRating, Integer> {

    TrailSceneryRating findByRunner_IdAndTrail_Id(Integer runner, Integer trail);

    List<TrailSceneryRating> findAllByTrail_Id(Integer trailId);

    List<TrailSceneryRating> findAllByRunner_Id(Integer runnerId);
}
