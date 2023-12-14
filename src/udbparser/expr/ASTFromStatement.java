// package udbparser.expr;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Stack;

// import syntax.expression.ArrayExpression;
// import syntax.expression.BinaryExpression;
// import syntax.expression.BracketExpression;
// import syntax.expression.CallExpression;
// import syntax.expression.CastExpression;
// import syntax.expression.ConditionalExpression;
// import syntax.expression.DeclExpression;
// import syntax.expression.EmptyExpression;
// import syntax.expression.Expression;
// import syntax.expression.IdExpression;
// import syntax.expression.LibraryCallExpression;
// import syntax.expression.LiteralExpression;
// import syntax.expression.NullExpression;
// import syntax.expression.Type;
// import syntax.expression.UnaryExpression;
// import syntax.statement.BranchStatement;
// import syntax.statement.CaseStatement;
// import syntax.statement.CompoundStatement;
// import syntax.statement.ContinueStatement;
// import syntax.statement.DeclarationStatement;
// import syntax.statement.DoStatement;
// import syntax.statement.ExitStatement;
// import syntax.statement.ExpressionStatement;
// import syntax.statement.ForStatement;
// import syntax.statement.Function;
// import syntax.statement.IfStatement;
// import syntax.statement.LabelStatement;
// import syntax.statement.LoopStatement;
// import syntax.statement.NullStatement;
// import syntax.statement.ReturnStatement;
// import syntax.statement.Statement;
// import udbparser.udbrawdata.UdbLexemeNode;

// public class ASTFromStatement {

// 	/* filed */
// 	NewOperatorInfo operatorInfo = new NewOperatorInfo();
// 	private ArrayList<UdbLexemeNode> originalLexemes;
// 	private ArrayList<UdbLexemeNode> postfix;
// 	private ArrayList<Expression> expressions;
// 	private HashMap<UdbLexemeNode, String> operatorKind;
// 	private ArrayList<String> castType;
// 	private Statement stmt;
// 	//private int numOfparams;
// 	// TestCFG�� function���κ��� �� function�� parameter���� �˾Ƴ����� ���� ��� �����ų �� ������ �����غ���
// 	/* call�Ǵ� �Լ��� parameter ���� Understand�κ��� �� �� �ִ��� Ȯ�� ��, �Ʒ��� ������ ������ ��� */
// 	private HashMap<UdbLexemeNode, Integer> theNumberOfFunctionParameter;
// 	private HashMap<Function, Integer> NumOfParam;

// 	/*chl@2023.02.09 - check var of compoundstatement*/
// 	private boolean cmpdchk = false;
	
// 	/*
// 	 * <Algorithm>
// 	 * makeASTFromLexemeOfStatement(statement, numPara)
// 	 * 1. numPara : function -> num // the hashmap to find the number of parameters of some function
// 	 * 2. lexeme_Sequence <- proprecessing_Lexemes(statement)
// 	 * 3. lexeme_Sequence <- change_Postfix(lexeme_Sequence)
// 	 * 4. expressionList <- make_Expression_From_Lexeme(lexeme_Sequence)
// 	 * 5. AST <- merge_Expression(expressionList)
// 	 * 6. return AST
// 	 * */
	
	
// 	/* method */
// 	/*
// 	 * 1. ��ó���� ���� ast�� �����ϴµ� ���ʿ��� lexeme ����
// 	 * 2. ��ó���� lexeme list�� post fixȭ
// 	 * 3. post fix�� lexeme list�� expression���� ����
// 	 * 4. expression���� ast�� ����
// 	 * */
// 	public Statement makeASTFromLexOfStmt(Statement stmt, HashMap<Function, Integer> numOfParam) {
// 		/* ���α׷����� ���ǵ� ����ü Ÿ�Ե��� �ٸ��� castType�� ���� ���ִ� �ɷ� */
// 		// ������ castType�� ����ü type���� �߰��ؾ��ϱ� ������, �������� ��.(20210729)
// 		this.castType = new ArrayList<String>(Arrays.asList("char", "short", "int", "long", "float", "enum", "unsigned", "signed"));
// 		this.stmt = stmt;
// 		operatorKind = new HashMap<UdbLexemeNode, String>();
// 		this.originalLexemes = stmt.getRawData();
// 		this.theNumberOfFunctionParameter = new HashMap<UdbLexemeNode, Integer>();
// 		this.NumOfParam = numOfParam;
// 		preprocessingLexemes(stmt);
		
// 		//System.out.println(theNumberOfFunctionParameter);
		
// 		/* for the debug of preprocessing */
// 		/*System.out.print("\tpreprocessing : ");
// 		for (UdbLexemeNode lex : this.originalLexemes) {
// 			System.out.print(lex.getData());
// 		}
// 		System.out.println();*/
		
// 		this.postfix = new ArrayList<UdbLexemeNode>();
// 		changePostfix(originalLexemes);
		
// 		/* for the debug of postfix */
// 		/*System.out.print("\tpostfix : ");
// 		for (UdbLexemeNode lex : this.postfix) {
// 			System.out.print(lex.getData());
// 		}
// 		System.out.println();*/
		
