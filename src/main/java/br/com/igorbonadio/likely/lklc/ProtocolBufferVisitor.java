package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class ProtocolBufferVisitor extends LikelyBaseVisitor<Expression.Builder> {
  private Program.Builder program;

  public Expression.Builder visitFile_input(LikelyParser.File_inputContext ctx) {
    program = Program.newBuilder();
    visit(ctx.header());
    for (int i = 0; i < ctx.statement().size(); i++) {
      program.addStatements(visit(ctx.statement(i)));
    }
    return null;
  }

  public Expression.Builder visitHeader(LikelyParser.HeaderContext ctx) {
    for (int i = 0; i < ctx.importPackage().size(); i++) {
      visit(ctx.importPackage(i));
    }
    return null;
  }

  public Expression.Builder visitImportPackage(LikelyParser.ImportPackageContext ctx) {
    ImportPackage.Builder impts = ImportPackage.newBuilder();
    String path = removeQuotes(ctx.STRING().getText());
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

  public Expression.Builder visitStatement(LikelyParser.StatementContext ctx) {
    return visit(ctx.expression());
  }

  public Expression.Builder visitExpression(LikelyParser.ExpressionContext ctx) {
    Expression.Builder expr = visitIfNotNull(
      ctx.literal(),
      ctx.attribution(),
      ctx.sequence(),
      ctx.functionCall(),
      ctx.distribution(),
      ctx.returnExpression(),
      ctx.objectMessage(),
      ctx.constructorCall(),
      ctx.complexExpression());
    if (ctx.OPEN_PAREN() != null) {
      expr = visit(ctx.expression(0));
    } else if (ctx.ID() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.ID)
          .setString(ctx.ID().getText());
    } else if (ctx.ARROW() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.PAIR)
          .setLhs(visit(ctx.expression(0)))
          .setRhs(visit(ctx.expression(1)));
    } else if (ctx.op() != null) {
      expr = Expression.newBuilder();
      expr = visit(ctx.op());
      expr.setLhs(visit(ctx.expression(0)))
          .setRhs(visit(ctx.expression(1)));
    } else if (ctx.NOT() != null) {
      expr = Expression.newBuilder();
      expr.setType(Expression.Type.NOT)
          .setRhs(visit(ctx.expression(0)));
    }
    return expr;
  }

  public Expression.Builder visitDistribution(LikelyParser.DistributionContext ctx) {
    return visitIfNotNull(ctx.dist_body())
      .setType(Expression.Type.FUNCTION_CALL);
  }

  public Expression.Builder visitDist_body(LikelyParser.Dist_bodyContext ctx) {
    return visitIfNotNull(
      ctx.dist_body_fat(),
      ctx.dist_body_thin());
  }

  public Expression.Builder visitDist_body_fat(LikelyParser.Dist_body_fatContext ctx) {
    return addProbabilitiesToBlock1(ctx.prob());
  }

  public Expression.Builder visitDist_body_thin(LikelyParser.Dist_body_thinContext ctx) {
    return addProbabilitiesToBlock1(ctx.prob());
  }

  public Expression.Builder visitProb(LikelyParser.ProbContext ctx) {
    return visit(ctx.prob_vars())
      .setType(Expression.Type.PROBABILITY)
      .setRhs(visit(ctx.expression()));
  }

  public Expression.Builder visitProb_vars(LikelyParser.Prob_varsContext ctx) {
    return visitIfNotNull(
      ctx.joint_vars(),
      ctx.cond_vars());
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

  public Expression.Builder visitConstructorCall(LikelyParser.ConstructorCallContext ctx) {
    Expression.Builder expr = visit(ctx.functionCall());
    java.util.List<Expression> block = visit(ctx.block()).getBlock1List();
    for (int i = 0; i < block.size(); i++) {
      expr.addBlock2(block.get(i));
    }
    return expr;
  }

  public Expression.Builder visitComplexExpression(LikelyParser.ComplexExpressionContext ctx) {
    return visitIfNotNull(
      ctx.if_expr(),
      ctx.for_expr(),
      ctx.while_expr(),
      ctx.func_def());
  }

  public Expression.Builder visitWhile_expr(LikelyParser.While_exprContext ctx) {
    return visit(ctx.block())
      .setType(Expression.Type.WHILE)
      .setRhs(visit(ctx.expression()));
  }

  public Expression.Builder visitFor_expr(LikelyParser.For_exprContext ctx) {
    return visit(ctx.block())
      .setType(Expression.Type.FOR)
      .setString(ctx.ID().getText())
      .setLhs(visit(ctx.expression(0)))
      .setRhs(visit(ctx.expression(1)));
  }

  public Expression.Builder visitIf_expr(LikelyParser.If_exprContext ctx) {
    Expression.Builder expr = Expression.newBuilder()
      .setType(Expression.Type.IF)
      .setRhs(visit(ctx.expression()));

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
    return visitIfNotNull(
      ctx.fat_expr(),
      ctx.thin_expr());
  }

  public Expression.Builder visitFat_expr(LikelyParser.Fat_exprContext ctx) {
    return addExpressionToBlock1(ctx.expression());
  }

  public Expression.Builder visitThin_expr(LikelyParser.Thin_exprContext ctx) {
    return addExpressionToBlock1(ctx.expression());
  }

  public Expression.Builder visitObjectMessage(LikelyParser.ObjectMessageContext ctx) {
    Expression.Builder obj = visit(ctx.obj());
    int nMsg = ctx.ID().size();
    for (int i = 0; i < nMsg; i++) {
      Expression.Builder objectMessage = Expression.newBuilder()
        .setType(Expression.Type.OBJECT_MESSAGE)
        .setLhs(obj)
        .setString(ctx.ID(i).getText());
      obj = objectMessage;
      if (ctx.list(i) != null) {
        Expression.Builder f_call = visit(ctx.list(i));
        f_call.setType(Expression.Type.FUNCTION_CALL)
              .setLhs(objectMessage);
        obj = f_call;
      }
    }
    return obj;
  }

  public Expression.Builder visitObj(LikelyParser.ObjContext ctx) {
    if (ctx.ID() != null)
      return createID(ctx.ID());
    return visitIfNotNull(
      ctx.literal(),
      ctx.sequence(),
      ctx.prob(),
      ctx.expression());
  }

  public Expression.Builder visitReturnExpression(LikelyParser.ReturnExpressionContext ctx) {
    return Expression.newBuilder()
      .setType(Expression.Type.RETURN)
      .setRhs(visit(ctx.expression()));
  }

  public Expression.Builder visitFunctionCall(LikelyParser.FunctionCallContext ctx) {
    Expression.Builder func = visit(ctx.func());
    for (int i = 0; i < ctx.list().size(); i++) {
      Expression.Builder args = visit(ctx.list(i))
        .setType(Expression.Type.FUNCTION_CALL)
        .setLhs(func);
      func = args;
    }
    return func;
  }

  public Expression.Builder visitList(LikelyParser.ListContext ctx) {
    return visitIfNotNull(ctx.listBody());
  }

  public Expression.Builder visitFunc(LikelyParser.FuncContext ctx) {
    Expression.Builder expr = visitIfNotNull(ctx.objectMessage());
    if (ctx.ID() != null)
      expr = createID(ctx.ID());
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

  public Expression.Builder visitSequence(LikelyParser.SequenceContext ctx) {
    return visitIfNotNull(ctx.listBody())
      .setType(Expression.Type.SEQUENCE);
  }

  public Expression.Builder visitListBody(LikelyParser.ListBodyContext ctx) {
    return visitIfNotNull(
      ctx.fatListBody(),
      ctx.list_body_thin());
  }

  public Expression.Builder visitFatListBody(LikelyParser.FatListBodyContext ctx) {
    return addExpressionToBlock1(ctx.expression());
  }

  public Expression.Builder visitList_body_thin(LikelyParser.List_body_thinContext ctx) {
    return addExpressionToBlock1(ctx.expression());
  }

  public Expression.Builder visitAttribution(LikelyParser.AttributionContext ctx) {
    Expression.Builder container = visitIfNotNull(ctx.objectMessage());
    if (ctx.ID() != null)
      container = createID(ctx.ID());
    return Expression.newBuilder()
      .setType(Expression.Type.ATTRIBUTION)
      .setLhs(container)
      .setRhs(visit(ctx.expression()));
  }

  public Expression.Builder visitLiteral(LikelyParser.LiteralContext ctx) {
    if (ctx.STRING() != null)
      return createString(ctx.STRING());
    else if (ctx.bool() != null)
      return createBoolean(ctx.bool());
    return visitIfNotNull(ctx.number());
  }

  public Expression.Builder visitNumber(LikelyParser.NumberContext ctx) {
    if (ctx.INTEGER() != null)
      return createInteger(ctx.INTEGER());
    return createFloat(ctx.FLOAT());
  }

  public Program getProtocolBuffer() {
    return program.build();
  }

  private Expression.Builder createID(TerminalNode terminal) {
    return Expression.newBuilder()
      .setType(Expression.Type.ID)
      .setString(terminal.getText());
  }

  private Expression.Builder createString(TerminalNode terminal) {
    return Expression.newBuilder()
      .setType(Expression.Type.STRING)
      .setString(removeQuotes(terminal.getText()));
  }

  private String removeQuotes(String str) {
    return str.substring(1, str.length()-1);
  }

  private Expression.Builder createBoolean(LikelyParser.BoolContext bool) {
    return Expression.newBuilder()
      .setType(Expression.Type.BOOLEAN)
      .setBoolean(bool.getText().equals("true"));
  }

  private Expression.Builder createInteger(TerminalNode terminal) {
    return Expression.newBuilder()
      .setType(Expression.Type.INTEGER)
      .setInteger(Integer.valueOf(terminal.getText()));
  }

  private Expression.Builder createFloat(TerminalNode terminal) {
    return Expression.newBuilder()
      .setType(Expression.Type.REAL)
      .setReal(Double.valueOf(terminal.getText()));
  }

  private Expression.Builder visitIfNotNull(ParserRuleContext... ctxs) {
    for (int i = 0; i < ctxs.length; i++) {
      if (ctxs[i] != null)
        return visit(ctxs[i]);
    }
    return Expression.newBuilder();
  }

  private Expression.Builder addProbabilitiesToBlock1(java.util.List<LikelyParser.ProbContext> elements) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < elements.size(); i++) {
      expr.addBlock1(visit(elements.get(i)));
    }
    return expr;
  }

  private Expression.Builder addExpressionToBlock1(java.util.List<LikelyParser.ExpressionContext> elements) {
    Expression.Builder expr = Expression.newBuilder();
    for (int i = 0; i < elements.size(); i++) {
      expr.addBlock1(visit(elements.get(i)));
    }
    return expr;
  }
}