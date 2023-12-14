package udbparser.udbrawdata;

import java.util.ArrayList;

public class UdbCFGNode {
	
	/* field */
	private int node_kind;		// K
	private int start_line;		// sline
	private int start_column;	// scol
	private int end_line;		// eline
	private int end_column;		// ecol
	private int end_structure_node;	// esn
	private ArrayList<Integer> child_nodes;	// children
	private ArrayList<UdbLexemeNode> contents;
	
	
	/* method */
	public UdbCFGNode() {
		node_kind = -1;
		start_line = 0;
		start_column = 0;
		end_line = 0;
		end_column = 0;
		end_structure_node = 0;
		child_nodes = new ArrayList<>();
	}

	public UdbCFGNode(int k, int sline, int scol, int eline, int ecol, int esn) {
		this.node_kind = k;
		this.start_line = sline;
		this.start_column = scol;
		this.end_line = eline;
		this.end_column = ecol;
		this.end_structure_node = esn;
	}
	
	@Override
	public String toString() {
		return "CFG [node_kind=" + node_kind + ", start_line=" + start_line + ", start_column=" + start_column
				+ ", end_line=" + end_line + ", end_column=" + end_column + ", end_structure_node=" + end_structure_node
				+ ", child_nodes=" + child_nodes + "]";
	}
	
	
	/* getter and setter */
	public int getNode_kind() {
		return node_kind;
	}

	public void setNode_kind(int node_kind) {
		this.node_kind = node_kind;
	}

	public int getStart_line() {
		return start_line;
	}

	public void setStart_line(int start_line) {
		this.start_line = start_line;
	}

	public int getStart_column() {
		return start_column;
	}

	public void setStart_column(int start_column) {
		this.start_column = start_column;
	}

	public int getEnd_line() {
		return end_line;
	}

	public void setEnd_line(int end_line) {
		this.end_line = end_line;
	}

	public int getEnd_column() {
		return end_column;
	}

	public void setEnd_column(int end_column) {
		this.end_column = end_column;
	}

	public int getEnd_structure_node() {
		return end_structure_node;
	}

	public void setEnd_structure_node(int end_structure_node) {
		this.end_structure_node = end_structure_node;
	}

	public ArrayList<Integer> getChild_nodes() {
		return child_nodes;
	}

	public void setChild_nodes(ArrayList<Integer> child_nodes) {
		this.child_nodes = child_nodes;
	}

	public ArrayList<UdbLexemeNode> getContents() {
		return contents;
	}

	public void setContents(ArrayList<UdbLexemeNode> contents) {
		this.contents = contents;
	}

	

}
