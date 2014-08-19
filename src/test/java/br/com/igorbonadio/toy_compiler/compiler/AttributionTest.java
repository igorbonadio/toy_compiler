package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class AttributionTest extends TestCase {

  public AttributionTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(AttributionTest.class);
  }

  public void testAttribution() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("x = 1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.ATTRIBUTION)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("x"))
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
