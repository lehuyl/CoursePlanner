package ca.courseplanner.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ModelDumper implementation which sorts information about all the courses.
 */
public class ModelDumper {
    private List<Department> departmentList = new ArrayList<>();
    private AtomicLong nextDepartmentId = new AtomicLong();

    /**
     * ModelDumper constructor.
     */
    public ModelDumper(String filename) {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {

            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                if (lineNumber == 1) {
                    scanner.nextLine();
                } else {
                    String[] newLine = scanner.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                    addNewRecord(newLine);
                }
                lineNumber++;
            }

        } catch (Exception e) {
            System.out.println("something bad happened with scanning the file");
        }
    }

    /**
     * Gets the department list.
     *
     * @return List of Department objects.
     */
    public List<Department> getDepartmentList() {
        sortDepartmentsAlphabetical();
        return departmentList;
    }

    /**
     * Adds a new record into the system.
     *
     * @param record Must not be null. String array containing the information of the new course.
     */
    public void addNewRecord(String[] record) {
        int year;
        int semester;
        String subject;
        String catalogNumber;
        String location;
        String componentCode;
        int enrollmentNumber;
        int totalEnrollmentNumber;
        List<String> newInstructorList;

        //Takes the String array and puts it into the appropriate
        year = Integer.parseInt(record[0].substring(0, 3));
        semester = Integer.parseInt(record[0].substring(3));

        subject = record[1].trim();
        catalogNumber = record[2].trim();
        location = record[3].trim();
        totalEnrollmentNumber = Integer.parseInt(record[4]);
        enrollmentNumber = Integer.parseInt(record[5]);

        record[6] = record[6].replace("\"", "");
        record[6] = record[6].replace(", ", ",");
        newInstructorList = Arrays.asList(record[6].split(","));
        for (int i = 0; i < newInstructorList.size(); i++) {
            newInstructorList.set(i, newInstructorList.get(i).trim());
        }
        componentCode = record[7];

        for (Department currentDepartment : departmentList) {
            if (currentDepartment.isEqual(subject)) {
                currentDepartment.addCourseInfo(catalogNumber, year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
                return;
            }
        }
        addNewDepartmentListElement(subject);
        departmentList.get(departmentList.size() - 1).addCourseInfo(catalogNumber, year, semester, location, componentCode, enrollmentNumber, totalEnrollmentNumber, newInstructorList);
    }

    /**
     * Prints the model onto the console and also makes a text file with idential information.
     */
    public void dumpToConsole() {

        File logFile = new File("./data/output_dump.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {

            String intro = "Model Dump from 'course_data_2018.csv' file\n\n";
            writer.write(intro);
            System.out.print(intro);

            sortDepartmentsAlphabetical();
            for (Department currentDepartment : departmentList) {
                System.out.print(currentDepartment.getDepartmentInfo());
                writer.write(currentDepartment.getDepartmentInfo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new department list element into the department list.
     *
     * @param name Must not be null. String containing the name of the new department.
     */
    private void addNewDepartmentListElement(String name) {
        departmentList.add(new Department(nextDepartmentId.getAndIncrement(), name));
    }

    /**
     * Sorts the department objects into alphabetical ascending.
     */
    private void sortDepartmentsAlphabetical() {
        departmentList.sort(new Comparator<Department>() {
            @Override
            public int compare(Department o1, Department o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public Department getDepartmentWithID(long deptId) {
        for (Department currentDepartment : departmentList) {
            if (currentDepartment.getDeptId() == deptId) {
                return currentDepartment;
            }
        }
        return null;
    }
}
