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

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.OBJECT_MESSAGE)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("x"))
          .setString("y")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testObject2Message() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y.z"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.OBJECT_MESSAGE)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.OBJECT_MESSAGE)
              .setLhs(
                Expression.newBuilder()
                  .setType(Expression.Type.ID)
                  .setString("x"))
              .setString("y"))
          .setString("z")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testObject2MessageAndACall() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("x.y().z"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.OBJECT_MESSAGE)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.FUNCTION_CALL)
              .setLhs(
                Expression.newBuilder()
                  .setType(Expression.Type.OBJECT_MESSAGE)
                  .setLhs(
                    Expression.newBuilder()
                      .setType(Expression.Type.ID)
                      .setString("x"))
                  .setString("y")))
          .setString("z")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
