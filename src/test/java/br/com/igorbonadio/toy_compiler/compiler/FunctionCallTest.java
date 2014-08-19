package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class FunctionCallTest extends TestCase {

  public FunctionCallTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FunctionCallTest.class);
  }

  public void testFunctionCallWithoutParameters() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("f()"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("f"))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testFunctionCallWithParameters() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("f(1,2)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("f"))
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(2))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testFunctionCallOfFunctionCall() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("f(1,2)(3,4)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.FUNCTION_CALL)
              .setLhs(
                Expression.newBuilder()
                  .setType(Expression.Type.ID)
                  .setString("f"))
              .addBlock1(
                Expression.newBuilder()
                  .setType(Expression.Type.INTEGER)
                  .setInteger(1))
              .addBlock1(
                Expression.newBuilder()
                  .setType(Expression.Type.INTEGER)
                  .setInteger(2)))
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(3))
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(4))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testObjectMessageFunctionCall() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("x.f()"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.OBJECT_MESSAGE)
              .setLhs(
                Expression.newBuilder()
                  .setType(Expression.Type.ID)
                  .setString("x"))
              .setString("f"))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
