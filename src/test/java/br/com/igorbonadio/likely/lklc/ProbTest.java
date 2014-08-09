package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProbTest extends TestCase {

  public ProbTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ProbTest.class);
  }

  public void testProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob(x = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testJointProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob((x,y) = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .addStrings1("y")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testConditionalProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob(x | y = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .addStrings2("y")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testVLMCProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob(x | y y y = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .addStrings2("y y y")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testJointVLMCProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob((x, z) | (y y y, w w w) = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .addStrings1("z")
              .addStrings2("y y y")
              .addStrings2("w w w")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testConditionalJointProb() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("Prob((x,y) | (z,w) = 0.5)"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.PROBABILITY)
              .addStrings1("x")
              .addStrings1("y")
              .addStrings2("z")
              .addStrings2("w")
              .setRhs(
                Expression.newBuilder()
                  .setType(Expression.Type.REAL)
                  .setReal(0.5)))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
