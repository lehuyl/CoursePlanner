package ca.courseplanner.model;

import java.util.ArrayList;
import java.util.List;

//TODO: javadocs
public class Watcher implements Observer{
    private long id;
    private Department department;
    private Course course;
    private List<String> events = new ArrayList<>();

    public Watcher(long id, Department department, Course course){
        this.id = id;
        this.department = department;
        this.course = course;
        this.course.addWatcher(this);
    }

    public long getId(){
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

    //i think this is supposed to be called inside offering or something????
    @Override
    public void update() {
        //here we want to update the events list
        events.add(course.getLatestAddition());
    }
}


//subject has a list of observers
//subject notifies all of its observers..
//an observer takes in its subject as a parameter for the constructor... an observer remembers its subject
//an observer just does stuff with the subject it remembers?
