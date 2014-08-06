package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class BinaryOperationTest extends TestCase {

  public BinaryOperationTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(BinaryOperationTest.class);
  }

  public void testSumOperation() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1 + 1"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BINARY_OPERATION)
          .setBinaryOperation(
            BinaryOperation.newBuilder()
              .setOperation(0)
              .setLhs(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1)))
              .setRhs(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testSubOperation() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1 - 1"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.BINARY_OPERATION)
          .setBinaryOperation(
            BinaryOperation.newBuilder()
              .setOperation(1)
              .setLhs(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1)))
              .setRhs(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
