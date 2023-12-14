package syntax.statement;

public class LabelStatement extends LabeledStatement {
	/* field */
	private Label label = new Label();

	
	/* getter and setter */
	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
}
