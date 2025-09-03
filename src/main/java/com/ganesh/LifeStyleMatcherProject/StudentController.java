package com.ganesh.LifeStyleMatcherProject;

import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/{vid}")
    public Student getStudentByVid(@PathVariable int vid) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("University");
            MongoCollection<Document> collection = db.getCollection("students");

            Document doc = collection.find(new Document("VID", vid)).first();

            if (doc != null) {
                return new Student(doc);
            } else {
                throw new RuntimeException("Student not found");
            }
        }
    }
}