// //		for(UdbLexemeNode lex : originalLexemes) {
// //			System.out.println(lex.getVar());
// //		}
		
// 		if (stmt instanceof IfStatement) {
// 			return makeIfStatementAST((IfStatement) stmt);
// 		} else if (stmt instanceof ForStatement) {
// 			return makeForStatementAST((ForStatement) stmt);
// 		} else if (stmt instanceof DoStatement) {
// 			return makeDoStatementAST((DoStatement) stmt);
// 		} else if (stmt instanceof LoopStatement) {
// 			return makeLoopStatementAST((LoopStatement) stmt);
// 		} else if (stmt instanceof ExpressionStatement) {
// 			return makeExpressionStatementAST((ExpressionStatement) stmt);
// 		} else if (stmt instanceof DeclarationStatement) {
// 			return makeDeclarationStatementAST((DeclarationStatement) stmt);
// 		} else if (stmt instanceof ExitStatement) {
// 			return makeExitStatementAST((ExitStatement) stmt);
// 		} else if (stmt instanceof CaseStatement) {
// 			return makeCaseStatementAST((CaseStatement) stmt);
// 		} 
// 		// Return Statement�� ���� ���׵� ���Ǹ� ����� ��.
// 		// 20210802
// 		else if(stmt instanceof ReturnStatement) {
// 			return makeReturnStatementAST((ReturnStatement) stmt);
// 		} else if (stmt instanceof CompoundStatement) {/*chl@2023.02.08 - nested handling*/
// 			return makeCompoundStatementAST((CompoundStatement) stmt);
// 		} else if (stmt instanceof ContinueStatement) { /*@chl-2023.02.09 - need to add other statement*/
// 			return makeContinueStatementAST((ContinueStatement) stmt);
// 		}
// 		else {
// 			return stmt;
// 		}
// 	}


// 	public void preprocessingLexemes(Statement stmt) {
// 		ArrayList<UdbLexemeNode> newLexemes = new ArrayList<UdbLexemeNode>();
// 		UdbLexemeNode beforeLexeme = new UdbLexemeNode();
// 		// ���� �����͸� ó���ϱ� ���ؼ� CallStack�� �����(20210804)
// 		Stack<UdbLexemeNode> CallStack = new Stack<UdbLexemeNode>();
// 		int checkDecl = 0;
// 		if (stmt instanceof DeclarationStatement)
// 			checkDecl = 1;
// 		for (UdbLexemeNode lexeme : originalLexemes) {
// 			//System.out.println("lexeme : " + lexeme);
// 			if (lexeme.getToken().equals("Keyword")) {
// 				if (checkDecl == 1)
// 					continue;
// 			} else if (lexeme.getToken().equals("Whitespace")) {
// 				continue;
// 			} else if (lexeme.getRef_kind().equals("Type")) {
// 				beforeLexeme = lexeme;
// 				continue;
// 			}
// 			/* Classify all operator in list as unary, binary, or ternary */
// 			else if (lexeme.getToken().equals("Operator")) {
// 				//System.out.println(castType);
// 				if (lexeme.getData().equals(":") && stmt instanceof CaseStatement)
// 					continue;
// 				// castType �ڿ� *�� ������ pointer�� ����ϰ� �����Ѵ�.(20210730)
// 				else if (lexeme.getData().indexOf("*") == 0 && beforeLexeme.getRef_kind().equals("Cast"))
// 					continue;
// 				// Type �ڿ� *�� ������ pointer�� ����ϰ� �����Ѵ�.(20210730)
// 				else if (lexeme.getData().indexOf("*") == 0 && beforeLexeme.getRef_kind().equals("Type"))
// 					continue;
// 				// ���� ������ ������ �ذ��ϱ� ���ؼ� �켱 ������ ���� �����س���.(20210804)
// 				// CastType�� postfix ������ �����ؾ� preprocessing���� pointer�� ó���� �� ����...
// 				// �ش� ����� ����غ� ��!
// 				else if (lexeme.getData().indexOf("*") == 0 && CallStack.empty() == false && CallStack.peek().getData().equals("malloc")) {
// 					System.out.println("Multiple Pointer : " + lexeme);
// 					System.out.println("Multiple Before Lexeme : " + beforeLexeme);
// 					continue;
// 				}
// 				defineOperatorKind(lexeme);
// 			} else if (lexeme.getToken().equals("Identifier")) {
// 				if (checkDecl == 1 && (lexeme.getRef_kind().equals("Init") || lexeme.getRef_kind().equals("Define")))
// 					checkDecl = 0;
// 				if (lexeme.getRef_kind().equals("Call")) {
// 					/* ���� �Լ��� �Ķ���� ���� üũ�ؾ� �� �� ��� */
// 					CallStack.push(lexeme);
// 					for(Function f : NumOfParam.keySet()) {
// 						if(f.getName().equals(lexeme.getData())) {
// 							theNumberOfFunctionParameter.put(lexeme, NumOfParam.get(f));
// 							break;
// 						}
// 					}
// 					beforeLexeme = lexeme;
// 				}
// 			}
// 			beforeLexeme = lexeme;
// 			newLexemes.add(lexeme);
// 		}
// 		originalLexemes = newLexemes;
// 	}

