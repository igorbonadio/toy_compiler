package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class FloatTest extends TestCase {

  public FloatTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FloatTest.class);
  }

  public void testFloat() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1.0"))));
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
              .setTypeCode(Builtin.BuiltinType.REAL_NUMBER)
              .setRealNumber(1.0))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testTwoFloat() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1.0\n2.3"))));
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
              .setTypeCode(Builtin.BuiltinType.REAL_NUMBER)
              .setRealNumber(1.0)))
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BUILTIN)
          .setBuiltin(
            Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.REAL_NUMBER)
              .setRealNumber(2.3))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
