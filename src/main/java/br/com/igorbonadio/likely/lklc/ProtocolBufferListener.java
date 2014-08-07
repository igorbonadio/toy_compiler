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
    if (ctx.ARROW() != null) {
      Expression.Builder value = stackExpr.pop();
      Expression.Builder key = stackExpr.pop();
      Expression.Builder expr = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.BUILTIN)
        .setBuiltin(
          Builtin.newBuilder()
              .setTypeCode(Builtin.BuiltinType.PAIR)
              .setPair(
                Pair.newBuilder()
                  .setKey(key)
                  .setValue(value)));
      stackExpr.push(expr);
    } else if (ctx.ID() != null) {
      Expression.Builder id = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.ID)
        .setId(ctx.ID().getText());
      stackExpr.push(id);
    } else if (ctx.attr() != null) {
    } else if (ctx.return_expr() != null) {
    } else if (ctx.obj_msg() != null) {
    } else if (ctx.func_call() != null) {
      java.util.Vector<Expression.Builder> e = new java.util.Vector<>();
      int n = stackNumberOfExpr.pop();
      for (int i = 0; i < n; i++) {
        e.add(stackExpr.pop());
      }
      Expression.Builder f = stackExpr.pop();
      FunctionCall.Builder fcall = FunctionCall.newBuilder()
        .setFunction(f);
      for (int i = 0; i < n; i++) {
        fcall.addArguments(e.get(n-i-1));
      }
      Expression.Builder expr = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
        .setFunctionCall(fcall);
      stackExpr.push(expr);
    } else if (ctx.op() != null) {
      Expression.Builder expr2 = stackExpr.pop();
      Expression.Builder expr1 = stackExpr.pop();
      int op = 0;
      if (ctx.op().getText().equals("+"))
        op = 0;
      else if (ctx.op().getText().equals("-"))
        op = 1;
      else if (ctx.op().getText().equals("*"))
        op = 2;
      else if (ctx.op().getText().equals("/"))
        op = 3;
      else if (ctx.op().getText().equals("=="))
        op = 4;
      else if (ctx.op().getText().equals("!="))
        op = 5;
      else if (ctx.op().getText().equals(">="))
        op = 6;
      else if (ctx.op().getText().equals("<="))
        op = 7;
      else if (ctx.op().getText().equals(">"))
        op = 8;
      else if (ctx.op().getText().equals("<"))
        op = 9;
      else if (ctx.op().getText().equals(":"))
        op = 10;
      Expression.Builder binop = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.BINARY_OPERATION)
        .setBinaryOperation(
          BinaryOperation.newBuilder()
            .setOperation(op)
            .setLhs(expr1)
            .setRhs(expr2));
      stackExpr.push(binop);
    } else {
      Expression.Builder expr = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.BUILTIN)
        .setBuiltin(buildin);
      stackExpr.push(expr);
    }
  }

  public void exitObj_msg(LikelyParser.Obj_msgContext ctx) {
    java.util.Stack<Expression.Builder> _stackExpr = new java.util.Stack<>();
    java.util.Stack<Integer> _stackNumberOfExpr = new java.util.Stack<>();

    int nMsg = ctx.ID().size();
    for (int i = 0; i < nMsg; i++) {
      if (ctx.list(i) != null) {
        int nargs = stackNumberOfExpr.pop();
        _stackNumberOfExpr.push(nargs);
        for (int j = 0; j < nargs; j++) {
          _stackExpr.push(stackExpr.pop());
        }
      }
    }


    Expression.Builder obj = stackExpr.pop();
    Expression.Builder expr = Expression.newBuilder();

    for (int i = 0; i < nMsg; i++) {
      expr = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.OBJECT_MESSAGE)
        .setObjectMessage(
          ObjectMessage.newBuilder()
            .setObject(obj)
            .setMessage(ctx.ID(i).getText()));
      if (ctx.list(i) != null) {
        java.util.Vector<Expression.Builder> e = new java.util.Vector<>();
        FunctionCall.Builder fcall = FunctionCall.newBuilder()
          .setFunction(expr);
        int nargs = _stackNumberOfExpr.pop();
        for (int j = 0; j < nargs; j++) {
          fcall.addArguments(_stackExpr.pop());
        }
        expr = Expression.newBuilder()
          .setTypeCode(Expression.ExpressionType.FUNCTION_CALL)
          .setFunctionCall(fcall);
      }
      obj = expr;
    }


    stackExpr.push(expr);
  }

  public void exitObj(LikelyParser.ObjContext ctx) {
    if (ctx.ID() != null) {
      Expression.Builder obj = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.ID)
        .setId(ctx.ID().getText());
      stackExpr.push(obj);
    } else {
      Expression.Builder obj = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.BUILTIN)
        .setBuiltin(buildin);
      stackExpr.push(obj);
    }
  }

  public void exitReturn_expr(LikelyParser.Return_exprContext ctx) {
    Expression.Builder expr = stackExpr.pop();
    Expression.Builder r = Expression.newBuilder()
      .setTypeCode(Expression.ExpressionType.RETURN_OPERATOR)
      .setReturnOperator(expr);
    stackExpr.push(r);
  }

  public void exitFunc(LikelyParser.FuncContext ctx) {
    if (ctx.ID() != null) {
      Expression.Builder id = Expression.newBuilder()
        .setTypeCode(Expression.ExpressionType.ID)
        .setId(ctx.ID().getText());
      stackExpr.push(id);
    }
  }

  public void exitAttr(LikelyParser.AttrContext ctx) {
    Expression.Builder id = Expression.newBuilder();
    if (ctx.ID() != null) {
      id.setTypeCode(Expression.ExpressionType.ID)
        .setId(ctx.ID().getText());
    }
    Expression.Builder expr = stackExpr.pop();
    Expression.Builder attr = Expression.newBuilder()
      .setTypeCode(Expression.ExpressionType.ATTRIBUTION)
      .setAttribution(
        Attribution.newBuilder()
          .setId(id)
          .setValue(expr));
    stackExpr.push(attr);
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
      buildin.setTypeCode(Builtin.BuiltinType.INTEGER)
             .setInteger(Integer.valueOf(ctx.INTEGER().getText()));
    } else if (ctx.FLOAT() != null) {
      buildin = Builtin.newBuilder();
      buildin.setTypeCode(Builtin.BuiltinType.REAL)
             .setReal(Double.valueOf(ctx.FLOAT().getText()));
    }
  }

  public void exitLiteral(LikelyParser.LiteralContext ctx) {
    if (ctx.STRING() != null) {
      String str = ctx.STRING().getText();
      buildin = Builtin.newBuilder();
      buildin.setTypeCode(Builtin.BuiltinType.STRING)
             .setString(str.substring(1,str.length()-1));
    }
  }

  public void exitBool(LikelyParser.BoolContext ctx) {
    if (ctx.getText().equals("true")) {
      buildin = Builtin.newBuilder()
        .setTypeCode(Builtin.BuiltinType.BOOLEAN)
        .setBoolean(true);
    } else if (ctx.getText().equals("false")) {
      buildin = Builtin.newBuilder()
        .setTypeCode(Builtin.BuiltinType.BOOLEAN)
        .setBoolean(false);
    }
  }

  public Program getProtocolBuffer() {
    return program.build();
  }
}