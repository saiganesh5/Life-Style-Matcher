package com.ganesh.LifeStyleMatcherProject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebpageController {
    @RequestMapping("/webpage")
    public String greet(){
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Life Style Matcher</title>
                </head>
                <body>
                    <h1>Life Style Matcher</h1>
                    <p style="color: green;">Life style matcher is running in Backend</p>
                </body>
                </html>
                
                """;
    }
}
