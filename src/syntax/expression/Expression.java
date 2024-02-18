package syntax.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import syntax.statement.Function;
import udbparser.expr.NewOperatorInfo;
import udbparser.udbrawdata.UdbLexemeNode;

public class Expression {
	/* field */
	private ArrayList<IdExpression> usedVars = new ArrayList<IdExpression>();

	/*
	 * chl@2023.02.09 - make next expression var to find next line for not branch
	 * but nested
	 */
	private Expression next;

	/* chl@2023.02.08 - visit checking for code print */
	public boolean visit = false;

	protected ArrayList<String> castType = new ArrayList<String>(
			Arrays.asList("char", "short", "int", "long", "float", "enum", "unsigned", "signed"));

	/* method */
	public void addUsedVar(IdExpression usedVar) {
		this.usedVars.add(usedVar);
	}

	public void addUsedVars(ArrayList<IdExpression> usedVars) {
		for (IdExpression idExpression : usedVars)
			this.addUsedVar(idExpression);
	}

	/* getter and setter */
	public ArrayList<IdExpression> getUsedVars() {
		return usedVars;
	}

	public void setUsedVars(ArrayList<IdExpression> usedVars) {
		this.usedVars = usedVars;
	}

	public void setNext(Expression next) {
		this.next = next;
	}

	public Expression getNext() {
		return this.next;
	}

	// chl@2023.02.07 - print template
	public String toString() {
		String tmp = "";
		for (IdExpression i : usedVars) {
			tmp = tmp + i;
		}
		return tmp;
	}

	// chl@2023.02.08 - code print
	public String getCode() {
		/*
		 * For printing each expression class
		 * [From] CodeFromStmtTraverser.java
		 * [Use] Expression expr, expr.getcode()
		 */
		if (this instanceof IdExpression)
			((IdExpression) this).getCode();
		else if (this instanceof ArrayExpression)
			((ArrayExpression) this).getCode();
		else if (this instanceof AtomicExpression)
			((AtomicExpression) this).getCode();
		else if (this instanceof BinaryExpression)
			((BinaryExpression) this).getCode();
		else if (this instanceof BracketExpression)
			((BracketExpression) this).getCode();
		else if (this instanceof CallExpression)
			((CallExpression) this).getCode();
		else if (this instanceof CastExpression)
			((CastExpression) this).getCode();
		else if (this instanceof CompoundExpression)
			((CompoundExpression) this).getCode();
		else if (this instanceof ConditionalExpression)
			((ConditionalExpression) this).getCode();
		else if (this instanceof DeclExpression)
			((DeclExpression) this).getCode();
		else if (this instanceof LibraryCallExpression)
			((LibraryCallExpression) this).getCode();
		else if (this instanceof LiteralExpression)
			((LiteralExpression) this).getCode();
		else if (this instanceof UnaryExpression)
			((UnaryExpression) this).getCode();
		else if (this instanceof UserDefinedCallExpression)
			((UserDefinedCallExpression) this).getCode();
		/*
		 * else if (this instanceof StructExpression)
		 * ((StructExpression)this).getCode();
		 */
		return "";
	}

	/* chl@2023.02.08 - visit checking for code print */
	public void setVisit(boolean v) {
		this.visit = v;
	}

	public boolean getVisit() {
		return this.visit;
	}

