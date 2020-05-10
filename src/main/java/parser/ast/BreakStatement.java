package parser.ast;

import codeGeneration.AssemblyGenerator;

public class BreakStatement extends Statement {
    private int numberOfLoops;

    public BreakStatement(int numberOfLoops) {
        this.numberOfLoops = numberOfLoops;
    }

    @Override
    public String toPrettyString() {
        return "break(" + numberOfLoops + ");";
    }

    @Override
    public void generateAssembly(AssemblyGenerator assGen) {
        assGen.generateBreak(numberOfLoops);
    }
}
