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
    public void dumpModelInfo(){
        //get the scanner
        File file = new File("./data/course_data_2018.csv");
        try(Scanner scanner = new Scanner(file)){
//            while(scanner.hasNext()){
//                System.out.println(scanner.nextLine() + " | ");
//            }
            int lineNumber = 1;
            while(scanner.hasNextLine())
            {
//                System.out.println(scanner.nextLine());
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

        //add the stuff

        //when youre done, go ahead and dump the stuff
    }

}
