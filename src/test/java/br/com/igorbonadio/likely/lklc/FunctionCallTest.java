package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class FunctionCallTest extends TestCase {

  public FunctionCallTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(FunctionCallTest.class);
  }

  public void testFunctionCall() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x(1,2)"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
          .setFunctionCall(
            FunctionCall.newBuilder()
              .setFunction(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.ID)
                  .setId("x"))
              .addArguments(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1)))
              .addArguments(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(2))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testFunction2Call() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x(1,2)(3,4)"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
          .setFunctionCall(
            FunctionCall.newBuilder()
              .setFunction(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
                  .setFunctionCall(
                    FunctionCall.newBuilder()
                      .setFunction(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.ID)
                          .setId("x"))
                      .addArguments(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER)
                              .setInteger(1)))
                      .addArguments(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER)
                              .setInteger(2)))))
              .addArguments(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(3)))
              .addArguments(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(4))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
