package codeGeneration;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class PrettyFormatter implements Formatter {


    @Override
    public void writeCode(List<Map<String, Integer>> code, OutputStream out) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        int i = 1;
        for(Map<String,Integer> line : code) {
            writer.write(i + "\t");
            boolean first = true;
            for (String s : line.keySet()) {
                if (first) {
                    first = false;
                } else {
                    writer.write(", ");
                }
                writer.write(s + " " + line.get(s) );
            }
            writer.write("\n");
            i++;
        }
        writer.flush();
    }
}