// 	public void defineOperatorKind(UdbLexemeNode lexeme) {
// 		if (operatorInfo.getUnaryOperator().contains(lexeme.getData())) {
// 			operatorKind.put(lexeme, "Unary");
// 		} else if (operatorInfo.getBinaryOperator().contains(lexeme.getData())) {
// 			operatorKind.put(lexeme, "Binary");
// 		} else if (operatorInfo.getTernaryOperator().contains(lexeme.getData())) {
// 			operatorKind.put(lexeme, "Ternary");
// 		} else if (operatorInfo.getUnclearOperator().contains(lexeme.getData())) {
// 			if (operatorInfo.checkIsThisUnary(originalLexemes, lexeme, castType)) {
// 				operatorKind.put(lexeme, "Unary");
// 			} else {
// 				operatorKind.put(lexeme, "Binary");
// 			}
// 		}
// 	}

// 	/* Cast�ϴ� �� ����, Call �Լ��� ������ ������� ó���ϴ� ���� �߰� �ʿ� */
// 	public void changePostfix(ArrayList<UdbLexemeNode> lexemes) {
// 		Stack<UdbLexemeNode> stack = new Stack<UdbLexemeNode>();
// 		/* �ϳ��� stack�� ������� ���� �Ʒ��� ����, unary stack�� �߰� ��ų ��� ����غ��� */
// 		Stack<UdbLexemeNode> castTypeStack = new Stack<UdbLexemeNode>();
// 		//UdbLexemeNode beforeLexemeNode = null;
// 		int numOfBracket = 1;
// 		for (UdbLexemeNode lexeme : lexemes) {
// //			System.out.println("lexeme data : " + lexeme);
// //			System.out.println("Stack : " + stack);
// 			if (lexeme.getToken().equals("Identifier")) {
// 				if (lexeme.getRef_kind().equals("Call"))
// 					stack.add(lexeme);
// 				else if (castType.contains(lexeme.getData())) {
// 					castTypeStack.add(lexeme);
// 					//System.out.println("castType data : " + lexeme.getData());
// 					continue;
// 				}
// 				// Cast ref_kind�� ���� castTypeStack�� lexeme�� �߰��Ѵ�.(20210729)
// 				// Cast ref_kind�� ���� castType�� lexeme.getData()�� �߰��Ѵ�.(20210730)
// 				else if(lexeme.getRef_kind().equals("Cast")) {
// 					//System.out.println(castTypeStack);
// 					castTypeStack.add(lexeme);
// 					castType.add(lexeme.getData());
// 					//stack.add(lexeme);
// 					continue;
// 				}
// 				else
// 					this.postfix.add(lexeme);
// 				if (castTypeStack.size() != 0 && (castTypeStack.size() - 1 == numOfBracket))
// 					this.postfix.add(castTypeStack.pop());
// 			} else if (lexeme.getToken().equals("Keyword")) {
// 				if (lexeme.getData().equals("sizeof"))
// 					stack.add(lexeme);
// 				else if (castType.contains(lexeme.getData()))
// 					castTypeStack.add(lexeme);
// 				else //chl@2023.02.08 - keyword (continue) handling
// 					this.postfix.add(lexeme);
// 			} else if (lexeme.getToken().equals("Literal")) {
// 				this.postfix.add(lexeme);
// 			} else if (lexeme.getToken().equals("Punctuation")) {
// 				if (lexeme.getData().equals(";")) {
// 					while (!stack.empty()) {
// 						if (stack.size() == 1 && stack.peek().getData().equals("("))
// 							break;
// 						this.postfix.add(stack.pop());
// 					}
// 					postfix.add(lexeme);
// 				} else if (lexeme.getData().equals("(")) {
// 					stack.add(lexeme);
// 					if (castTypeStack.size() != 0)
// 						numOfBracket++;
// 				} else if (lexeme.getData().equals(")")) {
// 					while (!stack.peek().getData().equals("("))
// 						this.postfix.add(stack.pop());
// 					stack.pop();
// 					if (!stack.empty() && stack.peek().getRef_kind().equals("Call"))
// 						this.postfix.add(stack.pop());
// 					if (castTypeStack.size() != 0) {
// 						numOfBracket--;
// 						if (numOfBracket == 0)
// 							continue;
// 					}
// 					if (castTypeStack.size() != 0 && (castTypeStack.size() - 1 == numOfBracket))
// 						this.postfix.add(castTypeStack.pop());
// 				} else
// 					this.postfix.add(lexeme);
// 			} else if (lexeme.getToken().equals("Operator")) {
// 				if (lexeme.getData().equals(",")) {
// 					// strcpy(p1.name, name); ���� ��쿡�� ,�� ������ �� peek�� operator��� stack.pop()�� ��������� ��.
// 					// 20210802
// 					//System.out.println("this is operator stack peek : " + stack.peek());
// 					if(stack.peek().getToken().equals("Operator")) {
// 						this.postfix.add(stack.pop());
// 					}
// 					continue;
// 				} else if (lexeme.getData().equals("[")) {
// 					stack.add(lexeme);
// 					this.postfix.add(lexeme);
// 				} else if (lexeme.getData().equals("]")) {
// 					while (!stack.peek().getData().equals("["))
// 						this.postfix.add(stack.pop());
// 					stack.pop();
// 					this.postfix.add(lexeme);
// 				}
// 				/* ���� Unary Operator�� stack���� pop�� ���� ��Ģ�� ������ �ʿ� ����, by using unary stack */
// 				else if (operatorKind.get(lexeme).equals("Unary"))
// 					stack.add(lexeme);
// 				else if (operatorKind.get(lexeme).equals("Binary") || operatorKind.get(lexeme).equals("Ternary")) {
// 					if (stack.empty()) {
// 						stack.add(lexeme);
// 					} else if (stack.peek().getRef_kind().equals("Call")
// 							|| stack.peek().getToken().equals("Punctuation")) {
// 						stack.add(lexeme);
// 					} else {
// 						if (operatorInfo.getOperatorValue().get(lexeme.getData()) > operatorInfo.getOperatorValue()
// 								.get(stack.peek().getData()))
// 							stack.add(lexeme);
// 						else {
// 							while (!stack.empty() && !stack.peek().getRef_kind().equals("Call")
// 									&& (operatorInfo.getOperatorValue().get(lexeme.getData()) <= operatorInfo
// 											.getOperatorValue().get(stack.peek().getData()))) {
// 								this.postfix.add(stack.pop());
// 							}
// 							stack.add(lexeme);
// 						}
// 					}
// 				}
// 			} else if (lexeme.getToken().equals("String")) {
// 				this.postfix.add(lexeme);
// 			}
// 			//beforeLexemeNode = lexeme;
// 		}
// 		while (!stack.empty()) {
// 			this.postfix.add(stack.pop());
// 		}
// 		//System.out.println(stack);
// 	}

