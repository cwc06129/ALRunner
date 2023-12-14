package udbparser.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import syntax.statement.BranchStatement;
import syntax.statement.BreakStatement;
import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.ContinueStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.DoStatement;
import syntax.statement.EmptyStatement;
import syntax.statement.EndStatement;
import syntax.statement.ExitStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.ForStatement;
import syntax.statement.Function;
import syntax.statement.GotoStatement;
import syntax.statement.IfStatement;
import syntax.statement.Label;
import syntax.statement.LabelStatement;
import syntax.statement.LoopStatement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import syntax.statement.ReturnStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;
import udbparser.udbrawdata.UdbCFGNode;
import udbparser.udbrawdata.UdbLexemeNode;

public class CFGFromUdb {
	/* field */
	private HashMap<UdbCFGNode, Statement> ntos = new HashMap<UdbCFGNode, Statement>(); /*
	 * the table finding the Statement
	 * corresponding to CfgNode
	 */
	private HashMap<Statement, UdbCFGNode> ston = new HashMap<Statement, UdbCFGNode>(); /*
	 * the table finding the CfgNode
	 * corresponding to Statement
	 */
	private ArrayList<Statement> setOfStatement = new ArrayList<Statement>(); /* the set of statements */
	private Function function = new Function();

	/* method */
	public CompoundStatement makeFunctionCFGFromUdb(ArrayList<UdbCFGNode> cfg, String functionName) {

		/*
		 * If the data of cfg is null, then return just function object including NULL
		 * STATAEMENT in the body of function.
		 */
		if (cfg.size() == 0) {
			CompoundStatement functionBody = new CompoundStatement();
			PseudoStatement pseudoStatement1 = new PseudoStatement();
			PseudoStatement pseudoStatement2 = new PseudoStatement();
			NullStatement nullStatement = new NullStatement();

			pseudoStatement1.setNext(nullStatement);
			nullStatement.addBefore(pseudoStatement1);
			nullStatement.setNext(pseudoStatement2);
			pseudoStatement2.addBefore(nullStatement);

			functionBody.addBody(pseudoStatement1);
			functionBody.addBody(nullStatement);
			functionBody.addBody(pseudoStatement2);

			functionBody.setFirst(pseudoStatement1);
			functionBody.setLast(pseudoStatement2);

			return functionBody;
		}

		/* 0. Initialize the table and set of statement */
		ntos.clear();
		ston.clear();
		setOfStatement.clear();

		/* 1. Generating all the statement out of each UdbCFGNode. */
		int cfgNodeId = 1;
		//System.out.println("This is cfg size : " + cfg.size());
		for (UdbCFGNode udbCfgNode : cfg) {
			Statement statement = new Statement();
			statement = stmt(udbCfgNode, cfgNodeId);
			// exitstatement�� childnode�� �� �������� �ִ� pseudostatement�� �ٲ��ش�.
			// exit�� ������ ���α׷��� �����ؾ� �ϱ� �����̴�.
			// 202210827
			if(statement instanceof ExitStatement) {
				ArrayList<Integer> finalPseudo = new ArrayList<Integer>();
				finalPseudo.add(cfg.size() - 1);
				udbCfgNode.setChild_nodes(finalPseudo);
			}
//			System.out.println("udbcfgnode : " + udbCfgNode);
//			System.out.println("statement : " + statement);
			ntos.put(udbCfgNode, statement);
			ston.put(statement, udbCfgNode);
			setOfStatement.add(statement);

			if (!(statement instanceof NullStatement) && !(statement instanceof EndStatement)) {
				cfgNodeId++;
			}
		}
		
		/* 2. Finding block starts and block ends for each block statements. */
		for (UdbCFGNode udbCfgNode : cfg) {
			if (udbCfgNode.getEnd_structure_node() != -2)
				ntos.get(udbCfgNode).setEnd_block(ntos.get(cfg.get(udbCfgNode.getEnd_structure_node())));
		}

		/* 3. Add the raw data of node into statement */
		setRawData();

		/* 4. Connecting the next statement and before statement */
		setNext(cfg);

		/* 5. Add case, default, then, and else statements */
		setBlockStatement(cfg);

		/* 6. Set pair statement to END STATEMENT for adding parent statement */
		setPairOfEndStatement();

		/* 7. Add parent statement */
		setParent();

		/* 8. Delete the END STATEMENT -> component */
		removeEndStatement(functionName);

		/* 9. Set next line for printing code */
		setNextLine();

		CompoundStatement functionBody = new CompoundStatement();
		for(Statement stmt: setOfStatement) {
			if(stmt.getParents().size() != 0) { continue; }
			functionBody.getBody().add(stmt);
		}
//		System.out.println("[function Body]");
//		for(Statement stmt : functionBody.getBody()) {
//			System.out.println(stmt);
//		}
		functionBody.setFirst(functionBody.getBody().get(0));
		functionBody.setLast(functionBody.getBody().get(functionBody.getBody().size() - 1));
		
		return functionBody;
	}

