package br.com.igorbonadio.toy_compiler.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.toy_compiler.ast.ToyCompilerAst.*;

public class ImportTest extends TestCase {

  public ImportTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(ImportTest.class);
  }

  public void testImport() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import \"path/to/test\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("test")
          .setPackagePath("path/to/test")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testImportWithName() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import hello \"path/to/test\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("hello")
          .setPackagePath("path/to/test")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testImportShortPath() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import \"test\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("test")
          .setPackagePath("test")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testImportShortPathWithName() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import hello \"test\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("hello")
          .setPackagePath("test")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testListOfImports() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import \"path/to/test\"\nimport \"path/to/test2\""))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("test")
          .setPackagePath("path/to/test"))
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("test2")
          .setPackagePath("path/to/test2")).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }

  public void testImportAndExpressions() {
    ToyCompileParser parser = new ToyCompileParser(new CommonTokenStream(new ToyCompileLexer(new ANTLRInputStream("import \"path/to/test\"\n1"))));
    ParseTree tree = parser.file_input();

    ProtocolBufferVisitor visitor = new ProtocolBufferVisitor();
    visitor.visit(tree);
    Program program = visitor.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addImportedPackages(
        ImportPackage.newBuilder()
          .setPackageName("test")
          .setPackagePath("path/to/test"))
      .addStatements(
        Expression.newBuilder()
          .setType(Expression.Type.INTEGER)
          .setInteger(1)).build();

    assertEquals(program.toString(), expectedProgram.toString());
  }
}
