package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ModelDumper implementation which sorts information about all the courses.
 */
public class ModelDumper {
    private List<Course> courseList = new ArrayList<>();

    public ModelDumper(){

    }

    public void addNewRecord(String[] record){
        int year;
        int semester;
        String subject;
        String catalogNumber;
        String location;
        String componentCode;
        int enrollmentNumber;
        int totalEnrollmentNumber;
        List<String> newInstructorList;

        //get the first element of the record, use the first three char for the year, turn that to int, last one is for semester, turn that to int
        year = Integer.parseInt(record[0].substring(0, 3));//TODO: error check
        semester = Integer.parseInt(record[0].substring(3));//TODO: error check

        //get the second element, use that for subject
        subject = record[1];
        catalogNumber = record[2];
        location = record[3];
        totalEnrollmentNumber = Integer.parseInt(record[4]);
        enrollmentNumber = Integer.parseInt(record[5]);

        //for record[6] we will have to make sure that we separate these into comma separated then add those into the newInstructorList
        newInstructorList = Arrays.asList(record[6].split(","));//TODO: error check

        componentCode = record[7];

        for(Course currentCourse : courseList){
            if(currentCourse.isEqual(subject, catalogNumber)){
                currentCourse.addOfferingInfo(year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
                return;
            }
        }

        addNewCourseListElement(subject, catalogNumber);
        courseList.get(courseList.size() - 1).addOfferingInfo(year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);


    }

    public void dumpToConsole(){
        for(Course currentCourse : courseList){
            System.out.print(currentCourse.getCourseInfo());
        }
    }

    private void addNewCourseListElement(String subject, String catalogNumber){
        courseList.add(new Course(subject, catalogNumber));
    }
}
