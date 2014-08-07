package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ObjectMessageTest extends TestCase {

  public ObjectMessageTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ObjectMessageTest.class);
  }

  public void testObjectMessage() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.ID)
                  .setId("x"))
              .setMessage("y"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testBuiltinObjectMessage() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("1.y"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1)))
              .setMessage("y"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testSequenceObjectMessage() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[1,2,3].y"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.SEQUENCE)
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER)
                              .setInteger(1)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER)
                              .setInteger(2)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER)
                              .setInteger(3)))))
              .setMessage("y"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testObjectWith2Messages() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y.z"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
                  .setObjectMessage(
                    ObjectMessage.newBuilder()
                      .setObject(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.ID)
                          .setId("x"))
                      .setMessage("y")))
              .setMessage("z"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testObjectWith2MessagesAndACall() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y(1,2).z"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
                  .setFunctionCall(
                    FunctionCall.newBuilder()
                      .setFunction(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
                          .setObjectMessage(
                            ObjectMessage.newBuilder()
                              .setObject(
                                Expression.newBuilder()
                                  .setTypeCode(Expression.ExpressionType.ID)
                                  .setId("x"))
                              .setMessage("y")))
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
              .setMessage("z"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testObjectWith3MessagesAnd2Call() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y(1,2).z(3,4).w"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
          .setObjectMessage(
            ObjectMessage.newBuilder()
              .setObject(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
                  .setFunctionCall(
                    FunctionCall.newBuilder()
                      .setFunction(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
                          .setObjectMessage(
                            ObjectMessage.newBuilder()
                              .setObject(
                                Expression.newBuilder()
                                  .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
                                  .setFunctionCall(
                                    FunctionCall.newBuilder()
                                      .setFunction(
                                        Expression.newBuilder()
                                          .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
                                          .setObjectMessage(
                                            ObjectMessage.newBuilder()
                                              .setObject(
                                                Expression.newBuilder()
                                                  .setTypeCode(Expression.ExpressionType.ID)
                                                  .setId("x"))
                                              .setMessage("y")))
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
                              .setMessage("z")))
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
                              .setInteger(4)))))
              .setMessage("w"))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