// 	/* ���� ������ ����, usedVars�� �߰����� �־�� �� */
// 	public ArrayList<Expression> makeExprFromLex(ArrayList<UdbLexemeNode> lexemes) {
// 		ArrayList<Expression> buf = new ArrayList<Expression>();
// 		/* the phase of create expression corresponding to lexemeNode */
// 		for (UdbLexemeNode lexeme : lexemes) {
// 			//System.out.println("***** Lexeme : " + lexeme);
// 			//System.out.println("lexeme : " + lexeme);
// 			if (lexeme.getToken().equals("Identifier")) {
// 				/* �Ʒ��� �ڵ�� ���� �ʿ� ���� */
// 				if (stmt instanceof LabelStatement) {
// 					continue;
// 				}

// 				if (lexeme.getRef_kind().equals("Call")) {
// 					/* ���� �Լ��� parameter ���� Ȯ�� �ϴ� ���� �ľ��� ��, operator�� ������ ��Ģ���� ���� */
// 					CallExpression callExpression = new CallExpression();
// 					ArrayList<Expression> paramlist = new ArrayList<Expression>();
// 					callExpression.setActualParameters(paramlist);
// 					if(theNumberOfFunctionParameter.keySet().contains(lexeme)) {
// 						for(int i=0; i<theNumberOfFunctionParameter.get(lexeme); i++) {
// 							callExpression.addActualParameters(new Expression());
// 						}
// 					}
// 					buf.add(callExpression);
// 				} /*else if (lexeme.getRef_kind().equals("Init")) { //수정 필요 chl@2023.02.09 - declstatement 부분 보면서 돌아가는 정보 확인하기 (for loop에 decl 필요함)
// 					IdExpression idExpression;
// 					if(lexeme.getVar() != null) {
// 						idExpression = new IdExpression(lexeme.getVar().getName(), lexeme.getVar().getScope(), lexeme.getVar().getType());
// 					}
// 					else
// 						idExpression = new IdExpression(lexeme.getData(), null, null);
// 					Type type = new Type(lexeme.getData());
// 					DeclExpression declExpression = new DeclExpression(idExpression, type);
					
