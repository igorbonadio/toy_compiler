package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class IfTest extends TestCase {

  public IfTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(IfTest.class);
  }

  public void testIf() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("if (x): 1 else: 2"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.IF)
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("x"))
          .addBlock1(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))
          .addBlock2(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(2))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
