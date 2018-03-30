package ca.courseplanner.controllers;

import ca.courseplanner.model.ModelDumper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Scanner;

@RestController
public class CoursePlannerController
{
    ModelDumper modelDumper = new ModelDumper();

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

}