// 					buf.add(declExpression);
// 				} */
// 				else if (castType.contains(lexeme.getData())) {
// 					//System.out.println("cast type : " + castType);
// 					CastExpression castExpression = new CastExpression(lexeme.getData());
// 					//System.out.println("cast : " + castExpression);
// 					buf.add(castExpression);
// 				}
// 				else {
// 					IdExpression idExpression;
// 					// �켱�� cast�� ���õ� ó���� �켱 �����Ѵ�.(20210729)
// 					if(lexeme.getVar() != null) {
// 						idExpression = new IdExpression(lexeme.getVar().getName(), lexeme.getVar().getScope(), lexeme.getVar().getType());
// 					}
// 					else
// 						idExpression = new IdExpression(lexeme.getData(), null, null);
// 					buf.add(idExpression);
// 				}
// 			} else if (lexeme.getToken().equals("Operator")) {
// 				if (lexeme.getData().equals("[") || lexeme.getData().equals("]")) {
// 					BracketExpression bracketExpression = new BracketExpression();
// 					buf.add(bracketExpression);
// 				} else if (operatorKind.get(lexeme).equals("Unary")) {
// 					UnaryExpression unaryExpression = new UnaryExpression();
// 					unaryExpression.setOperator(lexeme.getData());
// 					buf.add(unaryExpression);
// 				} else if (operatorKind.get(lexeme).equals("Binary")) {
// 					BinaryExpression binaryExpression = new BinaryExpression();
// 					binaryExpression.setOperator(lexeme.getData());
// 					buf.add(binaryExpression);
// 				} else if (operatorKind.get(lexeme).equals("Ternary")) {
// 					BinaryExpression binaryExpression = new BinaryExpression();
// 					binaryExpression.setOperator(lexeme.getData());
// 					buf.add(binaryExpression);
// 				}
// 			}
// 			// String literal�� ���ؼ��� expression�� ������ ����.
// 			// ���� token=String ���� literal�� ���� ����� ���ش�.
// 			// ���� : 20210728
// 			else if ((lexeme.getToken().equals("Literal")) || (lexeme.getToken().equals("String"))) {
// 				LiteralExpression literalExpression = new LiteralExpression(lexeme.getData());
// 				buf.add(literalExpression);
// 			} else if (lexeme.getToken().equals("Keyword")) {
// 				if (this.stmt instanceof DeclarationStatement) { 
// 					  //chl@2023.01.17 - decl expression
// 						IdExpression idExpression;
// 						if(lexeme.getVar() != null) {
// 							idExpression = new IdExpression(lexeme.getVar().getName(), lexeme.getVar().getScope(), lexeme.getVar().getType());
// 						}
// 						else
// 							idExpression = new IdExpression(lexeme.getData(), null, null);
// 						Type type = new Type(lexeme.getData());
// 						DeclExpression declExpression = new DeclExpression(idExpression, type);
// 						buf.add(declExpression);
// 				}else if (castType.contains(lexeme.getData())) {
// 					CastExpression castExpression = new CastExpression(lexeme.getData());
// 					buf.add(castExpression);
// 				}else {
// 					LibraryCallExpression libraryCallExpression = new LibraryCallExpression();
// 					libraryCallExpression.setFunctionalName(lexeme.getData());
// 					buf.add(libraryCallExpression);
					
// 				}
// 			}
// 		}
		
// 		/* for the debug of expression list */
// 		/*System.out.print("\texpression list : ");
// 		for (Expression exp: buf) {
// 			System.out.print(exp.getClass().getSimpleName() + "  ");
// 		}
// 		System.out.println();
// 		System.out.println();
// 		System.out.println();*/
		
// 		return buf;
// 	}

