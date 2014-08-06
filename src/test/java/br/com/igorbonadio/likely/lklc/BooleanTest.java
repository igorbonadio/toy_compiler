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

  public void testTrue() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("true"))));
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
              .setTypeCode(Builtin.BuiltinType.BOOLEAN)
              .setBoolean(true))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testFalse() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("false"))));
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
              .setTypeCode(Builtin.BuiltinType.BOOLEAN)
              .setBoolean(false))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
