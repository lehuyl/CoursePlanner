package ca.courseplanner.model;

/**
 * CourseComponent implementation which describes the properties of the particular component of a course.
 */
public class CourseComponent {
    private String componentCode;
    private int enrollmentNumber = 0;
    private int totalEnrollmentNumber = 0;

    public CourseComponent(String componentCode){
        this.componentCode = componentCode;
    }

    public void addEnrollment(int enrollmentAmount){
        enrollmentNumber += enrollmentAmount;
    }

    public void addEnrollmentTotal(int seats){
        totalEnrollmentNumber += seats;
    }

    public boolean isEqual(String componentCode){
        return this.componentCode.equals(componentCode);
    }

    public String printCourseComponentInfo(){
        return "Type=" + componentCode + ", Enrollment=" + enrollmentNumber + "/" + totalEnrollmentNumber + "\n";
    }
}
