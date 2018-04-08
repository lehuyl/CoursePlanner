package ca.courseplanner.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for not finding the specific Offering.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfferingNotFoundException extends RuntimeException
{
    public OfferingNotFoundException(String message)
    {
        super(message);
    }
}
