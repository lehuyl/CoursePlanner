package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Offering implementation which describes the properties of an offering of a course.
 */
public class Offering {
    private int year;
    private int semester;
    private String location;
    private List<String> instructorList = new ArrayList<>();
    private List<CourseComponent> courseComponentList = new ArrayList<>();

    /**
     * Constructor for the Offering.
     * @param year Must not be null. Int containing the year code of the course.
     * @param semester Must not be null. Int containing the semester code of the course.
     * @param location Must not be null. String containing the location of the course.
     */
    public Offering(int year, int semester, String location){
        this.year = year;
        this.semester = semester;
        this.location = location;//TODO: make this all caps?
    }

    /**
     * Adds information into the courseComponent.
     * @param componentCode Must not be null. String containing the code of the courseComponent.
     * @param enrollmentNumber Must not be null. Int containing how many more students are enrolled in the course.
     * @param totalEnrollmentNumber Must not be null. Into containing how many more seats are available in the course.
     */
    public void addCourseComponentInfo(String componentCode, int enrollmentNumber, int totalEnrollmentNumber, List<String> newInstructorList){
        instructorList.addAll(newInstructorList);

        for(CourseComponent currentCourseComponent : courseComponentList){
            if(currentCourseComponent.isEqual(componentCode)){
                currentCourseComponent.addEnrollment(enrollmentNumber);
                currentCourseComponent.addEnrollmentTotal(totalEnrollmentNumber);
                return;
            }
        }

        addNewCourseComponentListElement(componentCode);
        courseComponentList.get(courseComponentList.size() - 1).addEnrollment(enrollmentNumber);
        courseComponentList.get(courseComponentList.size() - 1).addEnrollmentTotal(totalEnrollmentNumber);
    }

    /**
     * Checks if the Offering information matches the current object.
     * @param year Must not be null. Int contains the year code of the course.
     * @param semester Must not be null. Int contains the semester code of the course.
     * @param location Must not be null. String contains the location of the course.
     * @return Boolean showing if the information matches the current object.
     */
    public boolean isEqual(int year, int semester, String location){
        return this.year == year && this.semester == semester && this.location.equals(location);
    }
    //TODO: turn this to equals()?

    /**
     * Returns the information about the Offering.
     * @return String containing the information about the Offering.
     */
    public String getOfferingInfo(){
        StringBuilder stringBuilder = new StringBuilder("\t" + year + semester + " in " + location + " by ");

        Set<String> instructorDuplicateSet = new HashSet<>();
        for(String currentInstructor : instructorList){
            if(!instructorDuplicateSet.contains(currentInstructor))
            {
                stringBuilder.append(currentInstructor);
                if(instructorList.indexOf(currentInstructor) != instructorList.size() - 1){
                    stringBuilder.append(", ");
                }
                instructorDuplicateSet.add(currentInstructor);
            }
        }
        stringBuilder.append("\n");

        for(CourseComponent currentCourseComponent : courseComponentList){
            stringBuilder.append("\t\t");
            stringBuilder.append(currentCourseComponent.getCourseComponentInfo());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if the CourseComponent already exists in the list.
     * @param componentCode Must not be null. String that contains the componentCode of the CourseComponent.
     * @return Boolean showing if the courseComponent already exists in the list.
     */
    private boolean doesCourseComponentInfoExist(String componentCode){
        for(CourseComponent currentCourseComponent : courseComponentList){
            if(currentCourseComponent.isEqual(componentCode)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new CourseComponent into the ArrayList.
     * @param componentCode Must not be null. String that contains the componentCode for the new CourseComponent.
     */
    private void addNewCourseComponentListElement(String componentCode){
        courseComponentList.add(new CourseComponent(componentCode));
    }

    public String getOfferingId()
    {
        return "" + year + semester;
    }

    public String getLocation()
    {
        return location;
    }
}
