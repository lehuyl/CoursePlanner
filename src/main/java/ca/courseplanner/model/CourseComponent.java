package ca.courseplanner.model;

/**
 * CourseComponent implementation which describes the properties of the particular component of a course.
 */
public class CourseComponent {
    private String componentCode;
    private int enrollmentNumber = 0;
    private int totalEnrollmentNumber = 0;

    /**
     * Constructor for the CourseComponent
     * @param componentCode Must not be null. String that contains the componentCode.
     */
    public CourseComponent(String componentCode){
        this.componentCode = componentCode;
    }

    /**
     * Updates the amount of people enrolled in the CourseComponent.
     * @param enrollmentAmount Must not be null. Int containing how many people to be updated into the enrollment number.
     */
    public void addEnrollment(int enrollmentAmount){
        enrollmentNumber += enrollmentAmount;
    }

    /**
     * Updates the amount of seats available in the CourseComponent.
     * @param seats Must not be null. Int containing how many seats to be updated into the total enrollment number.
     */
    public void addEnrollmentTotal(int seats){
        totalEnrollmentNumber += seats;
    }

    /**
     * Checks if the CourseComponent is equal to the String in question.
     * @param componentCode Must not be null. String contains the component code in question.
     * @return Boolean showing if the CourseComponent is euqla to the String in question.
     */
    public boolean isEqual(String componentCode){
        return this.componentCode.equals(componentCode);
    }
    //TODO: make sure to turn this into equals()

    /**
     * Returns the information about the courseComponent.
     * @return String containing the information about the courseComponent.
     */
    public String getCourseComponentInfo(){
        return "Type=" + componentCode + ", Enrollment=" + enrollmentNumber + "/" + totalEnrollmentNumber + "\n";
    }
}
