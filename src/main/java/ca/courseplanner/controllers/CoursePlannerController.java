package ca.courseplanner.controllers;

import ca.courseplanner.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
/**
 * Controller for the application.
 */
public class CoursePlannerController {
    private ModelDumper modelDumper = new ModelDumper("./data/course_data_2018.csv");
    private List<Watcher> watcherList = new ArrayList<>();
    private AtomicLong nextWatcherId = new AtomicLong();

    @GetMapping("/api/about")
    public About getAbout() {
        return new About("The Course Planner", "Miguel Taningco and Steven Le");
    }

    @GetMapping("/api/dump-model")
    public void dumpModelInfo() {
        modelDumper.dumpToConsole();
    }

    @GetMapping("/api/departments")
    public List<Department> getDepartmentList() {
        return modelDumper.getDepartmentList();
    }

    @GetMapping("/api/departments/{deptId}/courses")
    public List<Course> getDepartmentCourses(@PathVariable("deptId") long deptId) {
        for (Department dept : modelDumper.getDepartmentList()) {
            if (dept.getDeptId() == deptId) {
                return dept.getCourseList();
            }
        }
        throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    public List<Offering> getDepartmentCourseOfferings(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId) {
        for (Department dept : modelDumper.getDepartmentList()) {
            if (dept.getDeptId() == deptId) {
                if (dept.getCourseWithID(courseId) != null) {
                    return dept.getCourseWithID(courseId).getOfferingList();
                } else {
                    throw new CourseNotFoundException("Course of ID " + courseId + " not found.");

                }
            }
        }
        throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
    }


    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")

    public List<CourseComponent> getDepartmentCourseOfferingComponents(@PathVariable("deptId") long deptId, @PathVariable("courseId") long courseId, @PathVariable("offeringId") long offeringId) {
        if (modelDumper.getDepartmentWithID(deptId) != null) {
            Department dept = modelDumper.getDepartmentWithID(deptId);
            if (dept.getCourseWithID(courseId) != null) {
                Course course = dept.getCourseWithID(courseId);
                if (course.getOfferingWithID(offeringId) != null) {
                    return course.getOfferingWithID(offeringId).getCourseComponentList();
                } else {
                    throw new OfferingNotFoundException(("Course offering of ID " + offeringId + " not found."));
                }
            } else {
                throw new CourseNotFoundException("Course of ID " + courseId + " not found.");
            }
        } else {
            throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
        }
    }

    @GetMapping("/api/stats/students-per-semester")
    public List<DataPoint> getDataPointList(@RequestParam("deptId") long deptId) {
        if (modelDumper.getDepartmentWithID(deptId) == null) {
            throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
        } else {
            List<DataPoint> dataPointList = new ArrayList<>();
            for (Course currentCourse : modelDumper.getDepartmentWithID(deptId).getCourseList()) {
                for (Offering currentOffering : currentCourse.getOfferingList()) {

                    boolean semesterCodeAlreadyExists = false;
                    for (DataPoint currentDataPoint : dataPointList) {
                        if (currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()) {
                            semesterCodeAlreadyExists = true;
                            break;
                        }
                    }

                    if (!semesterCodeAlreadyExists) {
                        dataPointList.add(new DataPoint(currentOffering.getSemesterCode()));
                    }

                    DataPoint appropriateDataPoint = null;
                    for (DataPoint currentDataPoint : dataPointList) {
                        if (currentDataPoint.getSemesterCode() == currentOffering.getSemesterCode()) {
                            appropriateDataPoint = currentDataPoint;
                            break;
                        }
                    }

                    if (appropriateDataPoint != null) {
                        for (CourseComponent currentCourseComponent : currentOffering.getCourseComponentList()) {
                            if (currentCourseComponent.getType().equals("LEC")) {
                                appropriateDataPoint.addToTotalCoursesTaken(currentCourseComponent.getEnrollmentTotal());
                            }
                        }
                    }
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
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/addoffering")
    public CourseComponent addNewOffering(@RequestBody Record record) {
        String[] stringArray = {"" + record.getSemester(),
                record.getSubjectName(),
                record.getCatalogNumber(),
                record.getLocation(),
                "" + record.getEnrollmentTotal(),
                "" + record.getEnrollmentCap(),
                record.getInstructor(),
                record.getComponent()};
        modelDumper.addNewRecord(stringArray);

        CourseComponent courseComponent = new CourseComponent(record.getComponent());
        courseComponent.addEnrollmentTotal(record.getEnrollmentTotal());
        courseComponent.addEnrollmentCap(record.getEnrollmentCap());
        return courseComponent;
    }

    @GetMapping("/api/watchers")
    public List<Watcher> getChangeWatchers() {
        return watcherList;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/watchers")
    public Watcher createWatcher(@RequestBody WatcherContents watcherContents) {
        long deptId = watcherContents.getDeptId();
        long courseId = watcherContents.getCourseId();
        if (modelDumper.getDepartmentWithID(deptId) != null) {
            Department department = null;
            for (Department currentDepartment : modelDumper.getDepartmentList()) {
                if (currentDepartment.getDeptId() == deptId) {
                    department = currentDepartment;
                }
            }

            if (department.getCourseWithID(courseId) != null) {
                Course course = null;
                for (Course currentCourse : department.getCourseList()) {
                    if (currentCourse.getCourseId() == courseId) {
                        course = currentCourse;
                    }
                }

                Watcher watcher = new Watcher(nextWatcherId.incrementAndGet(), department, course);
                watcherList.add(watcher);
                return watcher;
            } else {
                throw new CourseNotFoundException("Course of ID " + courseId + " not found.");
            }
        }
        throw new DepartmentNotFoundException("Department of ID " + deptId + " not found.");
    }

    @GetMapping("/api/watchers/{watcherId}")
    public Watcher getEventListOfWatcherId(@PathVariable("watcherId") long watcherId) {
        for (Watcher currentWatcher : watcherList) {
            if (currentWatcher.getId() == watcherId) {
                return currentWatcher;
            }
        }
        throw new ResourceNotFoundException("Unable to find requested watcher.");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/watchers/{watcherId}")
    public void deleteWatcherWithId(@PathVariable("watcherId") long watcherId) {
        for (ListIterator<Watcher> iterator = watcherList.listIterator(); iterator.hasNext(); ) {
            Watcher watcher = iterator.next();
            if (watcher.getId() == watcherId) {
                iterator.remove();
                return;
            }
        }

        throw new ResourceNotFoundException("Unable to find requested watcher.");
    }
}