	/* chl@2023.03.02 - make expression from lexemes */
	public ArrayList<Expression> makeExprFromLex(ArrayList<UdbLexemeNode> lexemes,
			HashMap<UdbLexemeNode, Integer> theNumberOfFunctionParameter, HashMap<Function, Boolean> typeOfFunc,
			ArrayList<String> defines) {

		/*
		 * [From] UdbLexemeNode lexeme -> save in 'lexemes' list
		 * [To] Expression expr -> save in 'expressions' list
		 */
		ArrayList<Expression> expressions = new ArrayList<Expression>();

		/* For checking binary, unary, Ternery */
		NewOperatorInfo operatorInfo = new NewOperatorInfo();

		/* remove whitespace & unnecessary ; (end of line) */
		for (int i = 0; i < lexemes.size(); i++) {
			if (lexemes.get(i).getToken().equals("Whitespace")) {
				lexemes.remove(lexemes.get(i));
				i--;
			} else if (i == lexemes.size() - 1 && lexemes.get(i).getToken().equals("Punctuation")
					&& lexemes.get(i).getData().equals(";")) {
				lexemes.remove(lexemes.get(i));
				break;
			}
		}

		/*
		 * 1. Assign Expression to each lexeme
		 * 
		 * Ex. lexemes (list) = {int, a, =, 1}
		 * int -> CastExpression
		 * a -> IdExpression
		 * = -> Operator -> Binary Expression
		 * 1 -> Literal Expression
		 * 
		 */
		Boolean typedef = false;

		for (int i = 0; i < lexemes.size(); i++) {
			UdbLexemeNode lexeme = lexemes.get(i);
			/*
			 * 1-1. Identifier(lexeme - Token) handles Call, Type, Declare, Define (lexeme -
			 * Ref_Kind)
			 * Call: Function -> UserdefinedCallExpression or LibraryCallExpression
			 * Type: int, double, float, ... -> CastExpression
			 * Declare, Define -> IdExpression
			 */

			/* typedef exception */
			if (lexeme.getData().equals("typedef")) {
				typedef = true;
			}

			if (lexeme.getToken().equals("Identifier")) {
				/* Call Expression Assignment */
				if (lexeme.getRef_kind().equals("Call")) {
					/* exit */
					if (lexeme.getData().equals("exit")) {
						ControlExpression controlExpression = new ControlExpression();
						controlExpression.setFunctionalName(lexeme.getData());
						expressions.add(controlExpression);
						continue;
					}
					/* define macro function */
					if (defines.contains(lexeme.getData())) {
						DefineExpression callExpression = new DefineExpression();
						callExpression.setFunctionalName(lexeme.getData());
						expressions.add(callExpression);
						continue;
					}
					/*
					 * Call Expression Classification [Use] typeOfFunc<Function, Calltype>
					 * librarycall: {pseudoExpression - nullExpression - pseudoExpression} ->
					 * Calltype: true
					 * userdefinedcall: {diffent for each function} -> Calltype: false
					 * Ex. printf() -> calltype: true
					 * add(int a, int b){return a+b;} & add(a,b) -> calltype: false
					 */
					Boolean calltype = false;
					Function callee = new Function();
					for (Function key : typeOfFunc.keySet()) {
						if (key.getName().equals(lexeme.getData())) {
							callee = key;
							calltype = typeOfFunc.get(key);
						}
					}
					if (calltype == true) { // librarycall
						LibraryCallExpression callExpression = new LibraryCallExpression();
						callExpression.setFunctionalName(lexeme.getData());
						expressions.add(callExpression);
						continue;
					} else if (calltype == false) {// userdefinedcall
						UserDefinedCallExpression callExpression = new UserDefinedCallExpression();
						callExpression.setFunctionalName(lexeme.getData());
						callExpression.setCalleeFunction(callee);
						expressions.add(callExpression);
						continue;
					}
					CallExpression callExpression = new CallExpression();
					callExpression.setFunctionalName(lexeme.getData());
					ArrayList<Expression> paramlist = new ArrayList<Expression>();
					callExpression.setActualParameters(paramlist);
					if (theNumberOfFunctionParameter.keySet().contains(lexeme)) {
						for (int j = 0; j < theNumberOfFunctionParameter.get(lexeme); j++) {
							callExpression.addActualParameters(new Expression());
						}
					}
					expressions.add(callExpression);
				}
				/* Type Ref_Kind -> continue; for const, static,... exception */
				else if (lexeme.getRef_kind().equals("Type")) {
					continue;
				} else if (castType.contains(lexeme.getData())) {
					CastExpression castExpression = new CastExpression(lexeme.getData());
					expressions.add(castExpression);
				}
				/* For IdExpression Assignment */
				else {
					IdExpression idExpression;
					/* lexeme.getVar() -> Variable */
					if (lexeme.getVar() != null && lexeme.getVar().getType().equals("enum") == false) {
						String type = "";
						if (lexeme.getVar().getType().toString().contains("struct")) {
							ArrayList<String> str = new ArrayList<>(
									Arrays.asList("const", "static", "unsigned", "signed", "extern"));
							if (i - 4 >= 0 && (str.contains(lexemes.get(i - 4).getData())
									&& str.contains(lexemes.get(i - 3).getData()))) {
								type += lexemes.get(i - 4).getData() + " ";
								type += lexemes.get(i - 3).getData() + " ";
								lexemes.remove(i - 3);
								lexemes.remove(i - 4);
								i -= 2;
							} else if (i - 3 >= 0 && str.contains(lexemes.get(i - 3).getData())) {
								type += lexemes.get(i - 3).getData() + " ";
								lexemes.remove(i - 3);
								i--;
							}
						}
						if (lexeme.getVar().getType().contains("const")
								&& lexeme.getVar().getType().contains("struct")) {
							lexeme.getVar().setType(lexeme.getVar().getType().replace("const ", ""));
						}
						if (lexeme.getVar().getType().contains("*")) {
							lexeme.getVar().setType(lexeme.getVar().getType().replace(" *", ""));
						}

						if (lexeme.getToken().equals("Identifier") && lexeme.getRef_kind().equals("Declare")
								&& i - 1 >= 0 && lexemes.get(i - 1).getToken().equals("Operator") && i - 2 >= 0
								&& expressions.get(i - 2) instanceof CastExpression) {
							idExpression = new IdExpression(lexeme.getData(), lexeme.getVar().getScope(),
									((CastExpression) expressions.get(i - 2)).getType().toString(), lexeme.getVar());
						} else {
							type += lexeme.getVar().getType();
							idExpression = new IdExpression(lexeme.getVar().getName(), lexeme.getVar().getScope(), type,
									lexeme.getVar());
						}
					} else if (lexeme.getVar() == null && i - 1 >= 0
							&& lexemes.get(i - 1).getToken().equals("Identifier")
							&& lexemes.get(i - 1).getRef_kind().equals("Type")) {
						ArrayList<String> str = new ArrayList<>(
								Arrays.asList("const", "static", "unsigned", "signed", "extern"));
						String type = "";
						if (i - 4 >= 0 && str.contains(lexemes.get(i - 4).getData())
								&& str.contains(lexemes.get(i - 3).getData())
								&& str.contains(lexemes.get(i - 2).getData())) {
							type += lexemes.get(i - 4).getData() + " ";
							type += lexemes.get(i - 3).getData() + " ";
							type += lexemes.get(i - 2).getData() + " ";
							lexemes.remove(i - 2);
							lexemes.remove(i - 3);
							lexemes.remove(i - 4);
							i -= 3;
						} else if (i - 3 >= 0 && str.contains(lexemes.get(i - 3).getData())
								&& str.contains(lexemes.get(i - 2).getData())) {
							type += lexemes.get(i - 3).getData() + " ";
							type += lexemes.get(i - 2).getData() + " ";
							lexemes.remove(i - 2);
							lexemes.remove(i - 3);
							i -= 2;
						} else if (i - 2 >= 0 && str.contains(lexemes.get(i - 2).getData())) {
							type += lexemes.get(i - 2).getData() + " ";
							lexemes.remove(i - 2);
							i -= 1;
						}
						type += lexemes.get(i - 1).getData();
						expressions.remove(expressions.size() - 1);
						idExpression = new IdExpression(lexeme.getData(), null, type, lexeme.getVar());
					} else if (lexeme.getRef_kind().equals("Define") && i - 1 >= 0
							&& (lexemes.get(i - 1).getData().contains("struct")
									|| lexemes.get(i - 1).getData().contains("enum"))) {
						// idExpression = new IdExpression(null, null, null);
						TypedefExpression tdefExpr = new TypedefExpression();

						// typedef exception 2 - if typedef struct or enum has name
						if (typedef == true) {
							tdefExpr.setPrefix("typedef");
							tdefExpr.setType(lexemes.get(i - 1).getData());
							tdefExpr.setName(lexeme.getData());
							tdefExpr.setNick(lexemes.get(lexemes.size() - 1).getData());
							lexemes.remove(lexemes.size() - 1);
						} else {
							tdefExpr.setType(lexemes.get(i - 1).getData());
							tdefExpr.setName(lexeme.getData());
							if (lexemes.get(lexemes.size() - 1).getVar() != null
									&& lexemes.get(lexemes.size() - 1).getVar().getType().equals("enum")) {
								tdefExpr.setNick(lexemes.get(lexemes.size() - 1).getData());
								lexemes.remove(lexemes.size() - 1);
							}
						}
						expressions.add(tdefExpr);
						continue;
					} else if (lexeme.getRef_kind().equals("Define") && typedef == true
							&& (lexemes.get(1).getData().contains("struct")
									|| lexemes.get(1).getData().contains("enum"))) {
						// typedef exception 3 - if typedef struct or enum has no name
						TypedefExpression tdefExpr = new TypedefExpression();
						tdefExpr.setPrefix("typedef");
						tdefExpr.setType(lexemes.get(1).getData());
						tdefExpr.setName("");
						tdefExpr.setNick(lexeme.getData());
						expressions.add(tdefExpr);
						continue;
					} else
						idExpression = new IdExpression(lexeme.getData(), null, null, lexeme.getVar());
					expressions.add(idExpression);
				}
			} /* 1-2. Make BracketExpression for making CompoundExpression */
			else if (lexeme.getToken().equals("Punctuation")) {
				if (lexeme.getData().equals("(")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("()");
					bracketExpression.setPosition(true);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals(")")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("()");
					bracketExpression.setPosition(false);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("{")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("{}");
					bracketExpression.setPosition(true);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("}")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("{}");
					bracketExpression.setPosition(false);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals(";")) { // for forloop
					LiteralExpression literalExpr = new LiteralExpression(";");
					expressions.add(literalExpr);
				}
			} /* 1-3. Operator to Binary, Unary Expression */
			else if (lexeme.getToken().equals("Operator")) {
				/*
				 * bracket expression type, position -> true: beginning, false: end
				 */
				if (lexeme.getData().equals("[")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("[]");
					bracketExpression.setPosition(true);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("]")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("[]");
					bracketExpression.setPosition(false);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("(")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("()");
					bracketExpression.setPosition(true);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals(")")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("()");
					bracketExpression.setPosition(false);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("{")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("{}");
					bracketExpression.setPosition(true);
					expressions.add(bracketExpression);
				} else if (lexeme.getData().equals("}")) {
					BracketExpression bracketExpression = new BracketExpression();
					bracketExpression.setType("{}");
					bracketExpression.setPosition(false);
					expressions.add(bracketExpression);
				} /*
					 * Operator Classification -> Unary Operation, Binary Operation, Ternary
					 * Operation
					 * Ex. a = 1; -> =: BinaryExpression
					 * a -> *: UnaryExpression
					 * b = a * 1 -> *: BinaryExpression
					 */
				else if (operatorInfo.getUnaryOperator().contains(lexemes.get(i).getData())) {
					UnaryExpression unaryExpr = new UnaryExpression();
					unaryExpr.setOperator(lexemes.get(i).getData());
					expressions.add(unaryExpr);
				} else if (operatorInfo.getBinaryOperator().contains(lexemes.get(i).getData())
						|| operatorInfo.getTernaryOperator().contains(lexemes.get(i).getData())) {
					BinaryExpression binaryExpr = new BinaryExpression();
					binaryExpr.setOperator(lexemes.get(i).getData());
					expressions.add(binaryExpr);
				} else if (operatorInfo.getUnclearOperator().contains(lexemes.get(i).getData())) {
					if (operatorInfo.checkIsThisUnary(lexemes, lexemes.get(i), castType)) {
						UnaryExpression unaryExpr = new UnaryExpression();
						unaryExpr.setOperator(lexemes.get(i).getData());
						expressions.add(unaryExpr);
					} else {
						BinaryExpression binaryExpr = new BinaryExpression();
						binaryExpr.setOperator(lexemes.get(i).getData());
						expressions.add(binaryExpr);
					}
				}
			} /* 1-4. Literal (lexeme - Token) -> Literal Expression */
			else if ((lexeme.getToken().equals("Literal")) || (lexeme.getToken().equals("String"))) {
				LiteralExpression literalExpression = new LiteralExpression(lexeme.getData());
				expressions.add(literalExpression);
			} /* 1-5. Keyword (lexeme - Token) -> Expression */
			else if (lexeme.getToken().equals("Keyword")) {
				/* continue, return, goto, break -> Control Expression */
				if (lexeme.getData().equals("continue") || lexeme.getData().equals("return")
						|| lexeme.getData().equals("goto") || lexeme.getData().equals("break")) {
					ControlExpression controlExpression = new ControlExpression();
					controlExpression.setFunctionalName(lexeme.getData());
					expressions.add(controlExpression);
				} /*
					 * (Type) -> Unary Expression
					 * can use Unary Expression for Casting instead of Cast Expression
					 */
				else if (i - 1 >= 0 && lexemes.get(i - 1).getData().equals("(") && i + 1 < lexemes.size()
						&& lexemes.get(i + 1).getData().equals(")")) { // cast to unary
					UnaryExpression unaryExpr = new UnaryExpression();
					unaryExpr.setOperator(
							lexemes.get(i - 1).getData() + lexemes.get(i).getData() + lexemes.get(i + 1).getData());
					expressions.remove(expressions.size() - 1);
					lexemes.remove(i + 1);
					expressions.add(unaryExpr);
				} else if (i - 1 >= 0 && lexemes.get(i - 1).getData().equals("(") && i + 1 < lexemes.size()
						&& lexemes.get(i + 1).getData().equals("*") && i + 2 < lexemes.size()
						&& lexemes.get(i + 2).getData().equals(")")) {
					UnaryExpression unaryExpr = new UnaryExpression();
					unaryExpr.setOperator(lexemes.get(i - 1).getData() + lexemes.get(i).getData()
							+ lexemes.get(i + 1).getData() + lexemes.get(i + 2).getData());
					expressions.remove(expressions.size() - 1);
					lexemes.remove(i + 2);
					lexemes.remove(i + 1);
					expressions.add(unaryExpr);
				} else if (i - 2 >= 0 && lexemes.get(i - 2).getData().equals("(")
						&& lexemes.get(i - 1).getData().equals("*") && i + 1 < lexemes.size()
						&& lexemes.get(i + 1).getData().equals(")")) {
					UnaryExpression unaryExpr = new UnaryExpression();
					unaryExpr.setOperator(lexemes.get(i - 2).getData() + lexemes.get(i - 1).getData()
							+ lexemes.get(i).getData() + lexemes.get(i + 1).getData());
					expressions.remove(expressions.size() - 1);
					expressions.remove(expressions.size() - 1);
					lexemes.remove(i + 1);
					expressions.add(unaryExpr);
				} /* sizeof -> LibraryCallExpression */
				else if (lexeme.getData().equals("sizeof")) {
					LibraryCallExpression callExpression = new LibraryCallExpression();
					callExpression.setFunctionalName(lexeme.getData());
					expressions.add(callExpression);
					continue;
				} /*
					 * const, static, extern, struct, unsigned -> use later for IdExpression's Type
					 */
				else if (lexeme.getData().equals("const") || lexeme.getData().equals("static")
						|| lexeme.getData().equals("extern") || lexeme.getData().contains("struct")
						|| lexeme.getData().contains("enum") || lexeme.getData().equals("unsigned")
						|| lexeme.getData().contains("typedef")) {
					continue;
				} /*
					 * const, static, extern, struct, unsigned -> will be absorbed to the latter
					 * CastExpression
					 */
				else {
					ArrayList<String> str = new ArrayList<>(
							Arrays.asList("const", "static", "unsigned", "signed", "extern"));
					String data = "";
					CastExpression castExpression;
					if (i - 3 >= 0 && str.contains(lexemes.get(i - 3).getData())
							&& str.contains(lexemes.get(i - 2).getData())
							&& str.contains(lexemes.get(i - 1).getData())) {
						data += lexemes.get(i - 3).getData() + " ";
						data += lexemes.get(i - 2).getData() + " ";
						data += lexemes.get(i - 1).getData() + " ";
						lexemes.remove(i - 1);
						lexemes.remove(i - 2);
						lexemes.remove(i - 3);
						i -= 3;
					} else if (i - 2 >= 0 && str.contains(lexemes.get(i - 2).getData())
							&& str.contains(lexemes.get(i - 1).getData())) {
						data += lexemes.get(i - 2).getData() + " ";
						data += lexemes.get(i - 1).getData() + " ";
						lexemes.remove(i - 1);
						lexemes.remove(i - 2);
						i -= 2;
					} else if (i - 1 >= 0 && str.contains(lexemes.get(i - 1).getData())) {
						data += lexemes.get(i - 1).getData() + " ";
						lexemes.remove(i - 1);
						i -= 1;
					}
					data += lexeme.getData();
					castExpression = new CastExpression(data);
					expressions.add(castExpression);
				}
			}
		}

		/*
		 * 2. Merge Expression
		 * 2-1. simple merging - CastExpression: for unary and const, static, ...)
		 * ex. (int) -> unary, static int -> static, int to static int
		 * - IdExpression: set type(CastType)
		 * - LiteralExpression: nothing
		 * - BracketExpression: for { , , ...} declaration
		 * ex. {, 1, 2, 3, } -> {1, 2, 3}
		 * for Array
		 * ex. array[3] -> array: ArrayExpression
		 * - CallExpression: exit, continue, break exception (no parameter)
		 * 2-2. find BracketExpression from expressions and make them compoundExpression
		 * ex. (, a, b, c, ) = bracketExpression, idExpression, idExpression,
		 * idExpression, bracketExpression -> CompoundExpression
		 * (in CompoundExpression, expressions = {bracketExpression, idExpression,
		 * idExpression, idExpression, bracketExpression})
		 * 2-3. ArrayExpression Index merging
		 * ex. arrayExpression, compoundExpression(include []),
		 * compoundExpression(include []) -> arrayExpression (in arrayExpression,
		 * indexexpression = {compoundExpression, compoundExpression})
		 * 2-4. malloc library call Exception (has 2 parameter before and after)
		 * 2-5. CallExpression merging
		 * ex. CallExpression, compoundExpression(include ()) -> CallExpression (in
		 * callExpression, actualParameters = {compoundExpression})
		 * 2-6. Unary, Binary merging
		 * ex. !, k = unaryExpression, idExpression -> UnaryExpression (operator: !,
		 * operand: k, position: true)
		 * ex. a, =, b = idExpression, BinaryExpression, idExpression ->
		 * BinaryExpression (operator: =, lhsOperand: a, rhsOperand: b)
		 * +) cast expression -> no longer use -> remove from expressions
		 * 2-7. return library call Exception (none parameters or has parameters)
		 */

		/* 2-1. simple merging */
		for (int i = 0; i < expressions.size();) {
			if (expressions.get(i) instanceof CastExpression) {
				while (i - 1 >= 0 && expressions.get(i - 1) instanceof CastExpression) {
					((CastExpression) expressions.get(i))
							.setType(((CastExpression) expressions.get(i - 1)).getType().toString() + " "
									+ ((CastExpression) expressions.get(i)).getType().toString());
					expressions.remove(i - 1);
					i--;
				}
				if (i - 1 >= 0 && i + 1 < expressions.size() && expressions.get(i - 1) instanceof BracketExpression
						&& expressions.get(i + 1) instanceof BracketExpression) {
					UnaryExpression unaryExpr = new UnaryExpression();
					unaryExpr.setPosition(true);
					unaryExpr.setOperator("(" + ((CastExpression) expressions.get(i)).getType().toString() + ")");
					expressions.remove(i);
					expressions.add(i, unaryExpr);
					expressions.remove(i + 1);
					expressions.remove(i - 1);
					i--;
				}
				i++;
			} else if (expressions.get(i) instanceof IdExpression) {
				expressions.get(i).addUsedVar((IdExpression) expressions.get(i));
				if (i - 1 >= 0 && expressions.get(i - 1) instanceof CastExpression
						&& (((CastExpression) expressions.get(i - 1)).getType().toString().contains("const")
								|| ((CastExpression) expressions.get(i - 1)).getType().toString().contains("static"))) {
					((IdExpression) expressions.get(i))
							.setType(((CastExpression) expressions.get(i - 1)).getType().toString());
				}
				i++;
			} else if (expressions.get(i) instanceof LiteralExpression) {
				i++;
			} else if (expressions.get(i) instanceof BracketExpression) {
				if (((BracketExpression) expressions.get(i)).getType().equals("{}")
						&& ((BracketExpression) expressions.get(i)).getPosition() == true) {
					HashMap<Integer, Integer> allcurlyBrackets = findBrackets(expressions, "curly");
					for (int key : allcurlyBrackets.keySet()) {
						CompoundExpression compoundExpr = new CompoundExpression();
						for (int j = key; j <= allcurlyBrackets.get(key); j++) {
							compoundExpr.AddExpression(expressions.get((int) key));
							expressions.remove((int) key);
						}
						expressions.add(key, compoundExpr);
					}
				} else if (((BracketExpression) expressions.get(i)).getType().equals("[]")
						&& ((BracketExpression) expressions.get(i)).getPosition() == true) {
					if (expressions.get(i - 1) instanceof IdExpression) {
						ArrayExpression arrayExpr = new ArrayExpression((IdExpression) expressions.get(i - 1));
						expressions.remove(i - 1);
						expressions.add(i - 1, arrayExpr);
					}
				}
				i++;
			} else if (expressions.get(i) instanceof CallExpression) {
				ArrayList<Expression> paramList = new ArrayList<Expression>();
				// only control expression(continue, break, exit) of call expression handling
				if (expressions.get(i) instanceof ControlExpression) {
					if (((ControlExpression) expressions.get(i)).getFunctionalName().equals("continue")
							|| ((ControlExpression) expressions.get(i)).getFunctionalName().equals("break")) {
						paramList.add(new NullExpression());
						((CallExpression) expressions.get(i)).setActualParameters(paramList);
						i++;
						continue;
					}
					if (((ControlExpression) expressions.get(i)).getFunctionalName().equals("exit")) {
						i++;
						continue;
					}
				}
				i++;
			} else {
				i++;
			}
		}

		/*
		 * 2-2. find BracketExpression from expressions and make them compoundExpression
		 */
		LinkedHashMap<Integer, Integer> allroundBrackets = findBrackets(expressions, "all");
		while (allroundBrackets.size() != 0) {
			HashMap.Entry<Integer, Integer> entry = allroundBrackets.entrySet().iterator().next();
			int key = entry.getKey();
			CompoundExpression compoundExpr = new CompoundExpression();
			for (int j = key; j <= allroundBrackets.get(key); j++) {
				compoundExpr.AddExpression(expressions.get((int) key));
				expressions.remove((int) key);
			}
			expressions.add(key, compoundExpr);
			allroundBrackets.clear();
			allroundBrackets = findBrackets(expressions, "all");
		}

		/* 2-3. ArrayExpression Index merging */
		expressions = mergeIndex(expressions);

		/* 2-4. malloc library call Exception (has 2 parameter before and after) */
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof LibraryCallExpression) {
				if (((LibraryCallExpression) expressions.get(i)).getFunctionalName().equals("malloc")) {
					CompoundExpression compExpr = new CompoundExpression();
					compExpr.AddExpression(expressions.get(i - 1));
					((CompoundExpression) expressions.get(i + 1))
							.setExpression(mergeCall(((CompoundExpression) expressions.get(i + 1)).getExpression()));
					((CompoundExpression) expressions.get(i + 1)).setExpression(
							mergeOperator(((CompoundExpression) expressions.get(i + 1)).getExpression()));
					((LibraryCallExpression) expressions.get(i)).addActualParameters(expressions.get(i + 1));
					compExpr.AddExpression(expressions.get(i));
					expressions.remove(i + 1);
					expressions.remove(i);
					expressions.remove(i - 1);
					expressions.add(compExpr);
					continue;
				}
			}
		}

		/* 2-5. CallExpression merging */
		expressions = mergeCall(expressions);

		/* 2-6. Unary, Binary merging */
		expressions = mergeOperator(expressions);

		/* 2-7. return library call Exception (none parameters or has parameters) */
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof CallExpression
					&& ((CallExpression) expressions.get(i)).getFunctionalName().equals("return")) {
				while (i + 1 < expressions.size()) {
					((CallExpression) expressions.get(i)).addActualParameters(expressions.get(i + 1));
					expressions.remove(i + 1);
				}
			}
		}

		return expressions;

	}

	/*
	 * 2-3. ArrayExpression Index merging
	 * ex. arrayExpression, compoundExpression(include []),
	 * compoundExpression(include []) -> arrayExpression (in arrayExpression,
	 * indexexpression = {compoundExpression, compoundExpression})
	 */
	public ArrayList<Expression> mergeIndex(ArrayList<Expression> expressions) {

		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof ArrayExpression) {
				while (i + 1 < expressions.size() && expressions.get(i + 1) instanceof CompoundExpression
						&& ((CompoundExpression) expressions.get(i + 1)).getExpression().isEmpty() == false) {
					if (((CompoundExpression) expressions.get(i + 1)).getExpression()
							.get(0) instanceof BracketExpression
							&& ((BracketExpression) ((CompoundExpression) expressions.get(i + 1)).getExpression()
									.get(0)).getType().equals("[]")) {
						((ArrayExpression) expressions.get(i)).addIndexExpression(expressions.get(i + 1));
						expressions.remove(i + 1);
					}
				}
			} else if (expressions.get(i) instanceof CompoundExpression) {
				mergeIndex(((CompoundExpression) expressions.get(i)).getExpression());
			}
		}

		return expressions;
	}

	/*
	 * 2-5. CallExpression merging
	 * ex. CallExpression, compoundExpression(include ()) -> CallExpression (in
	 * callExpression, actualParameters = {compoundExpression})
	 */
	public ArrayList<Expression> mergeCall(ArrayList<Expression> expressions) {
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof CallExpression) {
				if (((CallExpression) expressions.get(i)).getActualParameters().isEmpty()) {
					if (((CallExpression) expressions.get(i)).getFunctionalName().equals("return")) {
						continue;
					} else if (i + 1 < expressions.size() && expressions.get(i + 1) instanceof CompoundExpression
							&& ((CompoundExpression) expressions.get(i + 1)).getExpression().size() != 0
							&& ((CompoundExpression) expressions.get(i + 1)).getExpression()
									.get(0) instanceof BracketExpression) {
						// ((CompoundExpression)expressions.get(i+1)).setExpression(mergeOperator(((CompoundExpression)expressions.get(i+1)).getExpression()));
						((CompoundExpression) expressions.get(i + 1)).setExpression(
								mergeCall(((CompoundExpression) expressions.get(i + 1)).getExpression()));
						((CallExpression) expressions.get(i)).addActualParameters(expressions.get(i + 1));
						expressions.remove(i + 1);
					} else if (((CallExpression) expressions.get(i)).getFunctionalName().equals("sizeof")) {
						((CallExpression) expressions.get(i)).addActualParameters(expressions.get(i + 1));
						expressions.remove(i + 1);
					}
				} else if (((CallExpression) expressions.get(i)).getActualParameters()
						.get(0) instanceof CompoundExpression) {
					mergeCall(((CompoundExpression) ((CallExpression) expressions.get(i)).getActualParameters().get(0))
							.getExpression());
				}
			} else if (expressions.get(i) instanceof BinaryExpression) {
				if (((BinaryExpression) expressions.get(i)).getLhsOperand() instanceof CompoundExpression) {
					mergeCall(((CompoundExpression) ((BinaryExpression) expressions.get(i)).getLhsOperand())
							.getExpression());
				}
				if (((BinaryExpression) expressions.get(i)).getRhsOperand() instanceof CompoundExpression) {
					mergeCall(((CompoundExpression) ((BinaryExpression) expressions.get(i)).getRhsOperand())
							.getExpression());
				}
			} else if (expressions.get(i) instanceof CompoundExpression) {
				if (expressions.get(i) instanceof CallExpression
						&& ((CallExpression) expressions.get(i)).getFunctionalName().equals("return")) {
					continue;
				}
				mergeCall(((CompoundExpression) expressions.get(i)).getExpression());
			} else if (expressions.get(i) instanceof TypedefExpression) {
				if (i - 1 >= 0 && expressions.get(i - 1) instanceof CompoundExpression
						&& ((CompoundExpression) expressions.get(i - 1)).getExpression().size() != 0
						&& ((CompoundExpression) expressions.get(i - 1)).getExpression()
								.get(0) instanceof BracketExpression) {// typedef exception 4 - only has nick name
					if (((TypedefExpression) expressions.get(i)).getType().contains("enum")) {
						ArrayList<Expression> tmp = new ArrayList<>();
						tmp.add(((CompoundExpression) expressions.get(i - 1)));
						((TypedefExpression) expressions.get(i)).setExpressions(tmp);
					} else
						((TypedefExpression) expressions.get(i))
								.setExpressions(((CompoundExpression) expressions.get(i - 1)).getExpression());
					expressions.remove(i - 1);
				}
				if (i + 1 < expressions.size() && expressions.get(i + 1) instanceof CompoundExpression
						&& ((CompoundExpression) expressions.get(i + 1)).getExpression().size() != 0
						&& ((CompoundExpression) expressions.get(i + 1)).getExpression()
								.get(0) instanceof BracketExpression) {
					if (((TypedefExpression) expressions.get(i)).getType().contains("enum")) {
						ArrayList<Expression> tmp = new ArrayList<>();
						tmp.add(((CompoundExpression) expressions.get(i + 1)));
						((TypedefExpression) expressions.get(i)).setExpressions(tmp);
					} else
						((TypedefExpression) expressions.get(i))
								.setExpressions(((CompoundExpression) expressions.get(i + 1)).getExpression());
					expressions.remove(i + 1);
					continue;
				}
				if (i + 1 < expressions.size() && expressions.get(i + 1) instanceof CompoundExpression == false
						&& ((TypedefExpression) expressions.get(i)).getType().contains("enum")) {
					i = i + 1;
					while (i < expressions.size()) {
						expressions.remove(i);
					}
				}
			}

		}

		return expressions;
	}

	/*
	 * 2-6. Unary, Binary merging
	 * ex. !, k = unaryExpression, idExpression -> UnaryExpression (operator: !,
	 * operand: k, position: true)
	 * ex. a, =, b = idExpression, BinaryExpression, idExpression ->
	 * BinaryExpression (operator: =, lhsOperand: a, rhsOperand: b)
	 * +) cast expression -> no longer use -> remove from expressions
	 */
	public ArrayList<Expression> mergeOperator(ArrayList<Expression> expressions) {
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof CastExpression) {
				if (i + 4 < expressions.size() && expressions.get(i + 4) instanceof LiteralExpression
						&& ((LiteralExpression) expressions.get(i + 4)).getConstant().equals(";")) {
					continue;
				} else if (expressions.get(i + 1) instanceof UnaryExpression) {
					if (i == 0 && i + 2 < expressions.size() && expressions.get(i + 2) instanceof ArrayExpression) {
						expressions.remove(i);
						i--;
					} else
						continue;
				} else {
					expressions.remove(i);
					i--;
				}
			} else if (expressions.get(i) instanceof CallExpression) {
				if (((CallExpression) expressions.get(i)).getFunctionalName().equals("sizeof")
						|| ((CallExpression) expressions.get(i)).getFunctionalName().equals("malloc"))
					continue;
				else
					mergeOperator(((CallExpression) expressions.get(i)).getActualParameters());
			}
			/* unary Expression handling */
			else if (expressions.get(i) instanceof UnaryExpression) {
				if (i - 1 >= 0 && expressions.get(i - 1) instanceof CastExpression
						&& ((UnaryExpression) expressions.get(i)).getOperator().toString().equals("*")
						&& i + 1 < expressions.size() && expressions.get(i + 1) instanceof IdExpression) {
					Expression operand = expressions.remove(i + 1);
					((UnaryExpression) expressions.get(i)).setOperand(operand);
					((UnaryExpression) expressions.get(i)).setPosition(true);
					expressions.remove(i - 1);
					i--;
				} else if (i - 2 >= 0 && expressions.get(i - 2) instanceof BracketExpression && i - 1 >= 0
						&& expressions.get(i - 1) instanceof CastExpression
						&& ((UnaryExpression) expressions.get(i)).getOperator().toString().equals("*")
						&& i + 1 < expressions.size() && expressions.get(i + 1) instanceof BracketExpression) {
					((UnaryExpression) expressions.get(i)).setOperand(new NullExpression());
					((UnaryExpression) expressions.get(i)).setPosition(true);
					expressions.remove(i - 1);
					i--;
				} else if (i + 1 < expressions.size() && expressions.get(i + 1) instanceof BinaryExpression) {
					continue;
				} else if (i - 1 < 0 || (expressions.get(i - 1) instanceof IdExpression == false
						&& expressions.get(i - 1) instanceof ArrayExpression == false)) {
					Expression operand = expressions.remove(i + 1);
					if (operand instanceof CompoundExpression && operand instanceof BinaryExpression == false
							&& operand instanceof UnaryExpression == false) {
						mergeOperator(((CompoundExpression) operand).getExpression());
					}
					((UnaryExpression) expressions.get(i)).setOperand(operand);
					((UnaryExpression) expressions.get(i)).setPosition(true);
					expressions.get(i).addUsedVars(operand.getUsedVars());
					i++;
				} else {
					Expression operand = expressions.remove(i - 1);
					if (operand instanceof CompoundExpression && operand instanceof BinaryExpression == false
							&& operand instanceof UnaryExpression == false) {
						mergeOperator(((CompoundExpression) operand).getExpression());
					}
					((UnaryExpression) expressions.get(i - 1)).setOperand(operand);
					((UnaryExpression) expressions.get(i - 1)).setPosition(false);
					expressions.get(i - 1).addUsedVars(operand.getUsedVars());
				}
			} else if (expressions.get(i) instanceof CompoundExpression) {
				mergeOperator(((CompoundExpression) expressions.get(i)).getExpression());
			} else if (expressions.get(i) instanceof TypedefExpression) {
				mergeOperator(((TypedefExpression) expressions.get(i)).getExpressions());
			}
		}

		/* binaryExpression hadndling */
		/* find priority from binary */
		ArrayList<Expression> pri = new ArrayList<Expression>();
		ArrayList<Expression> lowpri = new ArrayList<Expression>();
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i) instanceof BinaryExpression) {
				if (((BinaryExpression) expressions.get(i)).getOperator().getOperator().equals("*")
						|| ((BinaryExpression) expressions.get(i)).getOperator().getOperator().equals("/")
						|| ((BinaryExpression) expressions.get(i)).getOperator().getOperator().equals("%")) {
					pri.add(expressions.get(i));
				} else {
					lowpri.add(expressions.get(i));
				}
			}
		}
		pri.addAll(lowpri);

		/* binary Expression merging */
		for (Expression e : pri) {
			for (int i = 0; i < expressions.size(); i++) {
				if (expressions.get(i) == e) {
					Expression lhsOperand = expressions.remove(i - 1);
					Expression rhsOperand = expressions.remove(i);
					((BinaryExpression) expressions.get(i - 1)).setRhsOperand(rhsOperand);
					expressions.get(i - 1).addUsedVars(rhsOperand.getUsedVars());
					((BinaryExpression) expressions.get(i - 1)).setLhsOperand(lhsOperand);
					expressions.get(i - 1).addUsedVars(lhsOperand.getUsedVars());
					break;
				}
			}
		}

		return expressions;
	}

	/*
	 * find Bracket from expressions
	 * 1. find beginning bracket and add to stack (brackets)
	 * 2. if find end bracket -> brakcet from brackets stack pop + end bracket , add
	 * to allBrackets(LinkedHashMap<beginning, end>)
	 * +) all: () and []
	 * curly: {}
	 */
	public LinkedHashMap<Integer, Integer> findBrackets(ArrayList<Expression> expressions, String type) {
		Stack<Integer> brackets = new Stack<Integer>();
		LinkedHashMap<Integer, Integer> allBrackets = new LinkedHashMap<Integer, Integer>();
		if (type.equals("all")) { // ()
			for (int i = 0; i < expressions.size(); i++) {
				if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("()")
						&& ((BracketExpression) expressions.get(i)).getPosition() == true) {
					brackets.add(i);
				} else if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("()")
						&& ((BracketExpression) expressions.get(i)).getPosition() == false) {
					allBrackets.put(brackets.pop(), i);
				} else if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("[]")
						&& ((BracketExpression) expressions.get(i)).getPosition() == true) {
					brackets.add(i);
				} else if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("[]")
						&& ((BracketExpression) expressions.get(i)).getPosition() == false) {
					allBrackets.put(brackets.pop(), i);
				}
			}
		} else if (type.equals("curly")) { // {}
			for (int i = 0; i < expressions.size(); i++) {
				if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("{}")
						&& ((BracketExpression) expressions.get(i)).getPosition() == true) {
					brackets.add(i);
				} else if (expressions.get(i) instanceof BracketExpression
						&& ((BracketExpression) expressions.get(i)).getType().equals("{}")
						&& ((BracketExpression) expressions.get(i)).getPosition() == false) {
					allBrackets.put(brackets.pop(), i);
				}
			}
		}
		return allBrackets;
	}

}
