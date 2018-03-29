package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Offering implementation which describes the properties of an offering of a course.
 */
public class Course {
    private String subject;
    private String catalogNumber;
    private List<Offering> offeringList = new ArrayList<>();

    /**
     * Constructor for the Course object.
     * @param subject Must not be null. String containing the subject of the course.
     * @param catalogNumber Must not be null. String containing the catalogNumber of the course.
     */
    public Course(String subject, String catalogNumber){
        this.subject = subject;
        this.catalogNumber = catalogNumber;
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
    public String getCourseInfo(){
        StringBuilder stringBuilder = new StringBuilder(subject + " " + catalogNumber + "\n");
        for(Offering currentOffering : offeringList){
            stringBuilder.append(currentOffering.getOfferingInfo());
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object otherObject){
        if(otherObject.getClass() == this.getClass()){
            Course otherCourse = (Course)otherObject;
            return otherCourse.getSubject().equals(this.getSubject()) && otherCourse.getCatalogNumber().equals(this.getCatalogNumber());
        }
        return false;
    }

    /**
     * Checks if the Course information matches the current object.
     * @param subject Must not be null. String containing the subject of the course.
     * @param catalogNumber Must not be null. String containing the catalogNumber of the course.
     * @return Boolean containing if the Course information matches the current object.
     */
    public boolean isEqual(String subject, String catalogNumber){
        return subject.equals(this.subject) && catalogNumber.equals(this.catalogNumber);
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    /**
     * Checks if the Offering already exists in the list.
     * @param year Must not be null. Int containing the year code of the course.
     * @param semester Must not be null. Int containing the semester code of the course.
     * @param location Must not be null. String containing the location of the course.
     * @return Boolean containing if the Offering already exists in the list.
     */
    private boolean doesOfferingExists(int year, int semester, String location){
        for(Offering currentOffering : offeringList){
            if(currentOffering.isEqual(year, semester, location)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new Offering into the ArrayList.
     * @param year Must not be null. Int containing the year code of the course.
     * @param semester Must not be null. Int containing the semester code of the course.
     * @param location Must not be null. String containing the location of the course.
     */
    private void addNewOfferingListElement(int year, int semester, String location){
        offeringList.add(new Offering(year, semester, location));
    }
}
