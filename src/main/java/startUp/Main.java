package startUp;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    @Parameter
    private List<String> mainFile = new ArrayList<>();

    public static void main(String[] args){
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.start();
    }


    private void start(){
        try {
            File file = new File(mainFile.get(0));
            Compiler c = Compiler.createNew(new FileReader(file), 10, System.out);
            c.compile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
