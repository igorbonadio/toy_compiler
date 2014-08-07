package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class AttributionTest extends TestCase {

  public AttributionTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(AttributionTest.class);
  }

  public void testAttribution() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x = 1"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.ATTRIBUTION)
          .setAttribution(
            Attribution.newBuilder()
              .setId(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.ID)
                  .setId("x"))
              .setValue(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testAttributionToObjectMessage() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y = 1"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.ATTRIBUTION)
          .setAttribution(
            Attribution.newBuilder()
              .setId(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
                  .setObjectMessage(
                    ObjectMessage.newBuilder()
                      .setObject(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.ID)
                          .setId("x"))
                      .setMessage("y")))
              .setValue(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER)
                      .setInteger(1))))).build();

    // assertEquals(expectedProgram.toString(), program.toString());
  }
}
