package ca.courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Offering implementation which describes the properties of an offering of a course.
 */
public class Offering {
    private long courseOfferingId;
    private String location;
    private List<String> instructorList = new ArrayList<>();
    private String instructors;
    private String term;
    private int semesterCode;
    private int year;

    private int yearCode;
    private List<CourseComponent> courseComponentList = new ArrayList<>();

    /**
     * Constructor for the Offering Object.
     *
     * @param courseOfferingId Must not be null. Long containing the ID given by the model.
     * @param location         Must not be null. String containing the location of the offering.
     * @param yearCode         Must not be null. Integer containing the year code of the offering.
     * @param semesterCode     Must not be null. Integer containing the semester code of the offering.
     */
    public Offering(long courseOfferingId, String location, int yearCode, int semesterCode) {
        this.courseOfferingId = courseOfferingId;
        this.location = location;
        this.yearCode = yearCode;
        this.year = getYear(yearCode);
        this.semesterCode = semesterCode;

        switch (semesterCode) {
            case 1:
                this.term = "SPRING";
                break;
            case 4:
                this.term = "SUMMER";
                break;
            case 7:
                this.term = "FALL";
                break;
            default:
                assert false;
        }
    }

    private int getYear(int yearCode) {
        return 1900 + yearCode;
    }

    /**
     * Gets the offering Id given by the model.
     *
     * @return Long containing the offering ID given by the model.
     */
    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    /**
     * Gets the Location of the Offering.
     *
     * @return String containing the location of the Offering.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the list of instructors in the offering.
     *
     * @return String containing the list of instructors in the offering.
     */
    public String getInstructors() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String currentInstructor : instructorList) {
            stringBuilder.append(currentInstructor);
            if (instructorList.indexOf(currentInstructor) != instructorList.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        instructors = stringBuilder.toString();
        return instructors;
    }

    /**
     * Gets the term for when the offering is offered.
     *
     * @return String containing when the offering is offered.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Gets the code given by SFU of the offering.
     *
     * @return Integer containing the code given by SFU of the offering.
     */
    public int getSemesterCode() {
        return yearCode * 10 + semesterCode;
    }

    /**
     * Gets the year for when this offering is offered.
     *
     * @return Integer containing the year for when this offering is offered.
     */
    public int getYear() {
        return year;
    }

    /**
     * Adds information into the courseComponent.
     *
     * @param componentCode         Must not be null. String containing the code of the courseComponent.
     * @param enrollmentNumber      Must not be null. Int containing how many more students are enrolled in the course.
     * @param totalEnrollmentNumber Must not be null. Into containing how many more seats are available in the course.
     */
    public void addCourseComponentInfo(String componentCode, int enrollmentNumber, int totalEnrollmentNumber, List<String> newInstructorList) {
        for (String newInstructor : newInstructorList) {
            boolean isAlreadyAdded = false;
            for (String instructor : instructorList) {
                if (instructor.equals(newInstructor)) {
                    isAlreadyAdded = true;
                    break;
                }
            }
            if (!isAlreadyAdded && (!newInstructor.equals("(null)") && !newInstructor.equals("<null>"))) {
                instructorList.add(newInstructor);
            }
        }

        for (CourseComponent currentCourseComponent : courseComponentList) {
            if (currentCourseComponent.isEqual(componentCode)) {
                //enrollmentNumber is how many people enrolled
                //totalEnrollmentNumber is the capacity of that
//                currentCourseComponent.addEnrollment(enrollmentNumber);
//                currentCourseComponent.addEnrollmentTotal(totalEnrollmentNumber);

                currentCourseComponent.addEnrollmentTotal(enrollmentNumber);
                currentCourseComponent.addEnrollmentCap(totalEnrollmentNumber);
                return;
            }
        }

        addNewCourseComponentListElement(componentCode);
//        courseComponentList.get(courseComponentList.size() - 1).addEnrollment(enrollmentNumber);
//        courseComponentList.get(courseComponentList.size() - 1).addEnrollmentTotal(totalEnrollmentNumber);
        courseComponentList.get(courseComponentList.size() - 1).addEnrollmentTotal(enrollmentNumber);
        courseComponentList.get(courseComponentList.size() - 1).addEnrollmentCap(totalEnrollmentNumber);
    }

    /**
     * Checks if the information matches the current Offering object.
     *
     * @param yearCode     Must not be null. Integer containing the year code of the offering.
     * @param semesterCode Must not be null. Integer containing the semester code of the offering.
     * @param location     Must not be null. String containing the location of the offering.
     * @return Boolean containing if the information matches the current Offering Object.
     */
    @JsonIgnore
    public boolean isEqual(int yearCode, int semesterCode, String location) {
        return this.yearCode == yearCode && this.semesterCode == semesterCode && this.location.equals(location);
    }

    /**
     * Returns the information about the Offering.
     *
     * @return String containing the information about the Offering.
     */
    @JsonIgnore
    public String getOfferingInfo() {
        StringBuilder stringBuilder = new StringBuilder("\t" + yearCode + semesterCode + " in " + location + " by ");

        Set<String> instructorDuplicateSet = new HashSet<>();
        for (String currentInstructor : instructorList) {
            if (!instructorDuplicateSet.contains(currentInstructor)) {
                stringBuilder.append(currentInstructor);
                if (instructorList.indexOf(currentInstructor) != instructorList.size() - 1) {
                    stringBuilder.append(", ");
                }
                instructorDuplicateSet.add(currentInstructor);
            }
        }
        stringBuilder.append("\n");

        sortComponentAlphabetical();

        for (CourseComponent currentCourseComponent : courseComponentList) {
            stringBuilder.append("\t\t");
            stringBuilder.append(currentCourseComponent.getCourseComponentInfo());
        }
        return stringBuilder.toString();
    }

    /**
     * Adds a new CourseComponent into the ArrayList.
     *
     * @param componentCode Must not be null. String that contains the componentCode for the new CourseComponent.
     */
    private void addNewCourseComponentListElement(String componentCode) {
        courseComponentList.add(new CourseComponent(componentCode));
    }

    /**
     * Gets the OfferingId of the Offering.
     *
     * @return String containing the OfferingId of the Offering.
     */
    @JsonIgnore
    public String getOfferingId() {
        return "" + yearCode + semesterCode;
    }

    /**
     * Sorts the CourseComponent list alphabetically.
     */
    private void sortComponentAlphabetical() {
        courseComponentList.sort(new Comparator<CourseComponent>() {
            @Override
            public int compare(CourseComponent component1, CourseComponent component2) {
                return component1.getType().compareTo(component2.getType());
            }
        });
    }

    @JsonIgnore
    public List<CourseComponent> getCourseComponentList() {
        sortComponentAlphabetical();
        return courseComponentList;
    }
}
