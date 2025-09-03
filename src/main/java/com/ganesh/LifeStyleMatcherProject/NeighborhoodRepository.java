package com.ganesh.LifeStyleMatcherProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    List<Neighborhood> findByCityIgnoreCase(String city);
    @Query("SELECT l FROM Listing l WHERE l.neighborhood.id = :nid AND l.rentMin <= :budget ORDER BY l.rentMin ASC")
    List<Listing> findTop3ByNeighborhoodAndBudget(@Param("nid") Long nid, @Param("budget") int budget, Pageable pageable);
}

