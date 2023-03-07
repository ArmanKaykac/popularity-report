package com.popularity.report.repository;

import com.popularity.report.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image_data ORDER BY RANDOM() LIMIT 2")
    List<ImageData> getRandomImages();

}