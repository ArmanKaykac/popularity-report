package com.popularity.report.repository;

import com.popularity.report.model.ImageData;
import com.popularity.report.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Long countByImageDataAndVoteDateAfter(ImageData imageData, Date startDate);

}
