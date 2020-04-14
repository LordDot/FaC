package startUp;

import codeGeneration.AssemblyGenerator;
import codeGeneration.FacAssemblyGenerator;
import codeGeneration.Formatter;
import codeGeneration.PrettyFormatter;
import parser.Parser;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

public class Compiler {
    private Tokenizer tokenizer;
    private Parser parser;
    private FacAssemblyGenerator assemblyGenerator;
    private Formatter formatter;
    private OutputStream outputStream;

    public static Compiler createNew(Reader input, int addressBuffer, OutputStream outputStream) throws IOException {
        Tokenizer t = new Tokenizer(input);
        Parser p = new Parser(t);
        FacAssemblyGenerator g = new FacAssemblyGenerator(addressBuffer);
        Formatter formatter = new PrettyFormatter();
        return new Compiler(t,p,g, formatter, outputStream);
    }

    public Compiler(Tokenizer tokenizer, Parser parser, FacAssemblyGenerator assemblyGenerator, Formatter formatter, OutputStream outputStream) {
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.assemblyGenerator = assemblyGenerator;
        this.formatter = formatter;
        this.outputStream = outputStream;
    }

    public void compile() throws IOException {
        parser.parse();
        parser.getAst().generateAssembly(assemblyGenerator);
        formatter.writeCode(assemblyGenerator.getGeneratedAssembly(), outputStream);
    }
}
