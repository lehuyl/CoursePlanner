package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a record of changes made to a course and its offerings.
 */
public class Watcher implements Observer {
    private long id;
    private Department department;
    private Course course;
    private List<String> events = new ArrayList<>();

    public Watcher(long id, Department department, Course course) {
        this.id = id;
        this.department = department;
        this.course = course;
        this.course.addWatcher(this);
    }

    public long getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public Course getCourse() {
        return course;
    }

    public List<String> getEvents() {
        return events;
    }

    @Override
    public void update() {
        events.add(course.getLatestAddition());
    }
}
