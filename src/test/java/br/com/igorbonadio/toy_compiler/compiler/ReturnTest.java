package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class ReturnTest extends TestCase {

  public ReturnTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ReturnTest.class);
  }

  public void testReturn() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("return 1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.RETURN)
          .setRhs(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