// 	public ArrayList<Expression> mergeExpression(ArrayList<Expression> expressions) { //usedvar에 넣고, list로 반환
// 		ArrayList<Expression> buf = expressions;
// //		System.out.println("****************** First Buf ******************");
// //		for (Expression exp: buf) {
// //			System.out.println(exp.getClass().getSimpleName() + "  ");
// //		}
// 		int index = 0;
// 		ArrayList<Expression> subBuf;
// 		for (int i = 0; i < buf.size();) {
// 			//System.out.println("===== current buf : " + buf.get(i).getClass().getSimpleName());
// 			if (buf.get(i) instanceof IdExpression) {
// 				buf.get(i).addUsedVar((IdExpression) buf.get(i));
// 				i++;
// 			} else if (buf.get(i) instanceof LiteralExpression) {
// 				i++;
// 			} else if (buf.get(i) instanceof BracketExpression) {
// 				index = findNextBracket(expressions, i + 1);
// 				subBuf = new ArrayList<Expression>();
// 				for (int j = i + 1; j < index; j++)
// 					subBuf.add(buf.remove(i + 1));
// 				buf.remove(i);
// 				buf.remove(i);
// 				buf.add(i, mergeExpression(subBuf).get(0));
// 				ArrayExpression arrayExpression;
// 				if (buf.get(i - 1) instanceof IdExpression) {
// 					IdExpression atomic = (IdExpression) buf.remove(i - 1);
// 					Expression indexEpxression = buf.remove(i - 1);
// 					//arrayExpression = new ArrayExpression(atomic, indexEpxression);
// 					//buf.add(i - 1, arrayExpression);
// 				} else if (buf.get(i - 1) instanceof ArrayExpression) {
// 					ArrayExpression nestedArrayExpression = (ArrayExpression) buf.remove(i - 1);
// 					IdExpression atomic = nestedArrayExpression.getAtomic();
// 					Expression indexEpxression = buf.remove(i - 1);
// 					//arrayExpression = new ArrayExpression(nestedArrayExpression, atomic, indexEpxression);
// 					//buf.add(i - 1, arrayExpression);
// 				}
// 			} else if (buf.get(i) instanceof BinaryExpression) {
// 				Expression rhsOperand = buf.remove(i - 1);
// 				Expression lhsOperand = buf.remove(i - 2);
// 				((BinaryExpression) buf.get(i - 2)).setRhsOperand(rhsOperand);
// 				buf.get(i - 2).addUsedVars(rhsOperand.getUsedVars());
// 				((BinaryExpression) buf.get(i - 2)).setLhsOperand(lhsOperand);
// 				buf.get(i - 2).addUsedVars(lhsOperand.getUsedVars());
// 				i = i - 1;
// 			} else if (buf.get(i) instanceof UnaryExpression) {
// 				Expression operand = buf.remove(i - 1);
// 				((UnaryExpression) buf.get(i - 1)).setOperand(operand);
// 				buf.get(i - 1).addUsedVars(operand.getUsedVars());
// 			} else if (buf.get(i) instanceof CallExpression) {
// 				// CallExpression�� ���ؼ� Tree�� �ٽ� �������ش�.(20210803)
// 				int number = ((CallExpression)buf.get(i)).getActualParameters().size();
// 				ArrayList<Expression> paramList = new ArrayList<Expression>();
// 				/*chl@2023.02.08 - continue exception*/
// 				if (buf.get(i) instanceof LibraryCallExpression) { 
// 					if (((LibraryCallExpression)buf.get(i)).getFunctionalName().equals("continue")){
// 						paramList.add(new NullExpression());
// 						((CallExpression)buf.get(i)).setActualParameters(paramList);
// 						i++;
// 						continue;
// 					}
// 				}
// 				for(int j=number; j>=1; j--) {
// 					Expression parameter = buf.remove(i - j);
// 					buf.get(i - 1).addUsedVars(parameter.getUsedVars());
// 					paramList.add(parameter);
// 					i = i - 1;
// 				}
// 				((CallExpression)buf.get(i)).setActualParameters(paramList);
// 				i++;
// 			} else if (buf.get(i) instanceof CastExpression) {
// 				Expression castExpression = buf.remove(i - 1);
// 				((CastExpression) buf.get(i - 1)).setCast(castExpression);
// 			}
// 		}
// //		System.out.println("****************** final AST ******************");
// //		for (Expression exp: buf) {
// //			System.out.println(exp.getClass().getSimpleName() + "  ");
// //		}
// 		return buf;
// 	}

// 	public int findNextBracket(ArrayList<Expression> expressions, int index) {
// 		for (int i = index; i < expressions.size(); i++) {
// 			if (expressions.get(i) instanceof BracketExpression)
// 				return i;
// 		}
// 		return 0;
// 	}

	
// 	/*chl@2023.02.08 - handling if statement then, els */
// 	public Statement makeIfStatementAST(IfStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression condition = expressions.get(0);
		
// 		/*chl@2023.02.08 - conditional cast*/
// 		ConditionalExpression condexpr = new ConditionalExpression();
// 		condexpr.setCondition(condition);
// 		//condexpr.setType("If");
		
// 		/*chl@2023.02.09 - handling compoundstatement*/
// 		//get then
// 		if(stmt.getThen() != null && stmt.getThen() instanceof CompoundStatement) {
// 			condexpr.setTrueExpr(makeCompoundStatementAST(stmt.getThen()));
// 		}else if(stmt.getThen() != null && stmt.getThen() instanceof NullStatement == false) {
// 			condexpr.setTrueExpr(makeBranchStatementAST(stmt.getThen()));
// 		}else if (stmt.getThen() instanceof NullStatement) {
// 			condexpr.setTrueExpr(new NullExpression());
// 		}
		
// 		//get else
// 		if(stmt.getEls() != null && stmt.getEls() instanceof CompoundStatement) {
// 			condexpr.setFalseExpr(makeCompoundStatementAST(stmt.getEls()));
// 		}if(stmt.getEls() != null && stmt.getEls() instanceof NullStatement == false) {
// 			condexpr.setFalseExpr(makeBranchStatementAST(stmt.getEls()));
// 		}else if (stmt.getEls() instanceof NullStatement) {
// 			condexpr.setFalseExpr(new NullExpression());
// 		}
		
// 		stmt.setCondition(condexpr);
// 		return stmt;
// 	}
	
// 	/*chl@2023.02.09 - branch statement handling detail partition*/
// 	public void makePreASTFromLex(Statement stmt) {
// 		this.stmt = stmt;
// 		this.originalLexemes = stmt.getRawData();
		
// 		preprocessingLexemes(stmt);
		
// 		this.postfix = new ArrayList<UdbLexemeNode>();
// 		changePostfix(originalLexemes);
// 	}
	
