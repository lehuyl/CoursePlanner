package ca.courseplanner.controllers;

import ca.courseplanner.model.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@RestController
public class CoursePlannerController
{
    private ModelDumper modelDumper = new ModelDumper("./data/course_data_2018.csv");

    @GetMapping("/api/about")
    public About getAbout(){
        return new About("appname", "miguel and steven");//TODO: error check
    }

    @GetMapping("/api/dump-model")
    public void dumpModelInfo(){//TODO: clean up later
        //get the scanner
//        File file = new File("./data/course_data_2018.csv");//TODO: change later to 2018 upon submission
//        try(Scanner scanner = new Scanner(file)){
//
//            int lineNumber = 1;
//            while(scanner.hasNextLine())
//            {
//                if(lineNumber == 1)
//                {
//                    scanner.nextLine();
//                }
//                else
//                {
//                    String[] newLine = scanner.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                    modelDumper.addNewRecord(newLine);
//                }
//                lineNumber++;
//            }
//
//            modelDumper.dumpToConsole();
//
//        }catch(Exception e){
//            System.out.println("something bad happened");//TODO: fix this later
//        }
        modelDumper.dumpToConsole();
    }

    @GetMapping("/api/departments")
    public List<Department> getDepartmentList(){
//        File file = new File("./data/course_data_2018.csv");//TODO: change later to 2018 upon submission
//        try(Scanner scanner = new Scanner(file)){
//
//            int lineNumber = 1;
//            while(scanner.hasNextLine())
//            {
//                if(lineNumber == 1)
//                {
//                    scanner.nextLine();
//                }
//                else
//                {
//                    String[] newLine = scanner.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//
//                    modelDumper.addNewRecord(newLine);
//                }
//                lineNumber++;
//            }
//
////            modelDumper.dumpToConsole();
////            return modelDumper.getDepartmentList();
//        }catch(Exception e){
//            System.out.println("something bad happened");//TODO: fix this later
//        }

        return modelDumper.getDepartmentList();
    }

    @GetMapping("/api/departments/{deptId}/courses")
    public List<Course> getDepartmentCourses(@PathVariable("deptId") long deptId){
        for(Department dept: modelDumper.getDepartmentList())
        {
            if(dept.getDeptId() == deptId)
            {
//                return modelDumper.getDepartmentWithID(deptId).getCourseList();
                return dept.getCourseList();
            }
        }
        throw new IllegalArgumentException("Department of ID " + deptId + " not found.");
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public List<Offering> getDepartmentCourseOfferings(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId){
//        return modelDumper.getDepartmentWithID(deptId).getCourseWithID(courseId).getOfferingList();

        for(Department dept: modelDumper.getDepartmentList())
        {
            if(dept.getDeptId() == deptId)
            {
                if(dept.getCourseWithID(courseId) != null)
                {
                    return dept.getCourseWithID(courseId).getOfferingList();
                }
                else
                {
                    throw new IllegalArgumentException("Course of ID " + courseId + " not found.");

                }
//                return dept.getCourseList();
            }
        }
        throw new IllegalArgumentException("Department of ID " + deptId + " not found.");
    }


    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
//    public List<CourseComponent> getDepartmentCourseOfferingComponents(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId, @PathVariable("offeringId") long offeringId){
    public List<CourseComponent> getDepartmentCourseOfferingComponents(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId, @PathVariable("offeringId") long offeringId){
        if(modelDumper.getDepartmentWithID(deptId) != null)
        {
            Department dept = modelDumper.getDepartmentWithID(deptId);
            if(dept.getCourseWithID(courseId) != null)
            {
                Course course = dept.getCourseWithID(courseId);
                if(course.getOfferingWithID(offeringId) != null)
                {
                    return course.getOfferingWithID(offeringId).getCourseComponentList();
                }
                else
                {
                    throw new IllegalArgumentException(("Course offering of ID " + offeringId + " not found."));
                }
            }
            else
            {
                throw new IllegalArgumentException("Course of ID " + courseId + " not found.");
            }
        }
        else
        {
            throw new IllegalArgumentException("Department of ID " + deptId + " not found.");
        }
    }

    @GetMapping("/api/stats/students-per-semester")//TODO: error check, also switch out void later
    public List<DataPoint> getDataPointList(@RequestParam("deptId") long deptId){
        //TODO: i think this should have a DataPoint object, a list of them to be exact
        //TODO: error check heavily

        /*
        * go to the department id, go inside each course inside each offering and add up
        * */

        //go through each offering in each course in the department
            //check if this offering semesterCode exists in the dataPoints
            //if not, make a dataPoint with that semesterCode
            //else keep going as if nothing happened

            //go inside the offering for its componentList and add that into the specific dataPoint
        List<DataPoint> dataPointList = new ArrayList<>();
        System.out.println("the department name is " + modelDumper.getDepartmentWithID(deptId).getName());
        for(Course currentCourse : modelDumper.getDepartmentWithID(deptId).getCourseList()){
            System.out.println("the course name is " + modelDumper.getDepartmentWithID(deptId).getName() + " " + currentCourse.getCatalogNumber());
            System.out.println("about to get every offering in this course...");
            for(Offering currentOffering : currentCourse.getOfferingList()){

                boolean semesterCodeAlreadyExists = false;
                for(DataPoint currentDataPoint : dataPointList){
                    System.out.println("Existence: data point semester code: " + currentDataPoint.getSemesterCode() + " | offering semester code: " + currentOffering.getSemesterCode());
                    if(currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()){
                        semesterCodeAlreadyExists = true;
                        System.out.println("found that the appropriate semester code exists, move on... ");
                        break;
                    }
                }

                if(!semesterCodeAlreadyExists){
                    System.out.println("semester code does not exist yet, adding a new data point into list... ");
                    dataPointList.add(new DataPoint(currentOffering.getSemesterCode()));
                }

                DataPoint appropriateDataPoint = null;
                System.out.println("find datapoint and keep track...");
                for(DataPoint currentDataPoint : dataPointList){
                    System.out.println("Keep: data point semester code: " + currentDataPoint.getSemesterCode() + " | offering semester code: " + currentOffering.getSemesterCode());
                    if(currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()){
                        System.out.println("found the data point, will keep track of it now... ");
                        appropriateDataPoint = currentDataPoint;
                        break;
                    }
                }

                if(appropriateDataPoint == null){
                    System.out.println("the appropriate datapoint is still null");
                }
                else{
                    for(CourseComponent currentCourseComponent : currentOffering.getCourseComponentList()){
                        if(currentCourseComponent.getType().equals("LEC")){
                            appropriateDataPoint.addToTotalCoursesTaken(currentCourseComponent.getEnrollmentTotal());
                            System.out.println("appropriate data sem code: " + appropriateDataPoint.getSemesterCode() + " | adding " + currentCourseComponent.getEnrollmentTotal() + " amount");
                        }
                    }
                }
                System.out.println("finished looking through all this offering");
            }
        }

        dataPointList.sort(new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint o1, DataPoint o2) {
                return o1.getSemesterCode() - o2.getSemesterCode();
            }
        });
        return dataPointList;
    }

    @PostMapping("/api/addoffering")//TODO: error check, switch out void if needed
    public void addNewOffering(@RequestBody Record record){
        String[] stringArray = {"" + record.getSemester(),
            record.getSubjectName(),
            record.getCatalogNumber(),
            record.getLocation(),
            "" + record.getEnrollmentTotal(),
            "" + record.getEnrollmentCap(),
            record.getInstructor(),
            record.getComponent()};
        modelDumper.addNewRecord(stringArray);
    }
}