	public void setRawData() {
		for (Statement statement : setOfStatement) {
			if (statement != null && !(statement instanceof EndStatement)) {
				if (statement instanceof DoStatement)
					statement.setRawData(ston.get(statement.getEnd_block()).getContents());
				else
					statement.setRawData(ston.get(statement).getContents());
				if (statement instanceof GotoStatement) {
					Label label = new Label();
					label.setName(statement.getRawStringData());
					((GotoStatement) statement).setLabel(label);
					//System.out.println("this is label : " + label.getNaFme());
				} else if (statement instanceof LabelStatement) {
					Label label = new Label();
					label.setName(statement.getRawStringData());
					((LabelStatement) statement).setLabel(label);
				}
				//System.out.println("RawData : " + statement.getRawData());
			}
		}
	}

	public void setNext(ArrayList<UdbCFGNode> cfg) {
		for (Statement statement : setOfStatement) {
			if (statement != null) {
				/* block statement */
				if (ston.get(statement).getChild_nodes().get(0) != null
						&& (statement instanceof IfStatement || statement instanceof SwitchStatement)) {
					statement.setNext(ntos.get(cfg.get(ston.get(statement).getEnd_structure_node())));
					ntos.get(cfg.get(ston.get(statement).getEnd_structure_node())).addBefore(statement);
				}
				/* do statement */
				else if (ston.get(statement).getNode_kind() == 0) {
					statement.setNext(ntos.get(cfg.get(ston.get(statement).getEnd_structure_node() + 1)));
					ntos.get(cfg.get(ston.get(statement).getEnd_structure_node() + 1)).addBefore(statement);
				}
				/* do-while statement */
				else if (ston.get(statement).getNode_kind() == 1) {
					statement.setNext(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(0))));
				}
				/* general statement */
				else if (ston.get(statement).getChild_nodes().get(0) != -2) {
					statement.setNext(ntos.get(cfg.get(ston.get(statement).getChild_nodes()
							.get(ston.get(statement).getChild_nodes().size() - 1))));
					if (!(statement instanceof CaseStatement || statement instanceof DefaultStatement)) {
						ntos.get(cfg.get(ston.get(statement).getChild_nodes()
								.get(ston.get(statement).getChild_nodes().size() - 1))).addBefore(statement);
					}
				}
//				System.out.println("CfgNode : " + ston.get(statement));
//				System.out.println("Statement : " + statement);
//				System.out.println("Next Statement : " + statement.getNext());
//				System.out.println("Before Statement : " + statement.getBefore());
				
			}
		}
	}

	public void setBlockStatement(ArrayList<UdbCFGNode> cfg) {
		for (Statement statement : setOfStatement) {
			if (statement instanceof DoStatement) {
				/* make then statement in DO STATEMENT */
				CompoundStatement thenStatement = new CompoundStatement();
				thenStatement.setLast(thenStatement.makeCompound(ntos, ston, cfg,
						ston.get(statement).getChild_nodes().get(0), ston.get(statement).getEnd_structure_node()));
				if (thenStatement.getBody().size() == 1) {
					((DoStatement) statement).setThen(thenStatement.getBody().get(0));
					((DoStatement) statement).getThen().addBefore(statement);
				} else {
					thenStatement.setFirst(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(0))));
					thenStatement.getFirst().addBefore(statement);
					thenStatement.setNext(statement.getNext());
					thenStatement.addBefore(statement);
					((DoStatement) statement).setThen(thenStatement);
				}
				/* make else statement in DO STATEMENT */
				NullStatement nullStatement = new NullStatement();
				nullStatement.setNext(statement.getNext());
				nullStatement.addBefore(statement);
				((DoStatement) statement).setEls(nullStatement);
				//System.out.println("Statement : " + statement);
				//System.out.println("thenStatement : " + ((DoStatement)statement).getThen());
			} else if (statement instanceof IfStatement) {
				/* make then statement in IF STATEMENT */
				CompoundStatement thenStatement = new CompoundStatement();
				thenStatement.setLast(thenStatement.makeCompound(ntos, ston, cfg,
						ston.get(statement).getChild_nodes().get(0), ston.get(statement).getChild_nodes().get(1)));
				if (thenStatement.getBody().size() == 1) {
					((IfStatement) statement).setThen(thenStatement.getBody().get(0));
					((IfStatement) statement).getThen().addBefore(statement);
				} else {
					thenStatement.setFirst(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(0))));
					thenStatement.getFirst().addBefore(statement);
					thenStatement.setNext(statement.getNext());
					thenStatement.addBefore(statement);
					((IfStatement) statement).setThen(thenStatement);
				}
				/* make else statement in IF STATEMENT */
				if ((ston.get(statement).getChild_nodes().get(1) == ston.get(statement).getEnd_structure_node())) {
					NullStatement nullStatement = new NullStatement();
					nullStatement.setNext(statement.getNext());
					nullStatement.addBefore(statement);
					((IfStatement) statement).setEls(nullStatement);
				} else {
					CompoundStatement elseStatement = new CompoundStatement();
					elseStatement.setLast(
							elseStatement.makeCompound(ntos, ston, cfg, ston.get(statement).getChild_nodes().get(1) + 1,
									ston.get(statement).getEnd_structure_node()));
					if (elseStatement.getBody().size() == 1) {
						((IfStatement) statement).setEls(elseStatement.getBody().get(0));
						((IfStatement) statement).getEls().addBefore(statement);
					} else {
						elseStatement.setFirst(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(1) + 1)));
						elseStatement.getFirst().addBefore(statement);
						elseStatement.setNext(elseStatement.getLast().getNext());
						elseStatement.addBefore(statement.getNext());
						((IfStatement) statement).setEls(elseStatement);
					}
				}
			} else if (statement instanceof ForStatement) {
				/* make then statement in FOR STATEMENT */
				CompoundStatement thenStatement = new CompoundStatement();
				thenStatement.setLast(thenStatement.makeCompound(ntos, ston, cfg,
						ston.get(statement).getChild_nodes().get(0), ston.get(statement).getChild_nodes().get(1)));
				if (thenStatement.getBody().size() == 1) {
					((ForStatement) statement).setThen(thenStatement.getBody().get(0));
					((ForStatement) statement).getThen().addBefore(statement);
				} else {
					thenStatement.setFirst(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(0))));
					thenStatement.getFirst().addBefore(statement);
					thenStatement.setNext(statement.getNext());
					thenStatement.addBefore(statement);
					((ForStatement) statement).setThen(thenStatement);
				}
				/* make else statement in FOR STATEMENT */
				NullStatement nullStatement = new NullStatement();
				nullStatement.setNext(statement.getNext());
				nullStatement.addBefore(statement);
				((ForStatement) statement).setEls(nullStatement);
				//System.out.println("Statement : " + statement);
				//System.out.println("thenStatement : " + ((ForStatement)statement).getThen());
			} else if (statement instanceof LoopStatement) {
				/* make then statement in LOOP STATEMENT */
				CompoundStatement thenStatement = new CompoundStatement();
				thenStatement.setLast(thenStatement.makeCompound(ntos, ston, cfg,
						ston.get(statement).getChild_nodes().get(0), ston.get(statement).getChild_nodes().get(1)));
				if (thenStatement.getBody().size() == 1) {
					((LoopStatement) statement).setThen(thenStatement.getBody().get(0));
					((LoopStatement) statement).getThen().addBefore(statement);
				} else {
					thenStatement.setFirst(ntos.get(cfg.get(ston.get(statement).getChild_nodes().get(0))));
					thenStatement.getFirst().addBefore(statement);
					thenStatement.setNext(statement.getNext());
					thenStatement.addBefore(statement);
					((LoopStatement) statement).setThen(thenStatement);
				}
				/* make else statement in LOOP STATEMENT */
				NullStatement nullStatement = new NullStatement();
				nullStatement.setNext(statement.getNext());
				nullStatement.addBefore(statement);
				((LoopStatement) statement).setEls(nullStatement);
				//System.out.println("Statement : " + statement);
				//System.out.println("thenStatement : "+ ((LoopStatement)statement).getThen());
			} else if (statement instanceof SwitchStatement) {
				ArrayList<Integer> caseSet = new ArrayList<Integer>();
				for (int i : ston.get(statement).getChild_nodes())
					caseSet.add(i);
				caseSet.add(ston.get(statement).getEnd_structure_node()); 
				for (int i = 0; i < caseSet.size() - 2; i++) {
					/* The kind of cfgNode is 'CASE STATEMENT' */
					CompoundStatement body = new CompoundStatement();
					body.setLast(body.makeCompound(ntos, ston, cfg, caseSet.get(i) + 1, caseSet.get(i + 1)));
					if (body.getBody().size() == 1) {
						body.getBody().get(0).addBefore((CaseStatement) ntos.get(cfg.get(caseSet.get(i))));
						((CaseStatement) ntos.get(cfg.get(caseSet.get(i)))).setBody(body.getBody().get(0));
					} else {
						body.setFirst(ntos.get(cfg.get(caseSet.get(i) + 1)));
						body.getFirst().addBefore(((CaseStatement) ntos.get(cfg.get(caseSet.get(i)))));
						((CaseStatement) ntos.get(cfg.get(caseSet.get(i)))).setBody(body);
						((CaseStatement) ntos.get(cfg.get(caseSet.get(i))))
								.setSwitchStatement((SwitchStatement) statement);
					}
					ntos.get(cfg.get(caseSet.get(i))).setNext(ntos.get(cfg.get(caseSet.get(i + 1))));
					ntos.get(cfg.get(caseSet.get(i + 1))).addBefore(ntos.get(cfg.get(caseSet.get(i))));
					((SwitchStatement) statement).addCase((CaseStatement) ntos.get(cfg.get(caseSet.get(i))));
				}
				/* The kind of cfgNode is 'DEFAULT STATEMENT' */
				if (cfg.get(caseSet.get(caseSet.size() - 2)).getNode_kind() == 23) {
					CompoundStatement body = new CompoundStatement();
					body.setLast(body.makeCompound(ntos, ston, cfg, caseSet.get(caseSet.size() - 2) + 1,
							caseSet.get(caseSet.size() - 1)));
					if (body.getBody().size() == 1) {
						body.getBody().get(0)
								.addBefore((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2))));
						((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2))))
								.setBody(body.getBody().get(0));
					} else {
						body.setFirst(ntos.get(cfg.get(caseSet.get(caseSet.size() - 2) + 1)));
						body.getFirst()
								.addBefore((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2))));
						((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2)))).setBody(body);
						((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2))))
								.setSwitchStatement((SwitchStatement) statement);
					}
					((SwitchStatement) statement)
							.setDefaultStatement((DefaultStatement) ntos.get(cfg.get(caseSet.get(caseSet.size() - 2))));
				} else {
					DefaultStatement defaultStatement = new DefaultStatement();
					NullStatement nullStatement = new NullStatement();
					nullStatement.setNext(statement.getNext());
					nullStatement.addBefore(defaultStatement);
					defaultStatement.setBody(nullStatement);
					defaultStatement.setNext(statement.getNext());
					((SwitchStatement) statement).setDefaultStatement(defaultStatement);
				}
			}
			// � statement�� before�߿� exitstatement�� ������ �ش� statement�� �����Ѵ�.
			// ConccurrentModificationException�� �����ϱ� ���ؼ� iterator ���
			// 20210824
