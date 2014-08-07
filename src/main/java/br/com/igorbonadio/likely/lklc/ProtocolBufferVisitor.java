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
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.literal() != null) {
      expr = visit(ctx.literal());
    } else if (ctx.attr() != null) {
      expr = visit(ctx.attr());
    } else if (ctx.seq() != null) {
      expr = visit(ctx.seq());
    } else if (ctx.func_call() != null) {
      expr = visit(ctx.func_call());
    } else if (ctx.func_call() != null) {
      expr = visit(ctx.func_call());
    } else if (ctx.dist() != null) {
      expr = visit(ctx.dist());
    } else if (ctx.return_expr() != null) {
      expr = visit(ctx.return_expr());
    } else if (ctx.obj_msg() != null) {
      expr = visit(ctx.obj_msg());
    } else if (ctx.constructor_call() != null) {
      expr = visit(ctx.constructor_call());
    } else if (ctx.comp_expr() != null) {
      expr = visit(ctx.comp_expr());
    } else if (ctx.ID() != null) {
      expr.setType(Expression.Type.ID)
          .setString(ctx.ID().getText());
    } else if (ctx.ARROW() != null) {

    } else if (ctx.op() != null) {

    } else if (ctx.OPEN_PAREN() != null) {

    }
    return expr;
  }

  public Expression.Builder visitAttr(LikelyParser.AttrContext ctx) {
    Expression.Builder container = Expression.newBuilder();
    if (ctx.ID() != null) {
      container.setType(Expression.Type.ID)
               .setString(ctx.ID().getText());
    } else if (ctx.obj_msg() != null) {
      container = visit(ctx.obj_msg());
    }
    return Expression.newBuilder()
      .setType(Expression.Type.ATTRIBUTION)
      .setLhs(container)
      .setRhs(visit(ctx.expr()));
  }

  public Expression.Builder visitLiteral(LikelyParser.LiteralContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.number() != null) {
      expr = visit(ctx.number());
    } else if (ctx.STRING() != null) {
      expr.setType(Expression.Type.STRING)
          .setString(ctx.STRING().getText().substring(1, ctx.STRING().getText().length()-1));
    } if (ctx.bool() != null) {
      expr.setType(Expression.Type.BOOLEAN)
          .setBoolean(ctx.bool().getText().equals("true"));
    }
    return expr;
  }

  public Expression.Builder visitNumber(LikelyParser.NumberContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.INTEGER() != null) {
      expr.setType(Expression.Type.INTEGER)
          .setInteger(Integer.valueOf(ctx.INTEGER().getText()));
    } else if (ctx.FLOAT() != null) {
      expr.setType(Expression.Type.REAL)
          .setReal(Double.valueOf(ctx.FLOAT().getText()));
    }
    return expr;
  }

  public Program getProtocolBuffer() {
    return program.build();
  }
}