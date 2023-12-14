package udbparser.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import udbparser.udbrawdata.UdbLexemeNode;

public class NewOperatorInfo {
	/* field */
	private ArrayList<String> binaryOperator = new ArrayList<String>(Arrays.asList("=", "==", "+=", "-=", "<", ">",
			"->", "<=", ">=", "/", ".", "|", "^", "%", "!=", "&&", "||", "*=", "/=", "<<=", ">>=", "<<", ">>", ":"));

	private ArrayList<String> unaryOperator = new ArrayList<String>(Arrays.asList("++", "--", "!", "~"));

	private ArrayList<String> ternaryOperator = new ArrayList<String>(Arrays.asList("?"));

	private ArrayList<String> unclearOperator = new ArrayList<String>(Arrays.asList("+", "-", "*", "&"));

	private ArrayList<String> bracketOperator = new ArrayList<String>(Arrays.asList("(", ")", "{", "}", "[", "]"));

	@SuppressWarnings("serial")
	private HashMap<String, Integer> operatorValue = new HashMap<String, Integer>() {
		{
			put("(", 0);
			put("{", 0);
			put("[", 0);
			put(")", 0);
			put("}", 0);
			put("]", 0);

			put("=", 50);
			put("==", 50);
			put("!=", 50);
			put("+=", 50);
			put("-=", 50);
			put("*=", 50);
			put("/=", 50);
			put("<=", 50);
			put(">=", 50);
			put("<<=", 50);
			put(">>=", 50);
			put("&&", 50);
			put("||", 50);

			put("?", 70);

			put("+", 100);
			put("-", 100);
			put("<", 100);
			put(">", 100);
			put("|", 100);
			put("^", 100);
			put("->", 100);
			put(".", 100);
			put("%", 100);
			put("<<", 100);
			put(">>", 100);
			put(":", 100);

			/*
			 * ���� unclear�� ���� operator�鿡 ���Ͽ�, unary�� ���� binary�� ����� value���� ���� �ٸ��� �ϴ� ����
			 * �����غ���.
			 */
			put("*", 150);

			/* �Ʒ��� �͵��� �� �����غ� ���� value�� */
			put("/", 150);

			/* ������ ++�� --�� ���� ������ �޴� ������ ref_kind�� Modify�� ǥ�� �� */
			put("++", 100);
			put("--", 100);
			put("!", 100);
			put("~", 100);
			put("&", 100);

		}
	};

	/* method */
	public boolean checkIsThisUnary(ArrayList<UdbLexemeNode> lexemes, UdbLexemeNode lexeme,
			ArrayList<String> castType) {
		int index = lexemes.indexOf(lexeme);
		if (lexeme.getData().equals("+")) {
			for (int i = index - 1; 0 <= i; i--) {
				if (lexemes.get(i).getToken().equals("Operator"))
					if (lexemes.get(i).getData().equals("]"))
						return false;
					else if (lexemes.get(i).getData().equals(","))
						continue;
					else
						return true;
				else if (lexemes.get(i).getToken().equals("Whitespace"))
					continue;
				else if (lexemes.get(i).getToken().equals("Literal"))
					return false;
				else if (lexemes.get(i).getToken().equals("Identifier")) {
					return false;
				}
			}
		} else if (lexeme.getData().equals("-")) {
			for (int i = index - 1; 0 <= i; i--) {
				if (lexemes.get(i).getToken().equals("Operator"))
					if (lexemes.get(i).getData().equals("]"))
						return false;
					else if (lexemes.get(i).getData().equals(","))
						continue;
					else
						return true;
				else if (lexemes.get(i).getToken().equals("Whitespace"))
					continue;
				else if (lexemes.get(i).getToken().equals("Literal"))
					return false;
				else if (lexemes.get(i).getToken().equals("Identifier")) {
					return false;
				}
			}
		} else if (lexeme.getData().equals("*")) {
			for (int i = index + 1; i < lexemes.size(); i++) {
				if (lexemes.get(i).getToken().equals("Identifier")) {
					if (lexemes.get(i).getRef_kind().equals("Deref Use"))
						return true;
					else
						break;
				}
			}
			//chl@2023.02.28 - pointer check
			if (lexemes.get(index+1).getToken().equals("Identifier")){
				for (int i = index - 1; 0<=i ; i--){
					if (lexemes.get(i).getToken().equals("Whitespace")) continue;
					else if (lexemes.get(i).getToken().equals("Operator")) 
						break;
					else if (lexemes.get(i).getToken().equals("Keyword"))
						return true;
					else if (lexemes.get(i).getToken().equals("Identifier") && lexemes.get(i).getRef_kind().equals("Type"))
						return true;
					else 
						return false;
				}
				for (int i = index+2; i<lexemes.size() ; i++){
					if (lexemes.get(i).getToken().equals("Whitespace")) continue;
					else if (lexemes.get(i).getToken().equals("Operator") || lexemes.get(i).getToken().equals("Punctuation") ) 
						return true;
					else 
						return false;
				}
			}
			if (index+2<lexemes.size() && lexemes.get(index+2).getData().equals("malloc")){
				return true;
			}
			/*for (int i = index - 1; 0 <= i; i--) {
				if (lexemes.get(i).getToken().equals("Whitespace"))
					continue;
				if (castType.contains(lexemes.get(i).getData()))
					return true;
			}*/
			return false;
		} else if (lexeme.getData().equals("&")) {
			for (int i = index + 1; i < lexemes.size(); i++) {
				if (lexemes.get(i).getToken().equals("Identifier")) {
					if (lexemes.get(i).getRef_kind().equals("Addr Use"))
						return true;
					else
						break;
				}
			}
			return false;
		}
		return true;
	}

	/* getter and setter */
	public ArrayList<String> getBinaryOperator() {
		return binaryOperator;
	}

	public ArrayList<String> getUnaryOperator() {
		return unaryOperator;
	}

	public ArrayList<String> getTernaryOperator() {
		return ternaryOperator;
	}

	public ArrayList<String> getUnclearOperator() {
		return unclearOperator;
	}

	public ArrayList<String> getBracketOperator() {
		return bracketOperator;
	}

	public HashMap<String, Integer> getOperatorValue() {
		return operatorValue;
	}
}
