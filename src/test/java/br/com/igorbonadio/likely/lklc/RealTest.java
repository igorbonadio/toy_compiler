package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class RealTest extends TestCase {

  public RealTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(RealTest.class);
  }

  public void testReal() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1.0"))));
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
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1.0;3.0;"))));
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
