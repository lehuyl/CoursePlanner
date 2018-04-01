package ca.courseplanner.controllers;

import ca.courseplanner.model.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class CoursePlannerController
{
    private ModelDumper modelDumper = new ModelDumper();

    @GetMapping("/about")
    public About getAbout(){
        return new About("appname", "miguel and steven");//TODO: error check
    }

    @GetMapping("/dump-model")
    public void dumpModelInfo(){//TODO: clean up later
        //get the scanner
        File file = new File("./data/course_data_2018.csv");//TODO: change later to 2018 upon submission
        try(Scanner scanner = new Scanner(file)){

            int lineNumber = 1;
            while(scanner.hasNextLine())
            {
                if(lineNumber == 1)
                {
                    scanner.nextLine();
                }
                else
                {
                    String[] newLine = scanner.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                    modelDumper.addNewRecord(newLine);
                }
                lineNumber++;
            }

            modelDumper.dumpToConsole();

        }catch(Exception e){
            System.out.println("something bad happened");//TODO: fix this later
        }
    }

    @GetMapping("/departments")
    public List<Department> getDepartmentList(){
        return modelDumper.getDepartmentList();
    }

    @GetMapping("/departments/{deptId}/courses")
    public List<Course> getDepartmentCourses(@PathVariable("deptId") long deptId){
        return modelDumper.getDepartmentWithID(deptId).getCourseList();
    }

    @GetMapping("/departments/{deptId}/courses/{courseId}/offerings")
    public List<Offering> getDepartmentCourseOfferings(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId){
        return modelDumper.getDepartmentWithID(deptId).getCourseWithID(courseId).getOfferingList();
    }

    @GetMapping("/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    public List<CourseComponent> getDepartmentCourseOfferingComponents(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId, @PathVariable("offeringId") long offeringId){
        return modelDumper.getDepartmentWithID(deptId).getCourseWithID(courseId).getOfferingWithID(offeringId).getCourseComponentList();
    }

    @GetMapping("/stats/students-per-semester?{deptId}")//TODO: error check, also switch out void later
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
        for(Course currentCourse : modelDumper.getDepartmentWithID(deptId).getCourseList()){
            for(Offering currentOffering : currentCourse.getOfferingList()){
                boolean semesterCodeAlreadyExists = false;
                for(DataPoint currentDataPoint : dataPointList){
                    if(currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()){
                        semesterCodeAlreadyExists = true;
                    }
                }

                if(!semesterCodeAlreadyExists){
                    dataPointList.add(new DataPoint(currentOffering.getSemesterCode()));
                }

                DataPoint appropriateDataPoint = null;
                for(DataPoint currentDataPoint : dataPointList){
                    if(currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()){
                        appropriateDataPoint = currentDataPoint;
                    }
                }

                for(CourseComponent currentCourseComponent : currentOffering.getCourseComponentList()){
                    appropriateDataPoint.addToTotalCoursesTaken(currentCourseComponent.getEnrollmentTotal());
                }
            }
        }

        return dataPointList;
    }

    @PostMapping("/addoffering")//TODO: error check, switch out void if needed
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
