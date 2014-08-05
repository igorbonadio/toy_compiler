package br.com.igorbonadio.likely.lklc;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class App {
  public static void main( String[] args ) {
    try {
      ANTLRInputStream input = new ANTLRInputStream(System.in);
      LikelyLexer lexer = new LikelyLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      LikelyParser parser = new LikelyParser(tokens);
      ParseTree tree = parser.file_input();
      System.out.println(tree.toStringTree(parser));
    } catch(Exception e) {
      System.out.println("error...");
    }
  }
}
