package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class IntegerTest extends TestCase {

  public IntegerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(IntegerTest.class);
  }

  public void testInteger() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.INTEGER)
          .setInteger(1)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void test2Integers() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1;2;"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.INTEGER)
          .setInteger(1))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.INTEGER)
          .setInteger(2)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
