package syntax.statement;

public class GotoStatement extends ControlStatement {
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
