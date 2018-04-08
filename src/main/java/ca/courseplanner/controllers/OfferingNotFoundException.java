package ca.courseplanner.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfferingNotFoundException extends RuntimeException
{
    public OfferingNotFoundException(String message)
    {
        super(message);
    }
}
