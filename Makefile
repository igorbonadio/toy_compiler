.PHONY: test
test: LikelyParser.java
	grun Likely stmts -tree -gui

LikelyParser.java: Likely.g4
	antlr4 Likely.g4
	javac *.java