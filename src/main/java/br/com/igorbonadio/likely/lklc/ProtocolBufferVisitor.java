package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProtocolBufferVisitor extends LikelyBaseVisitor<Expression.Builder> {
  private Program.Builder program;

  public Expression.Builder visitFile_input(LikelyParser.File_inputContext ctx) {
    program = Program.newBuilder();
    for (int i = 0; i < ctx.stmt().size(); i++) {
      program.addStatements(visit(ctx.stmt(i)));
    }
    return null;
  }

  public Expression.Builder visitStmt(LikelyParser.StmtContext ctx) {
    return visit(ctx.expr());
  }

  public Expression.Builder visitExpr(LikelyParser.ExprContext ctx) {
    return visit(ctx.literal());
  }

  public Expression.Builder visitLiteral(LikelyParser.LiteralContext ctx) {
    return visit(ctx.number());
  }

  public Expression.Builder visitNumber(LikelyParser.NumberContext ctx) {
    Expression.Builder expr = Expression.newBuilder()
      .setType(Expression.Type.INTEGER)
      .setInteger(Integer.valueOf(ctx.INTEGER().getText()));
    return expr;
  }

  public Program getProtocolBuffer() {
    return program.build();
  }
}