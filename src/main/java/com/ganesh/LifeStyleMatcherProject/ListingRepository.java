package com.ganesh.LifeStyleMatcherProject;// In file: ListingRepository.java

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    // This method is crucial. It finds the top 3 listings in a neighborhood
    // that are under the budget, ordered by the lowest rent.
    List<Listing> findTop5ByNeighborhoodIdAndRentMinLessThanEqualOrderByRentMinAsc(
            Long neighborhoodId, int budget);

    // You can keep or remove this other method depending on if you use it elsewhere.
    List<Listing> findByNeighborhoodIdAndRentMinLessThanEqual(Long neighborhoodId, Integer maxRent);
}