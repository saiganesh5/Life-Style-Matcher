//package com.ganesh.LifeStyleMatcherProject;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import java.util.ArrayList;
//import java.util.List;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class MatchController {
//
//    public record MatchResult(
//            String name,
//            String city,
//            int score,
//            @JsonProperty("avg_rent") int avgRent,
//            Listing.ListingType type,
//            String bhkOrRoomType,
//            String location,
//            String furnishing,
//            Listing.Gender gender,
//            String source
//    ) {}
//
//    private final NeighborhoodRepository neighborhoodRepository;
//    private final ListingRepository listingRepository;
//
//    @PostMapping("/match")
//    public ResponseEntity<List<MatchResult>> getMatches(@RequestBody MatchRequest request) {
//        String targetCity = "Bangalore"; // For now, static
//
//        List<Neighborhood> neighborhoods = neighborhoodRepository.findByCityIgnoreCase(targetCity);
//        List<MatchResult> results = new ArrayList<>();
//
//        for (Neighborhood n : neighborhoods) {
//            int score = calculateScore(n, request);
//
//            // Top 3 listings in budget
//            List<Listing> listings = listingRepository
//                    .findTop3ByCityAndNeighborhoodIdAndRentMinLessThanEqualOrderByRentMinAsc(
//                            targetCity, n.getId(), request.getBudget()
//                    );
//
//            for (Listing l : listings) {
//                MatchResult res = new MatchResult(
//                        n.getName(),
//                        n.getCity(),
//                        score,
//                        (l.getRentMin() + l.getRentMax()) / 2,
//                        l.getType(),
//                        l.getBhkOrRoomType(),
//                        l.getLocation(),
//                        l.getFurnishing(),
//                        l.getGender(),
//                        l.getSource()
//                );
//                results.add(res);
//            }
//        }
//
//        results.sort((a, b) -> b.score() - a.score());
//        return ResponseEntity.ok(results.stream().limit(10).toList());
//    }
//
//    private int calculateScore(Neighborhood n, MatchRequest req) {
//        int score = 0;
//        MatchRequest.Priorities p = req.getPriorities();
//
//        if (req.isGym() && n.isGymAccess()) score += 5;
//        if (req.isHospital() && n.isHospitalAccess()) score += 5;
//        if (req.isPet() && n.isPetFriendly()) score += 5;
//
//        score += scale(n.getSafetyRating(), p.getSafety());
//        score += scale(n.getWalkabilityScore(), p.getWalkability());
//        score += scale(n.getFoodOutlets(), p.getFood());
//        score += scale(n.getInternetConnectivity(), p.getInternet());
//        score += scale(n.getTransportScore(), p.getTransport());
//        score += scale(n.getMarketScore(), p.getMarket());
//        score += (10 - Math.abs(n.getNoiseLevel() - p.getNoise())); // Less deviation = better
//
//        return score;
//    }
//
//    private int scale(int value, int weight) {
//        return (value * weight) / 10;
//    }
//}
package com.ganesh.LifeStyleMatcherProject;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchController {

    public record MatchResult(
            String name,
            String city,
            int score,
            int avgRent, // Changed from Rent to avgRent
            String type,
            String bhkOrRoomType,
            String location, // Added location field
            String furnishing,
            String gender,
            String source
    ) {}

    private final NeighborhoodRepository neighborhoodRepository;
    private final ListingRepository listingRepository;

    @PostMapping("/match")
    public ResponseEntity<List<MatchResult>> getMatches(@RequestBody MatchRequest request) {
        String targetCity = "Ahmedabad"; // This can be made dynamic later
        List<Neighborhood> neighborhoods = neighborhoodRepository.findByCityIgnoreCase(targetCity);
        List<MatchResult> results = new ArrayList<>();

        for (Neighborhood n : neighborhoods) {
            // First, apply boolean filters (gym, hospital, pet)
            if ((request.isGym() && !n.isGymAccess()) ||
                    (request.isHospital() && !n.isHospitalAccess()) ||
                    (request.isPet() && !n.isPetFriendly())) {
                continue; // Skip neighborhood if it doesn't meet mandatory requirements
            }

            int score = calculateScore(n, request);

            // Fetch the top 3 listings within the user's budget for this neighborhood
            List<Listing> listings = listingRepository
                    .findTop5ByNeighborhoodIdAndRentMinLessThanEqualOrderByRentMinAsc(
                            n.getId(), request.getBudget()
                    );

            for (Listing l : listings) {
                // Create a result for each matching listing
                MatchResult res = new MatchResult(
                        n.getName(),
                        n.getCity(),
                        score,
                        l.getRentMin(), // Calculate average rent for the listing
                        l.getType().toString(), // Use the listing's type
                        l.getBhkOrRoomType(),
                        l.getLocation(),      // Use the listing's specific location
                        l.getFurnishing(),
                        l.getGender().toString(),
                        l.getSource()
                );
                results.add(res);
            }
        }

        // Sort all results by score (descending) and take the top 10
        results.sort((a, b) -> Integer.compare(b.score(), a.score()));
        List<MatchResult> topResults = results.stream().limit(10).toList();

        return ResponseEntity.ok(topResults);
    }

    private boolean neighborhoodMatchesFilters(Neighborhood n, MatchRequest req) {
        return (!req.isGym() || n.isGymAccess()) &&
                (!req.isHospital() || n.isHospitalAccess()) &&
                (!req.isPet() || n.isPetFriendly()) &&
                n.getAvgRent() <= req.getBudget();
    }

    private int calculateScore(Neighborhood n, MatchRequest req) {
        int score = 0;
        MatchRequest.Priorities p = req.getPriorities();

        if (req.isGym() && n.isGymAccess()) score += 5;
        if (req.isHospital() && n.isHospitalAccess()) score += 5;
        if (req.isPet() && n.isPetFriendly()) score += 5;

        score += scale(n.getSafetyRating(), p.getSafety());
        score += scale(n.getWalkabilityScore(), p.getWalkability());
        score += scale(n.getFoodOutlets(), p.getFood());
        score += scale(n.getInternetConnectivity(), p.getInternet());
        score += scale(n.getTransportScore(), p.getTransport());
        score += scale(n.getMarketScore(), p.getMarket());

        // Invert noise level for better score if user's preferred noise is low
        score += (10 - Math.abs(n.getNoiseLevel() - p.getNoise()));

        return score;
    }

    private int scale(int value, int weight) {
        return (value * weight) / 10;
    }
}
