package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class RealTest extends TestCase {

  public RealTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(RealTest.class);
  }

  public void testReal() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("1.0"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.REAL)
          .setReal(1.0)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void test2Reals() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("1.0;3.0;"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.REAL)
          .setReal(1.0))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.REAL)
          .setReal(3.0)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
