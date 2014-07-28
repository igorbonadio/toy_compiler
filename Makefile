.PHONY: test
test: LikelyParser.java
	grun Likely r -tree

LikelyParser.java: Likely.g4
	antlr4 Likely.g4
	javac *.java