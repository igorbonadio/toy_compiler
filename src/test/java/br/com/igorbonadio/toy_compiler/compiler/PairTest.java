package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class PairTest extends TestCase {

  public PairTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(PairTest.class);
  }

  public void testPair() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("1->2"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.PAIR)
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

  public void test2Pairs() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("1->2;3->4;"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.PAIR)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(2)))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.PAIR)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(3))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(4))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
