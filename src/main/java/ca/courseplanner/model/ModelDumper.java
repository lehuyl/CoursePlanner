package ca.courseplanner.model;

import java.util.*;

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
        record[6] = record[6].replace("\"", "");
        record[6] = record[6].replaceAll("\\s+","");
        newInstructorList = Arrays.asList(record[6].split(","));//TODO: error check
//        newInstructorList = Arrays.asList(record[6].split(",(?=([^\"]\"[^\"]\")[^\"]$)"));
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
        sortAlphabetical();
        for(Course currentCourse : courseList){
            sortLocationAlphabetical(currentCourse);
            sortNumericalYearSem(currentCourse);

            System.out.print(currentCourse.getCourseInfo());
        }
    }

    private void sortAlphabetical()
    {
        Collections.sort(courseList, new Comparator<Course>()
        {
            @Override
            public int compare(Course o1, Course o2)
            {
//                return o1.getSubject().compareToIgnoreCase(o2.getSubject());
                return (o1.getSubject() + " " + o1.getCatalogNumber()).compareTo(o2.getSubject() + " " + o2.getCatalogNumber());
            }
        });
    }

    private void sortNumericalYearSem(Course course)
    {
        Collections.sort(course.getOfferingList(), new Comparator<Offering>()
        {
            @Override
            public int compare(Offering o1, Offering o2)
            {
                return o1.getOfferingId().compareTo(o2.getOfferingId());
            }
        });
    }

    private void sortLocationAlphabetical(Course course)
    {
        Collections.sort(course.getOfferingList(), new Comparator<Offering>()
        {
            @Override
            public int compare(Offering o1, Offering o2)
            {
                return o1.getLocation().compareTo(o2.getLocation());
            }
        });
    }

    private void addNewCourseListElement(String subject, String catalogNumber){
        courseList.add(new Course(subject, catalogNumber));
    }
}