// 	/*chl@2023.02.09 - branch statement handling detail partition 2*/
// 	public Statement makeASTChkStatement(Statement stmt) {
// 		if (stmt instanceof IfStatement) {
// 			return makeIfStatementAST((IfStatement) stmt);
// 		} else if (stmt instanceof ForStatement) {
// 			return makeForStatementAST((ForStatement) stmt);
// 		} else if (stmt instanceof DoStatement) {
// 			return makeDoStatementAST((DoStatement) stmt);
// 		} else if (stmt instanceof LoopStatement) {
// 			return makeLoopStatementAST((LoopStatement) stmt);
// 		} else if (stmt instanceof ExpressionStatement) {
// 			return makeExpressionStatementAST((ExpressionStatement) stmt);
// 		} else if (stmt instanceof DeclarationStatement) {
// 			return makeDeclarationStatementAST((DeclarationStatement) stmt);
// 		} else if (stmt instanceof ExitStatement) {
// 			return makeExitStatementAST((ExitStatement) stmt);
// 		} else if (stmt instanceof CaseStatement) {
// 			return makeCaseStatementAST((CaseStatement) stmt);
// 		} 
// 		// Return Statement�� ���� ���׵� ���Ǹ� ����� ��.
// 		// 20210802
// 		else if(stmt instanceof ReturnStatement) {
// 			return makeReturnStatementAST((ReturnStatement) stmt);
// 		} else if (stmt instanceof CompoundStatement) {/*chl@2023.02.08 - nested handling*/
// 			return makeCompoundStatementAST((CompoundStatement) stmt);
// 		}
// 		else {
// 			return stmt;
// 		}
// 	}
	
// 	/*chl@2023.02.08 - temporary conditional cast*/ //branch recursion considering
// 	public Expression makeBranchStatementAST(Statement stmt) {
// 		/*chl@2023.02.09 - slice function*/
// 		makePreASTFromLex(stmt);
		
// 		Expression tmp = new Expression();
		
// 		/*chl@2023.02.09 - slice function*/
// 		stmt = makeASTChkStatement(stmt);
		
// 		if(stmt instanceof BranchStatement) {
// 			tmp = ((BranchStatement)stmt).getCondition();
// 		}/*else if(stmt instanceof ForStatement){
// 			tmp = ((ForStatement)stmt).getCondition();
// 		}*/else {
// 			this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 			if (expressions.size() != 0) {
// 				tmp = expressions.get(0);
// 			}else {
// 				return (new NullExpression());
// 			}
			
// 		}
		
// 		return tmp;
		
// 	}
	
// 	/*chl@2023.02.08 - handling compound statement nested*/
// 	public Expression makeCompoundStatementAST(Statement stmt) {
// 		Statement tmp = ((CompoundStatement)stmt).getFirst();
// 		makeASTFromLexOfStmt(tmp, NumOfParam);
// 		if (tmp instanceof BranchStatement == false) {
// 			Expression tmpexpr = makeBranchStatementAST(tmp.getNext());
// 			if (tmp instanceof ExpressionStatement) {
// 				((ExpressionStatement)tmp).getExpression().setNext(tmpexpr);
// 				return ((ExpressionStatement)tmp).getExpression();
// 			} else if (tmp instanceof DeclarationStatement) {
// 				((DeclarationStatement)tmp).getDeclExpression().get(0).setNext(tmpexpr);
// 				return ((DeclarationStatement)tmp).getDeclExpression().get(0);
// 			} else if (tmp instanceof ExitStatement) {
// 				((ExitStatement)tmp).getExitExpression().setNext(tmpexpr);
// 				return ((ExitStatement)tmp).getExitExpression();
// 			} 
// 		}
// 		return new NullExpression();			
// 	}

// 	/*chl@2023.02.08 - handling for statement then, els */
// 	public Statement makeForStatementAST(ForStatement stmt) { 
// 		this.expressions = new ArrayList<Expression>();
// 		ArrayList<ArrayList<UdbLexemeNode>> bufs = splitedBySemiColon(this.postfix);
// 		for (ArrayList<UdbLexemeNode> buf : bufs)
// 			this.expressions.add(mergeExpression(makeExprFromLex(buf)).get(0));
// 		//chl@2023.02.09 - handling Decl for init
// 		//if (expressions.get(0))
// 		stmt.setInitExpression(expressions.get(0));
// 		//stmt.setCondition(expressions.get(1));
// 		stmt.setIterExpression(expressions.get(2));
		
// 		/*chl@2023.02.09 - condition part*/
// 		Expression condition = expressions.get(1);
		
// 		ConditionalExpression condexpr = new ConditionalExpression();
// 		condexpr.setCondition(condition);
// 		//condexpr.setType("For");
		
// 		/*chl@2023.02.09 - handling compoundstatement*/
// 		//get then
// 		if(stmt.getThen() != null && stmt.getThen() instanceof CompoundStatement) {
// 			condexpr.setTrueExpr(makeCompoundStatementAST(stmt.getThen()));
// 		}else if(stmt.getThen() != null && stmt.getThen() instanceof NullStatement == false) {
// 			condexpr.setTrueExpr(makeBranchStatementAST(stmt.getThen()));
// 		}else if (stmt.getThen() instanceof NullStatement) {
// 			condexpr.setTrueExpr(new NullExpression());
// 		}
		
