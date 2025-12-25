//package com.ganesh.LifeStyleMatcherProject;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//
//@Service
//public class ListingImportService {
//
//    @Autowired
//    private ListingRepository listingRepository;
//
//    @PostConstruct
//    public void importCSV() {
//        try {
//            Resource resource = new ClassPathResource("final_all_cities_merged_cleaned.csv");
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
//            );
//
//            String line;
//            reader.readLine(); // Skip header
//
//            while ((line = reader.readLine()) != null) {
//                String[] fields = line.split(",", -1);
//
//                if (fields.length < 9) continue; // Skip invalid rows
//
//                Listing listing = new Listing();
//                listing.setCity(fields[0].trim());
//
//                // Remove ₹ and commas, then parse rent
//                String rentClean = fields[1].replaceAll("[^\\d]", "");
//                listing.setRent(rentClean.isEmpty() ? 0 : Integer.parseInt(rentClean));
//
//                listing.setType(Listing.Type.valueOf(fields[2].trim()));
//                listing.setBhkOrRoomType(fields[3].trim());
//                listing.setArea(fields[4].trim());
//                listing.setFurnishing(fields[5].trim());
//
//                // Gender enum handling
//                String genderStr = fields[6].trim().toLowerCase();
//                if (genderStr.equals("male")) listing.setGender(Listing.Gender.Male);
//                else if (genderStr.equals("female")) listing.setGender(Listing.Gender.Female);
//                else listing.setGender(Listing.Gender.Both);
//
//                listing.setLocation(fields[7].trim());
//                listing.setSource(fields[8].trim());
//
//                listingRepository.save(listing);
//            }
//
//            System.out.println("✅ Listings import completed!");
//        } catch (Exception e) {
//            System.err.println("❌ Failed to import CSV: " + e.getMessage());
//        }
//    }
//}
