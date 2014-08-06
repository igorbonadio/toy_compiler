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

  public void testSingleDigitInteger() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BUILTIN)
          .setBuiltin(
            Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.INTEGER)
              .setInteger(1))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testLongInteger() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("123"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BUILTIN)
          .setBuiltin(
            Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.INTEGER)
              .setInteger(123))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testTwoIntegers() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1;2;"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BUILTIN)
          .setBuiltin(
            Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.INTEGER)
              .setInteger(1)))
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BUILTIN)
          .setBuiltin(
            Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.INTEGER)
              .setInteger(2))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