// 		//get else
// 		if(stmt.getEls() != null && stmt.getEls() instanceof CompoundStatement) {
// 			condexpr.setFalseExpr(makeCompoundStatementAST(stmt.getEls()));
// 		}if(stmt.getEls() != null && stmt.getEls() instanceof NullStatement == false) {
// 			condexpr.setFalseExpr(makeBranchStatementAST(stmt.getEls()));
// 		}else if (stmt.getEls() instanceof NullStatement) {
// 			condexpr.setFalseExpr(new NullExpression());
// 		}
		
// 		stmt.setCondition(condexpr);
// 		return stmt;
// 	}
	
// 	public Statement makeCompoundStatementAST(CompoundStatement stmt) {	
		
// 		return stmt;
// 	}

// 	public ArrayList<ArrayList<UdbLexemeNode>> splitedBySemiColon(ArrayList<UdbLexemeNode> lexemes) {
// 		ArrayList<ArrayList<UdbLexemeNode>> bufs = new ArrayList<ArrayList<UdbLexemeNode>>();
// 		ArrayList<UdbLexemeNode> buf;
// 		buf = new ArrayList<UdbLexemeNode>();
// 		for (UdbLexemeNode lexeme : lexemes) {
// 			if (lexeme.getData().equals(";")) {
// 				bufs.add(buf);
// 				buf = new ArrayList<UdbLexemeNode>();
// 			} else {
// 				buf.add(lexeme);
// 			}
// 		}
// 		bufs.add(buf);
// 		return bufs;
// 	}

// 	private Statement makeDoStatementAST(DoStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression condition = expressions.get(0);
// 		stmt.setCondition(condition);
// 		return stmt;
// 	}

// 	private Statement makeLoopStatementAST(LoopStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression condition = expressions.get(0);
// 		stmt.setCondition(condition);
// 		return stmt;
// 	}

// 	private Statement makeExpressionStatementAST(ExpressionStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression expression = expressions.get(0);
// 		stmt.setExpression(expression);
// 		return stmt;
// 	}

// 	private Statement makeDeclarationStatementAST(DeclarationStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		ArrayList<Expression> declExpressions = new ArrayList<Expression>();
// 		for (Expression exp : expressions) {
// 			DeclExpression declExpression;
// 			if (exp instanceof BinaryExpression) {
// 				if (((BinaryExpression) exp).getLhsOperand() instanceof UnaryExpression) {
// 					declExpression = new DeclExpression((BinaryExpression) exp,
// 							(IdExpression) ((UnaryExpression) exp).getOperand(),
// 							((IdExpression) ((UnaryExpression) exp).getOperand()).getType());
// 				} else if (((BinaryExpression) exp).getLhsOperand() instanceof ArrayExpression) {
// 					declExpression = new DeclExpression((BinaryExpression) exp,
// 							((ArrayExpression) ((BinaryExpression) exp).getLhsOperand()).getAtomic(),
// 							((ArrayExpression) ((BinaryExpression) exp).getLhsOperand()).getAtomic().getType());
// 				} else {
// 					declExpression = new DeclExpression((BinaryExpression) exp,
// 							(IdExpression) ((BinaryExpression) exp).getLhsOperand(),
// 							((IdExpression) ((BinaryExpression) exp).getLhsOperand()).getType());
// 				}

// 			} else if (exp instanceof UnaryExpression) {
// 				declExpression = new DeclExpression((IdExpression) ((UnaryExpression) exp).getOperand(),
// 						((IdExpression) ((UnaryExpression) exp).getOperand()).getType());
// 			} else if (exp instanceof ArrayExpression) {
// 				declExpression = new DeclExpression(((ArrayExpression) exp).getAtomic(),
// 						((ArrayExpression) exp).getAtomic().getType());
// 			} else {
// 				declExpression = new DeclExpression((IdExpression) exp, ((IdExpression) exp).getType());
// 			}
// 			declExpressions.add(declExpression);
// 		}
// 		stmt.setDeclExpression(declExpressions);
// 		return stmt;
// 	}

// 	private Statement makeExitStatementAST(ExitStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression exitExpression = expressions.get(0);
// 		stmt.setExitExpression(exitExpression);
// 		return stmt;
// 	}

// 	private Statement makeCaseStatementAST(CaseStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression condition = expressions.get(0);
// 		stmt.setCondition(condition);
// 		return stmt;
// 	}
	
// 	private Statement makeReturnStatementAST(ReturnStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression returnExpr = expressions.get(0);
// 		ArrayList<Expression> returnParam = new ArrayList<Expression>();
		
// 		if (expressions.size() > 1)
// 			returnParam.add(expressions.get(1));
// 		((CallExpression)returnExpr).setActualParameters(returnParam);
		
// 		stmt.setReturnExpr(returnExpr);
		
// 		return stmt;
// 	}
	
// 	private Statement makeContinueStatementAST(ContinueStatement stmt) {
// 		this.expressions = mergeExpression(makeExprFromLex(this.postfix));
// 		Expression expression = expressions.get(0);
// 		stmt.setExpression(expression);
// 		return stmt;
// 	}
// }

