package ca.courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Offering implementation which describes the properties of an offering of a course.
 */
public class Course {
    private long courseId;
    private String catalogNumber;

    private List<Offering> offeringList = new ArrayList<>();
    private AtomicLong nextOfferingId = new AtomicLong();

    /**
     * Constructor for the Course.
     * @param courseId Must not be null. Long containing the id of the course.
     * @param catalogNumber Must not be null. String containing the catalog number of the course.
     */
    public Course(long courseId, String catalogNumber){
        this.courseId = courseId;
        this.catalogNumber = catalogNumber;
    }

    /**
     * Gets the course Id of the course.
     * @return Long containing the ID of the course.
     */
    public long getCourseId(){
        return courseId;
    }

    /**
     * Gets the CatalogNumber of the Course.
     * @return String containing the CatalogNumber of the Course.
     */
    public String getCatalogNumber() {
        return catalogNumber;
    }

    /**
     * Adds Offering information about the course
     * @param year Must not be null. Integer containing the year code the Course is offered.
     * @param semester Must not be null. Integer containing the semester code the Course is offered.
     * @param location Must not be null. String containing the location the Course is offered.
     * @param componentCode Must not be null. String containing the componentCode the Course is offered.
     * @param enrollmentNumber Must not be null. Integer containing the amount of enrolled students in the offered Course.
     * @param totalEnrollmentNumber Must not be null. Integer containing the amount of total seats in the offered Course.
     * @param newInstructorList Must not be null. ArrayList containing the instructor's names in the offered Course.
     */
    public void addOfferingInfo(int year, int semester,
                                String location, String componentCode,
                                int enrollmentNumber, int totalEnrollmentNumber, List<String> newInstructorList){
        for(Offering currentOffering : offeringList){
            if(currentOffering.isEqual(year, semester, location)){
                currentOffering.addCourseComponentInfo(componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
                return;
            }
        }

        addNewOfferingListElement(year, semester, location);
        offeringList.get(offeringList.size() - 1).addCourseComponentInfo(componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
    }

    /**
     * Gets the information about the Course.
     * @return Returns a String containing the information about the Course.
     */
    @JsonIgnore
    public String getCourseInfo(){
        sortLocationAlphabetical();
        sortNumericalYearSem();
        StringBuilder stringBuilder = new StringBuilder(catalogNumber + "\n");
        for(Offering currentOffering : offeringList){
            stringBuilder.append(currentOffering.getOfferingInfo());
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if the current information matches the Course's information.
     * @param catalogNumber Must not be null. String containing the catalogNumber.
     * @return Boolean to see if the current information matches the Course's information.
     */
    @JsonIgnore
    public boolean isEqual(String catalogNumber){
        return catalogNumber.equals(this.catalogNumber);
    }

    /**
     * Adds a new Offering into the ArrayList.
     * @param year Must not be null. Int containing the year code of the course.
     * @param semester Must not be null. Int containing the semester code of the course.
     * @param location Must not be null. String containing the location of the course.
     */
    private void addNewOfferingListElement(int year, int semester, String location){
        offeringList.add(new Offering(nextOfferingId.incrementAndGet(), location, year, semester));
    }

    /**
     * Sorts the offering list in an ascending order using the offeringID given by SFU.
     */
    private void sortNumericalYearSem(){
        offeringList.sort(new Comparator<Offering>() {
            @Override
            public int compare(Offering o1, Offering o2){
                return o1.getOfferingId().compareTo(o2.getOfferingId());
            }
        });
    }

    /**
     * Sorts the offering list in ascending order using the location of the course.
     */
    private void sortLocationAlphabetical(){
        offeringList.sort(new Comparator<Offering>() {
            @Override
            public int compare(Offering o1, Offering o2) {
                return o1.getLocation().compareTo(o2.getLocation());
            }
        });
    }

    @JsonIgnore
    public List<Offering> getOfferingList(){
        return offeringList;
    }

    @JsonIgnore
    public Offering getOfferingWithID(long offeringId){
        for(Offering currentOffering : offeringList){
            if(currentOffering.getCourseOfferingId() == offeringId){
                return currentOffering;
            }
        }

        return null;
    }
}
