package parser.ast;


import codeGeneration.AssemblyGenerator;

public abstract class AstNode {
    public abstract String toPrettyString();
    public abstract void generateAssembly(AssemblyGenerator assGen);
}
