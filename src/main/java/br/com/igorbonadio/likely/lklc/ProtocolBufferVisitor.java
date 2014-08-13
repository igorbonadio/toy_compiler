package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProtocolBufferVisitor extends LikelyBaseVisitor<Expression.Builder> {
  private Program.Builder program;

  public Expression.Builder visitFile_input(LikelyParser.File_inputContext ctx) {
    program = Program.newBuilder();
    visit(ctx.header());
    for (int i = 0; i < ctx.stmt().size(); i++) {
      program.addStatements(visit(ctx.stmt(i)));
    }
    return null;
  }

  public Expression.Builder visitHeader(LikelyParser.HeaderContext ctx) {
    for (int i = 0; i < ctx.impt().size(); i++) {
      visit(ctx.impt(i));
    }
    return null;
  }

  public Expression.Builder visitImpt(LikelyParser.ImptContext ctx) {
    ImportPackage.Builder impts = ImportPackage.newBuilder();
    String path = ctx.STRING().getText().substring(1, ctx.STRING().getText().length()-1);
    if (ctx.ID() != null) {
      impts.setPackageName(ctx.ID().getText());
    } else {
      String[] p = path.split("/");
      impts.setPackageName(p[p.length-1]);
    }
    impts.setPackagePath(path);
    program.addImportedPackages(impts);
    return null;
  }

  public Expression.Builder visitStmt(LikelyParser.StmtContext ctx) {
    return visit(ctx.expr());
  }

  private Expression.Builder visitIfNotNull(ParserRuleContext... ctxs) {
    for (int i = 0; i < ctxs.length; i++) {
      if (ctxs[i] != null)
        return visit(ctxs[i]);
    }
    return Expression.newBuilder();
  }

  public Expression.Builder visitExpr(LikelyParser.ExprContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.literal(),
      ctx.attr(),
      ctx.seq(),
      ctx.func_call(),
      ctx.dist(),
      ctx.return_expr(),
      ctx.obj_msg(),
      ctx.constructor_call(),
      ctx.comp_expr()
    );
    if (ctx.OPEN_PAREN() != null) {
      expr = visit(ctx.expr(0));
    } else if (ctx.ID() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.ID)
          .setString(ctx.ID().getText());
    } else if (ctx.ARROW() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.PAIR)
          .setLhs(visit(ctx.expr(0)))
          .setRhs(visit(ctx.expr(1)));
    } else if (ctx.op() != null) {
      expr = Expression.newBuilder();
      expr = visit(ctx.op());
      expr.setLhs(visit(ctx.expr(0)))
          .setRhs(visit(ctx.expr(1)));
    } else if (ctx.NOT() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.NOT)
          .setRhs(visit(ctx.expr(0)));
    }
    return expr;
  }

  public Expression.Builder visitDist(LikelyParser.DistContext ctx) {
    Expression.Builder expr = visitIfNotNull(ctx.dist_body());
    expr.setType(Expression.Type.FUNCTION_CALL);
    return expr;
  }

  public Expression.Builder visitDist_body(LikelyParser.Dist_bodyContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.dist_body_fat(),
      ctx.dist_body_thin()
    );
    return expr;
  }

  public Expression.Builder visitDist_body_fat(LikelyParser.Dist_body_fatContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nProb = ctx.prob().size();
    for (int i = 0; i < nProb; i++) {
      expr.addBlock1(visit(ctx.prob(i)));
    }
    return expr;
  }

  public Expression.Builder visitDist_body_thin(LikelyParser.Dist_body_thinContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nProb = ctx.prob().size();
    for (int i = 0; i < nProb; i++) {
      expr.addBlock1(visit(ctx.prob(i)));
    }
    return expr;
  }

  public Expression.Builder visitProb(LikelyParser.ProbContext ctx) {
    Expression.Builder expr = visit(ctx.prob_vars());
    expr.setType(Expression.Type.PROBABILITY)
        .setRhs(visit(ctx.expr()));
    return expr;
  }

  public Expression.Builder visitProb_vars(LikelyParser.Prob_varsContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.joint_vars(),
      ctx.cond_vars()
    );
    return expr;
  }

  public Expression.Builder visitJoint_vars(LikelyParser.Joint_varsContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    if (ctx.sample() != null) {
      java.util.List<String> s = visit(ctx.sample()).getStrings1List();
      String txt = "";
      for (int j = 0; j < s.size(); j++) {
        txt = txt.concat(s.get(j)).concat(" ");
      }
      expr.addStrings1(txt.trim());
    } else if (ctx.var_list() != null) {
      expr = visit(ctx.var_list());
    }
    return expr;
  }

  public Expression.Builder visitVar_list(LikelyParser.Var_listContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nVar = ctx.sample().size();
    for (int i = 0; i < nVar; i++) {
      java.util.List<String> s = visit(ctx.sample(i)).getStrings1List();
      String txt = "";
      for (int j = 0; j < s.size(); j++) {
        txt = txt.concat(s.get(j)).concat(" ");
      }
      expr.addStrings1(txt.trim());
    }
    return expr;
  }

  public Expression.Builder visitSample(LikelyParser.SampleContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    int nSample = ctx.ID().size();
    for (int i = 0; i < nSample; i++) {
      expr.addStrings1(ctx.ID(i).getText());
    }
    return expr;
  }

  public Expression.Builder visitCond_vars(LikelyParser.Cond_varsContext ctx) {
    Expression.Builder expr = visit(ctx.joint_vars(0));
    java.util.List<String> cond = visit(ctx.joint_vars(1)).getStrings1List();
    for (int i = 0; i < cond.size(); i++) {
      expr.addStrings2(cond.get(i));
    }
    return expr;
  }

  public Expression.Builder visitConstructor_call(LikelyParser.Constructor_callContext ctx) {
    Expression.Builder expr = visit(ctx.func_call());
    java.util.List<Expression> block = visit(ctx.block()).getBlock1List();
    for (int i = 0; i < block.size(); i++) {
      expr.addBlock2(block.get(i));
    }
    return expr;
  }

  public Expression.Builder visitComp_expr(LikelyParser.Comp_exprContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.if_expr(),
      ctx.for_expr(),
      ctx.while_expr(),
      ctx.func_def()
    );
    return expr;
  }

  public Expression.Builder visitWhile_expr(LikelyParser.While_exprContext ctx) {
    Expression.Builder expr = visit(ctx.block());
    expr.setType(Expression.Type.WHILE)
        .setRhs(visit(ctx.expr()));
    return expr;
  }

  public Expression.Builder visitFor_expr(LikelyParser.For_exprContext ctx) {
    Expression.Builder expr = visit(ctx.block());
    expr.setType(Expression.Type.FOR)
        .setString(ctx.ID().getText())
        .setLhs(visit(ctx.expr(0)))
        .setRhs(visit(ctx.expr(1)));
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
    Expression.Builder expr = visitIfNotNull(
      ctx.fat_expr(),
      ctx.thin_expr()
    );
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
    Expression.Builder expr = visitIfNotNull(
      ctx.literal(),
      ctx.seq(),
      ctx.prob(),
      ctx.expr()
    );
    if (ctx.ID() != null) {
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
    Expression.Builder expr = visitIfNotNull(ctx.list_body());
    return expr;
  }

  public Expression.Builder visitFunc(LikelyParser.FuncContext ctx) {
    Expression.Builder expr = visitIfNotNull(ctx.obj_msg());
    if (ctx.ID() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.ID)
          .setString(ctx.ID().getText());
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
      case "and": expr.setType(Expression.Type.AND); break;
      case "or":  expr.setType(Expression.Type.OR); break;
    }
    return expr;
  }

  public Expression.Builder visitSeq(LikelyParser.SeqContext ctx) {
    Expression.Builder expr = visitIfNotNull(ctx.list_body());
    expr.setType(Expression.Type.SEQUENCE);
    return expr;
  }

  public Expression.Builder visitList_body(LikelyParser.List_bodyContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.list_body_fat(),
      ctx.list_body_thin()
    );
    return expr;
  }

  public Expression.Builder visitList_body_fat(LikelyParser.List_body_fatContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < ctx.expr().size(); i++) {
      expr.addBlock1(visit(ctx.expr(i)));
    }
    return expr;
  }

  public Expression.Builder visitList_body_thin(LikelyParser.List_body_thinContext ctx) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < ctx.expr().size(); i++) {
      expr.addBlock1(visit(ctx.expr(i)));
    }
    return expr;
  }

  public Expression.Builder visitAttr(LikelyParser.AttrContext ctx) {
    Expression.Builder container = visitIfNotNull(ctx.obj_msg());
    if (ctx.ID() != null) {
      container = Expression.newBuilder();
      container.setType(Expression.Type.ID)
               .setString(ctx.ID().getText());
    }
    return Expression.newBuilder()
      .setType(Expression.Type.ATTRIBUTION)
      .setLhs(container)
      .setRhs(visit(ctx.expr()));
  }

  public Expression.Builder visitLiteral(LikelyParser.LiteralContext ctx) {
    Expression.Builder expr = visitIfNotNull(ctx.number());
    if (ctx.STRING() != null) {
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