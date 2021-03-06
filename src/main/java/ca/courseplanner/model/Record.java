package ca.courseplanner.model;

/**
 * Record implementation which contains all new information about an offering.
 */
public class Record {
    private int semester;
    private String subjectName;
    private String catalogNumber;
    private String location;
    private int enrollmentCap;
    private String component;
    private int enrollmentTotal;
    private String instructor;

    public Record(){

    }

    public int getSemester(){
        return semester;
    }

    public String getSubjectName(){
        return subjectName;
    }

    public String getCatalogNumber(){
        return catalogNumber;
    }

    public String getLocation(){
        return location;
    }

    public int getEnrollmentCap(){
        return enrollmentCap;
    }

    public String getComponent(){
        return component;
    }

    public int getEnrollmentTotal(){
        return enrollmentTotal;
    }

    public String getInstructor(){
        return instructor;
    }


}
