package ca.courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * CourseComponent implementation which describes the properties of the particular component of a course.
 */
public class CourseComponent {
    private String type;
    private int enrollmentCap = 0;
    private int enrollmentTotal = 0;

    /**
     * Constructor for the CourseComponent
     *
     * @param componentCode Must not be null. String that contains the componentCode.
     */
    public CourseComponent(String componentCode) {
        this.type = componentCode;
    }

    /**
     * Returns the componentCode of the CourseComponent.
     *
     * @return String containing the componentCode of the CourseComponent.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the enrollment cap of the component.
     *
     * @return Integer containing the enrollment cap of the component.
     */
    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    /**
     * Gets the enrollment total of the component.
     *
     * @return Integer containing the enrollment cap of the component.
     */
    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void addEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal += enrollmentTotal;
    }

    public void addEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap += enrollmentCap;
    }

    /**
     * Checks if the CourseComponent is equal to the String in question.
     *
     * @param componentCode Must not be null. String contains the component code in question.
     * @return Boolean showing if the CourseComponent is equal to the String in question.
     */
    @JsonIgnore
    public boolean isEqual(String componentCode) {
        return this.type.equals(componentCode);
    }

    /**
     * Returns the information about the courseComponent.
     *
     * @return String containing the information about the CourseComponent.
     */
    @JsonIgnore
    public String getCourseComponentInfo() {
        return "Type=" + type + ", Enrollment=" + enrollmentCap + "/" + enrollmentTotal + "\n";
    }
}
