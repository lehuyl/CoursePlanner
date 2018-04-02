package ca.courseplanner.model;

public class DataPoint {
    private int semesterCode;
    private int totalCoursesTaken;

    public DataPoint(int semesterCode){
        this.semesterCode = semesterCode;
        this.totalCoursesTaken = 0;
    }

    public void addToTotalCoursesTaken(int addition){
        totalCoursesTaken += addition;
    }

    public int getSemesterCode(){
        return semesterCode;
    }

    public int getTotalCoursesTaken(){
        return totalCoursesTaken;
    }
}
