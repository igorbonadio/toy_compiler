package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class ConstructorCallTest extends TestCase {

  public ConstructorCallTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ConstructorCallTest.class);
  }

  public void testConstructorCall() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("f(): 1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.FUNCTION_CALL)
          .setLhs(
            Expression.newBuilder()
              .setType(Expression.Type.ID)
              .setString("f"))
          .addBlock2(
            Expression.newBuilder()
              .setType(Expression.Type.INTEGER)
              .setInteger(1))).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
