package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProtocolBufferListener extends LikelyBaseListener {
  private Program.Builder program = Program.newBuilder();
  private Builtin.Builder buildin;

  private java.util.Stack<Expression.Builder> stackExpr = new java.util.Stack<>();
  private java.util.Stack<Integer> stackNumberOfExpr = new java.util.Stack<>();

  public void exitStmt(LikelyParser.StmtContext ctx) {
    program.addStatements(stackExpr.pop());
  }

  public void exitExpr(LikelyParser.ExprContext ctx) {
    Expression.Builder expr = Expression.newBuilder()
      .setTypeCode(Expression.ExpressionType.BUILTIN)
      .setBuiltin(buildin);
    stackExpr.push(expr);
  }

  public void exitSeq(LikelyParser.SeqContext ctx) {
    Builtin.Builder sequence = Builtin.newBuilder()
      .setTypeCode(Builtin.BuiltinType.SEQUENCE);

    java.util.Vector<Expression.Builder> e = new java.util.Vector<>();
    int n = stackNumberOfExpr.pop();
    for(int i = 0; i < n; i++) {
      e.add(stackExpr.pop());
    }
    for(int i = 0; i < n; i++) {
      sequence.addSequence(e.get(n-i-1));
    }

    buildin = sequence;
  }

  public void exitList_body_fat(LikelyParser.List_body_fatContext ctx) {
    stackNumberOfExpr.push(ctx.expr().size());
  }

  public void exitList_body_thin(LikelyParser.List_body_thinContext ctx) {
    stackNumberOfExpr.push(ctx.expr().size());
  }

  public void exitNumber(LikelyParser.NumberContext ctx) {
    if (ctx.INTEGER() != null) {
      buildin = Builtin.newBuilder();
      buildin.setTypeCode(Builtin.BuiltinType.INTEGER_NUMBER)
             .setIntegerNumber(Integer.valueOf(ctx.INTEGER().getText()));
    } else if (ctx.FLOAT() != null) {
      buildin = Builtin.newBuilder();
      buildin.setTypeCode(Builtin.BuiltinType.REAL_NUMBER)
             .setRealNumber(Double.valueOf(ctx.FLOAT().getText()));
    }
  }

  public void exitLiteral(LikelyParser.LiteralContext ctx) {
    if (ctx.STRING() != null) {
      String str = ctx.STRING().getText();
      buildin = Builtin.newBuilder();
      buildin.setTypeCode(Builtin.BuiltinType.STRING)
             .setStr(str.substring(1,str.length()-1));
    }
  }

  public void exitBool(LikelyParser.BoolContext ctx) {
    if (ctx.getText().equals("true")) {
      buildin = Builtin.newBuilder()
        .setTypeCode(Builtin.BuiltinType.BOOLEAN)
        .setB(true);
    } else if (ctx.getText().equals("false")) {
      buildin = Builtin.newBuilder()
        .setTypeCode(Builtin.BuiltinType.BOOLEAN)
        .setB(false);
    }
  }

  public Program getProtocolBuffer() {
    return program.build();
  }
}