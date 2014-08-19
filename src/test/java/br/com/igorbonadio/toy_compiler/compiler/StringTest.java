package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class StringTest extends TestCase {

  public StringTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(StringTest.class);
  }

  public void testString() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("\"hello\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.STRING)
          .setString("hello")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void test2Strings1() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("\"hello\"\n\"pequena\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.STRING)
          .setString("hello"))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.STRING)
          .setString("pequena")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
