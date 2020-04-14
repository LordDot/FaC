package codeGeneration;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface Formatter {
    void writeCode(List<Map<String,Integer>> code, OutputStream out) throws IOException;
}
