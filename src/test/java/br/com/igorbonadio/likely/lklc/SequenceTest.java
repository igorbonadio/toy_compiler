package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class SequenceTest extends TestCase {

  public SequenceTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(SequenceTest.class);
  }

  public void testFatSequenceOfSingleElement() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[1]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(1))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testThinSequenceOfSingleElement() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[\n  1\n]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(1))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testFatSequenceOfMultipleElement() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[1,2,3]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(1)))
              .addSequence(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(2)))
              .addSequence(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(3))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testThinSequenceOfMultipleElement() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[\n  1\n  2\n  3\n]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(1)))
              .addSequence(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(2)))
              .addSequence(
                Expression.newBuilder()
                  .setTypeCode(Expression.ExpressionType.BUILTIN)
                  .setBuiltin(
                    Builtin.newBuilder()
                      .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                      .setIntegerNumber(3))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testFatSequenceOfFatSequences() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[[1,2,3], [4, 5, 6]]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.SEQUENCE)
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(1)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(2)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(3)))))
              .addSequence(
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
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(4)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(5)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(6))))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }

  public void testThinSequenceOfThinSequences() {
    LikelyParser parser = new LikelyParser(new CommonTokenStream(new LikelyLexer(new ANTLRInputStream("[\n  [\n    1\n    2\n    3\n  ]\n  [\n    4\n    5\n    6\n  ]\n]"))));
    ParseTree tree = parser.file_input();

    ParseTreeWalker walker = new ParseTreeWalker();
    ProtocolBufferListener listener = new ProtocolBufferListener();
    walker.walk(listener, tree);
    Program program = listener.getProtocolBuffer();

    Program expectedProgram = Program.newBuilder()
      .addStatements(
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
                      .setTypeCode(Builtin.BuiltinType.SEQUENCE)
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(1)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(2)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(3)))))
              .addSequence(
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
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(4)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(5)))
                      .addSequence(
                        Expression.newBuilder()
                          .setTypeCode(Expression.ExpressionType.BUILTIN)
                          .setBuiltin(
                            Builtin.newBuilder()
                              .setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
                              .setIntegerNumber(6))))))).build();

    assertEquals(expectedProgram.toString(), program.toString());
  }
}
