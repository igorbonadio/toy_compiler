package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class FunctionDefinitionTest extends TestCase {

  public FunctionDefinitionTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FunctionDefinitionTest.class);
  }

  public void testFunctionDefinitionWithoutParameters() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("function(): 1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_DEFINITION)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testFunctionDefinitionWithParameters() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("function(x, y): 1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_DEFINITION)
          .addStrings1("x")
          .addStrings1("y")
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
