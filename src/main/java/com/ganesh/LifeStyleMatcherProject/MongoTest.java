package com.ganesh.LifeStyleMatcherProject;

import com.mongodb.client.*;
import org.bson.Document;

public class MongoTest {
    public static void main(String[] args) {
        // Connect to MongoDB at localhost:27017
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("University");
            MongoCollection<Document> collection = db.getCollection("students");

            // Fetch data
            for (Document doc : collection.find()) {
                System.out.println(doc.toJson());
            }
        }
    }
}

