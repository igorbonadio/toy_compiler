package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class StringTest extends TestCase {

  public StringTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(StringTest.class);
  }

  public void testString() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("\"hello\""))));
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
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("\"hello\"\n\"pequena\""))));
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
