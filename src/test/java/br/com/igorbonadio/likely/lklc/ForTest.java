package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ForTest extends TestCase {

  public ForTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ForTest.class);
  }

  public void testFor() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("for (i <- 1:2): 3"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FOR)
          .setString("i")
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(2))
          .addBlockTrue(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(3))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
