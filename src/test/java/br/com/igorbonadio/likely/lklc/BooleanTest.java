package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class BooleanTest extends TestCase {

  public BooleanTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(BooleanTest.class);
  }

  public void testBoolean() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("true"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.BOOLEAN)
          .setBoolean(true)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void test2Booleans() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("true;false;"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.BOOLEAN)
          .setBoolean(true))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.BOOLEAN)
          .setBoolean(false)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
