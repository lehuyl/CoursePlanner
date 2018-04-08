package ca.courseplanner.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for not finding the specific Course.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException
{
    public CourseNotFoundException(String message)
    {
        super(message);
    }
}