//			if(statement.getBefore() != null) {
//				Iterator<Statement> itr = (statement.getBefore()).iterator();
//				while(itr.hasNext()) {
//					Statement st = itr.next();
//					if(st instanceof ExitStatement) {
//						(statement.getBefore()).remove(st);
//						break;
//					}
//				}
//			}
			
//			if((statement instanceof ExitStatement) || (statement instanceof ReturnStatement)) {
//				System.out.println("CfgNode : " + ston.get(statement));
//				System.out.println("Statement : " + statement);
//				System.out.println("Next Statement : " + statement.getNext());
//				System.out.println("Before Statement : " + statement.getBefore());
//				System.out.println();
//			}

		}
	}

	public void setPairOfEndStatement() {
		for (Statement statement : setOfStatement) {
			if (statement.getEnd_block() != null)
				((EndStatement) statement.getEnd_block()).setPairStatement(statement);
		}
	}

	public void setParent() {
		Stack<Statement> tempParent = new Stack<Statement>();
		for (Statement statement : setOfStatement) {
			Stack<Statement> currentParent = new Stack<Statement>();
			currentParent.addAll(tempParent);
			if (statement != null) {
				if (statement instanceof CaseStatement) {
					if (tempParent.peek() instanceof CaseStatement) {
						tempParent.pop();
						currentParent.pop();
					}
					tempParent.push(statement);
				} else if (statement instanceof DefaultStatement) {
					tempParent.pop();
					tempParent.push(statement);
					currentParent.pop();
				}
				statement.setParents(currentParent);
				if (statement instanceof IfStatement || statement instanceof BranchStatement
						|| statement instanceof DoStatement || statement instanceof SwitchStatement)
					tempParent.push(statement);
				else if (statement instanceof EndStatement) {
					if (((EndStatement) statement).getPairStatement() != null) {
						if (((EndStatement) statement).getPairStatement() instanceof SwitchStatement)
							tempParent.pop();
						tempParent.pop();
					}
				}
			}
			//System.out.println("Statement : " + statement);
			//System.out.println("Parent : " + statement.getParents());
		}
	}

	public void removeEndStatement(String fn) {
		int sizeOfSet = setOfStatement.size();
		PseudoStatement pseudoStatement = new PseudoStatement();
		pseudoStatement.setNext(setOfStatement.get(1));
		pseudoStatement.getNext().getBefore().remove(setOfStatement.get(0));
		pseudoStatement.getNext().getBefore().add(pseudoStatement);
		setOfStatement.remove(0);
		setOfStatement.add(0, pseudoStatement);

		PseudoStatement pseudoStatement2 = new PseudoStatement();
		if (!(setOfStatement.get(sizeOfSet - 1).getBefore() == null)) {
			pseudoStatement2.setBefore(setOfStatement.get(sizeOfSet - 1).getBefore());
			for (Statement stmt : pseudoStatement2.getBefore()) {
				stmt.setNext(pseudoStatement2);
			}
		}
		pseudoStatement2.setParents(setOfStatement.get(sizeOfSet - 1).getParents());
		pseudoStatement2.setStatementId(setOfStatement.get(sizeOfSet - 1).getStatementId());
		setOfStatement.remove(sizeOfSet - 1);
		setOfStatement.add(sizeOfSet - 1, pseudoStatement2);

		//System.out.println("\n[ Before Statement ]");
		//for(Statement stmt : setOfStatement) System.out.println(stmt);
		
		// reset the edge and remove END statement
		for (Statement stmt : setOfStatement) {
			if (stmt instanceof EndStatement) {
				if (stmt.getBefore() != null) {
					for (Statement beforeStmt : stmt.getBefore()) {
						beforeStmt.setNext(stmt.getNext());
						stmt.getNext().addBefore(beforeStmt);
					}
				}
				stmt.getNext().getBefore().remove(stmt);
			}
			//System.out.println("Statement : " + stmt);
			//System.out.println("Before Statement : " + stmt.getBefore());
		}

		int n = 0;
		while (n < sizeOfSet) {
			if (setOfStatement.get(n) instanceof EndStatement || setOfStatement.get(n) instanceof NullStatement) {
				setOfStatement.remove(n);
				sizeOfSet--;
			} else {
				n++;
			}
		}
		for (Statement stmt : setOfStatement) {
			if (stmt.getRawData().size() != 0 && stmt.getRawData().get(0).getData().equals("exit")) {
				stmt.setNext(setOfStatement.get(setOfStatement.size() - 1));
				setOfStatement.get(setOfStatement.size() - 1).addBefore(stmt);
			}
		}
//		System.out.println("\n[ Final Statement ]");
//		for(Statement stmt : setOfStatement) {
//			System.out.println(stmt);
//			System.out.println("before statement : " + stmt.getBefore());
//		}
	}

	public void setNextLine() {
		for (int i = 0; i < setOfStatement.size() - 1; i++) {
			setOfStatement.get(i).setNextLine(setOfStatement.get(i + 1));
		}
	}

	/* Generate and return statement instance corresponding to UdbCFGNode */
	public Statement findNewNext(Statement stmt) {
		Statement newNext = new Statement();
		newNext = stmt.getNext();
		if (newNext instanceof EndStatement) {
			while (true) {
				if (!(newNext instanceof EndStatement)) {
					break;
				}
				newNext = newNext.getNext();
			}
		}
		return newNext;
	}

	public Statement stmt(UdbCFGNode udbCfgNode, int cfgNodeId) {

		/* The kind of udbCfgNode is 'DO-WHILE STATEMENT' */
		if (udbCfgNode.getNode_kind() == 0) {
			DoStatement doStatement = new DoStatement();
			doStatement.setStatementId(cfgNodeId);
			return (Statement) (DoStatement) doStatement;
		}
		/* The kind of udbCfgNode is 'END STATEMENT' */
		if (udbCfgNode.getNode_kind() == 1 || udbCfgNode.getNode_kind() == 8 || udbCfgNode.getNode_kind() == 15
				|| udbCfgNode.getNode_kind() == 24 || udbCfgNode.getNode_kind() == 36) {
			EndStatement endStatement = new EndStatement();
			endStatement.setStatementId(cfgNodeId);
			return (Statement) (EndStatement) endStatement;
		}
		/* The kind of udbCfgNode is 'IF STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 5) {
			IfStatement ifStatement = new IfStatement();
			ifStatement.setStatementId(cfgNodeId);
			return (Statement) (IfStatement) ifStatement;
		}
		/* The kind of udbCfgNode is 'WHILE-FOR STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 10) {
			for (UdbLexemeNode udbLexemeNode : udbCfgNode.getContents()) {
				if (udbLexemeNode.getData().equals(";")) {
					ForStatement forStatement = new ForStatement();
					forStatement.setStatementId(cfgNodeId);
					return (Statement) (ForStatement) forStatement;
				}
			}
			LoopStatement loopStatement = new LoopStatement();
			loopStatement.setStatementId(cfgNodeId);
			return (Statement) (LoopStatement) loopStatement;
		}
		/* The kind of udbCfgNode is 'SWITCH STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 21) {
			SwitchStatement switchStatement = new SwitchStatement();
			switchStatement.setStatementId(cfgNodeId);
			return (Statement) (SwitchStatement) switchStatement;
		}
		/* The kind of udbCfgNode is 'CASE STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 22) {
			CaseStatement caseStatement = new CaseStatement();
			caseStatement.setStatementId(cfgNodeId);
			return (Statement) (CaseStatement) caseStatement;
		}
		/* The kind of udbCfgNode is 'DEFAULT STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 23) {
			DefaultStatement defaultStatement = new DefaultStatement();
			defaultStatement.setStatementId(cfgNodeId);
			return (Statement) (DefaultStatement) defaultStatement;
		}
		/* The kind of udbCfgNode is 'GOTO STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 28) {
			GotoStatement gotoStatement = new GotoStatement();
			gotoStatement.setStatementId(cfgNodeId);
			//System.out.println("this is goto statement : " + gotoStatement);
			return (Statement) (GotoStatement) gotoStatement;
		}
		/* The kind of udbCfgNode is 'RETURN STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 30) {
			ReturnStatement returnStatement = new ReturnStatement();
			returnStatement.setStatementId(cfgNodeId);
			return (Statement) (ReturnStatement) returnStatement;
		}
		/* The kind of udbCfgNode is 'BREAK STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 32) {
			BreakStatement breakStatement = new BreakStatement();
			breakStatement.setStatementId(cfgNodeId);
			return (Statement) (BreakStatement) breakStatement;
		}
		/* The kind of udbCfgNode is 'CONTINUE STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 33) {
			ContinueStatement continueStatement = new ContinueStatement();
			continueStatement.setStatementId(cfgNodeId);
			return (Statement) (ContinueStatement) continueStatement;
		}
		/* The kind of udbCfgNode is 'PASSIVE STATEMENT' */
		else if (udbCfgNode.getNode_kind() == 35) {
			if (udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getData().equals(";")) {
				//System.out.println("this is empty child : " + udbCfgNode.getChild_nodes());
				EmptyStatement emptyStatement = new EmptyStatement();
				emptyStatement.setStatementId(cfgNodeId);
				return (Statement) (EmptyStatement) emptyStatement;
			} else if (udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getData().equals("exit")) {
				ExitStatement exitStatement = new ExitStatement();
				exitStatement.setStatementId(cfgNodeId);
				return (Statement) (ExitStatement) exitStatement;
			} else if (udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getRef_kind().equals("Declare")
					&& udbCfgNode.getContents().get(0).getToken().equals("Identifier")
					&& udbCfgNode.getContents().size() == 1) {
				LabelStatement labelStatement = new LabelStatement();
				labelStatement.setStatementId(cfgNodeId);
				//System.out.println("This is label statement : " + labelStatement);
				return (Statement) (LabelStatement) labelStatement;
			} else if (udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getToken().equals("Keyword")) {
				DeclarationStatement declarationStatement = new DeclarationStatement();
				declarationStatement.setStatementId(cfgNodeId);
				return (Statement) (DeclarationStatement) declarationStatement;
			} else {
				// if statement�� body�� �ƹ��͵� �������� �ʰ� {}�� ����� ���� ���,
				// NULL �����͸� ���� Expression Statement�� ������.
				// Expression Statement�� �������� �ʰ�, Null Statement�� �����ϵ��� ����
				// 20210813
				if((udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getData().equals("NULL"))) {
					NullStatement nullStatement = new NullStatement();
					return (Statement) (NullStatement) nullStatement;
				} 
				/*chl-230314 새로운 타입의 경우 decl로 선언 */
				else if (udbCfgNode.getContents() != null && udbCfgNode.getContents().get(0).getRef_kind().equals("Type") && udbCfgNode.getContents().get(0).getToken().equals("Identifier")){
					DeclarationStatement declarationStatement = new DeclarationStatement();
					declarationStatement.setStatementId(cfgNodeId);
					return (Statement) (DeclarationStatement) declarationStatement;
				}
				ExpressionStatement expressionStatement = new ExpressionStatement();
				expressionStatement.setStatementId(cfgNodeId);
				return (Statement) (ExpressionStatement) expressionStatement;
			}
		}
		/* This statement is 'EMPTY ELSE STATEMENT' */
		else {
			NullStatement nullStatement = new NullStatement();
			return (Statement) (NullStatement) nullStatement;
		}
	}

	public void classifyVariable(Statement stmt) {
		int length = stmt.getRawData().size();

//		for (UdbLexemeNode)
	}
}
