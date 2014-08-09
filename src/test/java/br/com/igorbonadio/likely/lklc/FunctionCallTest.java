package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class FunctionCallTest extends TestCase {

  public FunctionCallTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FunctionCallTest.class);
  }

  public void testFunctionCallWithoutParameters() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("f()"))));
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
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("f(1,2)"))));
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
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("f(1,2)(3,4)"))));
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
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.f()"))));
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
