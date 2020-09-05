package com.akandrewkoch.mavenRunshare.models.data;

import com.akandrewkoch.mavenRunshare.models.TrailDifficultyRating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrailDifficultyRatingRepository extends CrudRepository<TrailDifficultyRating, Integer> {

    TrailDifficultyRating findByRunner_IdAndTrail_Id(Integer runner, Integer trail);

    List<TrailDifficultyRating> findAllByTrail_Id(Integer trailId);

    List<TrailDifficultyRating> findAllByRunner_Id(Integer runnerId);

}
