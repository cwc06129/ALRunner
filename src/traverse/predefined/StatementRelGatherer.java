package traverse.predefined;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import syntax.statement.BranchStatement;
import syntax.statement.CaseStatement;
import syntax.statement.CompoundStatement;
import syntax.statement.ControlStatement;
import syntax.statement.DeclarationStatement;
import syntax.statement.DefaultStatement;
import syntax.statement.EmptyStatement;
import syntax.statement.ExpressionStatement;
import syntax.statement.Function;
import syntax.statement.LabeledStatement;
import syntax.statement.Statement;
import syntax.statement.SwitchStatement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import traverse.general.ForwardCfgTraverser;

public class StatementRelGatherer extends ForwardCfgTraverser {
	
	private List<StmtRel> allStatements = new ArrayList<>();
	
	public List<StmtRel> getAllStatementRelation(Function f) {
		allStatements.clear();
		traverse(f);
		return allStatements.stream().distinct().collect(Collectors.toList());
	}
	
	private void append(Statement m, Statement n) {
		if (m instanceof NullStatement || n instanceof NullStatement || m instanceof PseudoStatement || n instanceof PseudoStatement) {
			return;
		}
		boolean found = false;
		for (StmtRel r:allStatements) {
			if (r.getPredecessor().equals(m) && r.getSuccessor().equals(n)) {
				found = true;
			}
		}
		if (!found) {
			allStatements.add(new StmtRel(m, n));
		}
	}
	
	@Override
	public void visit(BranchStatement f) {
		// add current to next
		append(f, f.getNext());
		// add current to then
		if (f.getThen() instanceof CompoundStatement) {
			append(f, ((CompoundStatement) f.getEls()).getFirst());
		} else {
			append(f, f.getThen());
		}
		// add current to else
		if (f.getEls() instanceof CompoundStatement) {
			append(f, ((CompoundStatement) f.getEls()).getFirst());
		} else {
			append(f, f.getEls());
		}
	}
	
	@Override
	public void visit(ExpressionStatement f) {
		// add current to next
		append(f, f.getNext());
	}
	
	@Override
	public void visit(EmptyStatement f) {
		// add current to next
		append(f, f.getNext());
	}
	
	@Override
	public void visit(LabeledStatement f) {
		if(f instanceof CaseStatement) {
			// add current to next
			append(f, f.getNext());
			// add current to then
			//append(f, ((CaseStatement) f).getBody().getFirst());
		} else if(f instanceof DefaultStatement) {
			// add current to next
			append(f, f.getNext());
			// add current to then
			//append(f, ((DefaultStatement) f).getBody().getFirst());
		} else {
			// add current to next
			append(f, f.getNext());
		}
	}
	
	@Override
	public void visit(ControlStatement f) {
		// add current to next
		append(f, f.getNext());
	}
	
	@Override
	public void visit(DeclarationStatement f) {
		// add current to next
		append(f, f.getNext());
	}
	
	@Override
	public void visit(SwitchStatement f) {
		// add current to next
		append(f, f.getNext());
		// add current to then
		for (CaseStatement c : f.getCases()) {
			append(f, c);
		}
	}
	
	public class StmtRel {
		private Statement predecessor;
		private Statement successor;
		public StmtRel(Statement predecessor, Statement successor) {
			super();
			this.predecessor = predecessor;
			this.successor = successor;
		}
		public Statement getPredecessor() {
			return predecessor;
		}
		public void setPredecessor(Statement predecessor) {
			this.predecessor = predecessor;
		}
		public Statement getSuccessor() {
			return successor;
		}
		public void setSuccessor(Statement successor) {
			this.successor = successor;
		}
		
//		@Override
//		public boolean equals(Object arg0) {
//			if (arg0 instanceof StmtRel) {
//				return predecessor.getRawStringData().equals(((StmtRel) arg0).getPredecessor().getRawStringData()) 
//						&& successor.getRawStringData().equals(((StmtRel) arg0).getSuccessor().getRawStringData());
//			}
//			return false;
//		}
		public String toString() {
			String str = this.predecessor + "\t-->\t" + this.successor;
			return str;
		}
	}

}
