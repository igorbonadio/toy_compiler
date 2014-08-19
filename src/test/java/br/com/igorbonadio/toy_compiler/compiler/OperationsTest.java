package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class OperationsTest extends TestCase {

  public OperationsTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(OperationsTest.class);
  }

  public void testSum() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("1+2"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.ADDITION)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(2))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testExpression() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("(1+2)*3"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.MULTIPLICATION)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ADDITION)
              .setLhs(
                Expression.newBuilder()
                  .setType(Expression.Type.INTEGER)
                  .setInteger(1))
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.INTEGER)
                  .setInteger(2)))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(3))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testAnd() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("true and false"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.AND)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.BOOLEAN)
              .setBoolean(true))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.BOOLEAN)
              .setBoolean(false))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testNot() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("not true"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.NOT)
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.BOOLEAN)
              .setBoolean(true))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
