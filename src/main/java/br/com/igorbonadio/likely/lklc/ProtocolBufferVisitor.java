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
      expr.setType(Expression.Type.PAIR)
          .setLhs(visit(ctx.expr(0)))
          .setRhs(visit(ctx.expr(1)));
    } else if (ctx.op() != null) {
      expr = visit(ctx.op());
      expr.setLhs(visit(ctx.expr(0)))
          .setRhs(visit(ctx.expr(1)));
    } else if (ctx.OPEN_PAREN() != null) {
      expr = visit(ctx.expr(0));
    }
    return expr;
  }

  public Expression.Builder visitComp_expr(LikelyParser.Comp_exprContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.if_expr() != null) {
      expr = visit(ctx.if_expr());
    } else if (ctx.for_expr() != null) {
      expr = visit(ctx.for_expr());
    } else if (ctx.while_expr() != null) {
      expr = visit(ctx.while_expr());
    } else if (ctx.func_def() != null) {
      expr = visit(ctx.func_def());
    }
    return expr;
  }

  public Expression.Builder visitIf_expr(LikelyParser.If_exprContext ctx) {
    Expression.Builder expr = Expression.newBuilder()
      .setType(Expression.Type.IF)
      .setRhs(visit(ctx.expr()));

    java.util.List<Expression> trueBlock = visit(ctx.block(0)).getBlock1List();
    java.util.List<Expression> falseBlock = visit(ctx.block(1)).getBlock1List();


    for (int i = 0; i < trueBlock.size(); i++) {
      expr.addBlock1(trueBlock.get(i));
    }

    for (int i = 0; i < falseBlock.size(); i++) {
      expr.addBlock2(falseBlock.get(i));
    }

    return expr;
  }

  public Expression.Builder visitBlock(LikelyParser.BlockContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.fat_expr() != null) {
      expr = visit(ctx.fat_expr());
    } else if (ctx.thin_expr() != null) {
      expr = visit(ctx.thin_expr());
    }
    return expr;
  }

  public Expression.Builder visitFat_expr(LikelyParser.Fat_exprContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nExpr = ctx.expr().size();
    for (int i = 0; i < nExpr; i++) {
      expr.addBlock1(visit(ctx.expr(i)));
    }
    return expr;
  }

  public Expression.Builder visitThin_expr(LikelyParser.Thin_exprContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nExpr = ctx.expr().size();
    for (int i = 0; i < nExpr; i++) {
      expr.addBlock1(visit(ctx.expr(i)));
    }
    return expr;
  }

  public Expression.Builder visitObj_msg(LikelyParser.Obj_msgContext ctx) {
    Expression.Builder obj = visit(ctx.obj());
    int nMsg = ctx.ID().size();
    for (int i = 0; i < nMsg; i++) {
      Expression.Builder obj_msg = Expression.newBuilder()
        .setType(Expression.Type.OBJECT_MESSAGE)
        .setLhs(obj)
        .setString(ctx.ID(i).getText());
      obj = obj_msg;
      if (ctx.list(i) != null) {
        Expression.Builder f_call = visit(ctx.list(i));
        f_call.setType(Expression.Type.FUNCTION_CALL)
              .setLhs(obj_msg);
        obj = f_call;
      }
    }
    return obj;
  }

  public Expression.Builder visitObj(LikelyParser.ObjContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.literal() != null) {
      expr = visit(ctx.literal());
    } else if (ctx.seq() != null) {
      expr = visit(ctx.seq());
    } else if (ctx.prob() != null) {
      expr = visit(ctx.prob());
    } else if (ctx.expr() != null) {
      expr = visit(ctx.expr());
    } else if (ctx.ID() != null) {
      expr = Expression.newBuilder()
        .setType(Expression.Type.ID)
        .setString(ctx.ID().getText());
    }
    return expr;
  }

  public Expression.Builder visitReturn_expr(LikelyParser.Return_exprContext ctx) {
    return Expression.newBuilder()
      .setType(Expression.Type.RETURN)
      .setRhs(visit(ctx.expr()));
  }

  public Expression.Builder visitFunc_call(LikelyParser.Func_callContext ctx) {
    Expression.Builder func = visit(ctx.func());
    int nCall = ctx.list().size();
    for (int i = 0; i < nCall; i++) {
      Expression.Builder args = visit(ctx.list(i));
      args.setType(Expression.Type.FUNCTION_CALL)
          .setLhs(func);
      func = args;
    }
    return func;
  }

  public Expression.Builder visitList(LikelyParser.ListContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.list_body() != null)
      expr = visit(ctx.list_body());
    return expr;
  }

  public Expression.Builder visitFunc(LikelyParser.FuncContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.ID() != null) {
      expr.setType(Expression.Type.ID)
          .setString(ctx.ID().getText());
    } else if (ctx.obj_msg() != null) {
      expr = visit(ctx.obj_msg());
    }
    return expr;
  }

  public Expression.Builder visitOp(LikelyParser.OpContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    switch (ctx.getText()) {
      case "+":   expr.setType(Expression.Type.ADDITION); break;
      case "-":   expr.setType(Expression.Type.SUBTRACTION); break;
      case "*":   expr.setType(Expression.Type.MULTIPLICATION); break;
      case "/":   expr.setType(Expression.Type.DIVISION); break;
      case "==":  expr.setType(Expression.Type.EQUAL_TO); break;
      case "!=":  expr.setType(Expression.Type.NOT_EQUAL_TO); break;
      case ">":   expr.setType(Expression.Type.GREATER_THAN); break;
      case ">=":  expr.setType(Expression.Type.GREATER_THAN_OR_EQUAL_TO); break;
      case "<":   expr.setType(Expression.Type.LESS_THAN); break;
      case "<=":  expr.setType(Expression.Type.LESS_THAN_OR_EQUAL_TO); break;
    }
    return expr;
  }

  public Expression.Builder visitSeq(LikelyParser.SeqContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.list_body() != null)
      expr = visit(ctx.list_body());
    expr.setType(Expression.Type.SEQUENCE);
    return expr;
  }

  public Expression.Builder visitList_body(LikelyParser.List_bodyContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.list_body_fat() != null) {
      expr = visit(ctx.list_body_fat());
    } else if (ctx.list_body_thin() != null) {
      expr = visit(ctx.list_body_thin());
    }
    return expr;
  }

  public Expression.Builder visitList_body_fat(LikelyParser.List_body_fatContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < ctx.expr().size(); i++) {
      expr.addExpressions(visit(ctx.expr(i)));
    }
    return expr;
  }

  public Expression.Builder visitList_body_thin(LikelyParser.List_body_thinContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < ctx.expr().size(); i++) {
      expr.addExpressions(visit(ctx.expr(i)));
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