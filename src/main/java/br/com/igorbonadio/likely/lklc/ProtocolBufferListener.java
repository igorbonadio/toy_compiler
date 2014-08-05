package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProtocolBufferListener extends LikelyBaseListener {
  private Program.Builder program = Program.newBuilder();
  private Builtin.Builder buildin;

  public void exitExpr(LikelyParser.ExprContext ctx) {
    Expression expr = Expression.newBuilder()
      .setTypeCode(Expression.ExpressionType.BUILTIN)
      .setBuiltin(buildin).build();

    program.addStatements(expr);
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

  public Program getProtocolBuffer() {
    return program.build();
  }
}