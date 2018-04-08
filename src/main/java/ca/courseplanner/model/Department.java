package ca.courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Department implementation that describes the different departments of use university.
 */
public class Department
{
    private long deptId;
    private String name;

    private List<Course> courseList = new ArrayList<>();
    private AtomicLong nextCourseId = new AtomicLong();

    /**
     * Contructor for the Department object.
     *
     * @param deptId Must not be null. Long containing the id of the department.
     * @param name   Must not be null. String containing the name of the department.
     */
    public Department(long deptId, String name)
    {
        this.deptId = deptId;
        this.name = name;
    }

    /**
     * Get the id of the department.
     *
     * @return Integer containing the id of the department.
     */
    public long getDeptId()
    {
        return deptId;
    }

    /**
     * Get the name of the department.
     *
     * @return String containing the name of the department.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Adds a new course into its department.
     *
     * @param catalogNumber         Must not be null. String containing the catalog number of the course.
     * @param year                  Must not be null. Integer containing the year code of the course.
     * @param semester              Must not be null. Integer containing the semester code of the course.
     * @param location              Must not be null. String containing the location of the course.
     * @param componentCode         Must not be null. String containing the component code of the course.
     * @param enrollmentNumber      Must not be null. Integer containing number of students enrolled in the course.
     * @param totalEnrollmentNumber Must not be null. Integer containing the number of seats in the course.
     * @param newInstructorList     Must not be null. List of Strings containing the instructors in the course.
     */
    public void addCourseInfo(String catalogNumber, int year, int semester,
                              String location, String componentCode,
                              int enrollmentNumber, int totalEnrollmentNumber, List<String> newInstructorList)
    {
        for (Course currentCourse : courseList)
        {
            if (currentCourse.isEqual(catalogNumber))
            {
                currentCourse.addOfferingInfo(year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
                return;
            }
        }

        addNewCourseListElement(catalogNumber);
        courseList.get(courseList.size() - 1).addOfferingInfo(year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);


    }

    /**
     * Gets all the information about the department.
     *
     * @return String containing all the information about the department.
     */
    @JsonIgnore
    public String getDepartmentInfo()
    {
        StringBuilder stringBuilder = new StringBuilder();

        sortCoursesAscending();
        for (Course currentCourse : courseList)
        {
            stringBuilder.append(name);
            stringBuilder.append(" ");
            stringBuilder.append(currentCourse.getCourseInfo());
        }
        return stringBuilder.toString();
    }

    /**
     * Checks if the current information is the same as the object.
     *
     * @param name Must not be null. String containing the name of the department.
     * @return Boolean showing if the current information is the same as the object.
     */
    @JsonIgnore
    public boolean isEqual(String name)
    {
        return this.name.equals(name);
    }

    /**
     * Adds a new Course into the Course List.
     *
     * @param catalogNumber Must not be null. String containing the catalog number of the course.
     */
    private void addNewCourseListElement(String catalogNumber)
    {
        courseList.add(new Course(nextCourseId.incrementAndGet(), catalogNumber));
    }

    /**
     * Sorts the course list in ascending order according to the catalog number.
     */
    private void sortCoursesAscending()
    {
        courseList.sort(new Comparator<Course>()
        {
            @Override
            public int compare(Course o1, Course o2)
            {
                return o1.getCatalogNumber().compareTo(o2.getCatalogNumber());
            }
        });
    }

    @JsonIgnore
    public List<Course> getCourseList()
    {
        sortCoursesAscending();
        return courseList;
    }

    @JsonIgnore
    public Course getCourseWithID(long courseId)
    {
        for (Course currentCourse : courseList)
        {
            if (currentCourse.getCourseId() == courseId)
            {
                return currentCourse;
            }
        }

        return null;
    }
}
