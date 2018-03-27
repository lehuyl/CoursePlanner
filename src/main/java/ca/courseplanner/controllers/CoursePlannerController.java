package ca.courseplanner.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoursePlannerController
{
    @GetMapping("/hello")
    public String getHelloMessage()
    {
        return "Hello World";
    }


}
