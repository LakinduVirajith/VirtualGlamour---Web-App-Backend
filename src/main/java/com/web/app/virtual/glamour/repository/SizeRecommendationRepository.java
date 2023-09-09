package com.web.app.virtual.glamour.repository;

import com.web.app.virtual.glamour.entity.SizeRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRecommendationRepository extends JpaRepository<SizeRecommendation, Long> {
}
