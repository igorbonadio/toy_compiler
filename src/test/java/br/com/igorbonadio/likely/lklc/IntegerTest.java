package br.com.igorbonadio.likely.lklc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import br.com.igorbonadio.likely.lklast.LikelyAst.*;

public class IntegerTest extends TestCase {

  public IntegerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(IntegerTest.class);
  }

  public void testSingleDigitInteger() {
    assertEquals(1, 1);
  }
}
