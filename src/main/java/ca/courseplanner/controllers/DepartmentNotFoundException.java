package ca.courseplanner.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for not finding the specific Department.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DepartmentNotFoundException extends RuntimeException
{
    public DepartmentNotFoundException(String message)
    {
        super(message);
    }
}
