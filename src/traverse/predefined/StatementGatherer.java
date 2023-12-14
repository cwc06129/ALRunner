package traverse.predefined;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import syntax.statement.CompoundStatement;
import syntax.statement.EndStatement;
import syntax.statement.Function;
import syntax.statement.Statement;
import syntax.statement.NullStatement;
import syntax.statement.PseudoStatement;
import traverse.general.ForwardCfgTraverser;

public class StatementGatherer extends ForwardCfgTraverser {
	
	private List<Statement> allStatements = new ArrayList<>();
	
	public List<Statement> getAllStatement(Function f) {
		allStatements.clear();
		traverse(f);
		return allStatements.stream().distinct().collect(Collectors.toList());
	}
	
	@Override
	public void visit(Statement f) {
		if ( f instanceof CompoundStatement || f instanceof EndStatement || f instanceof NullStatement || f instanceof PseudoStatement) {
			return;
		}
		allStatements.add(f);
	}

}
