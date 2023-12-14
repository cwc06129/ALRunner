package syntax.expression;


public class DeclExpression extends CompoundExpression {
	/* field */
	private BinaryExpression binaryExpression = null;
	private IdExpression idExpression = null;
	private Type type = null;

	public DeclExpression(IdExpression idExpression, Type type) {
		this.setIdExpression(idExpression);
		this.setType(type);
	}
	
	public DeclExpression(BinaryExpression expression, IdExpression expression2, Type type) {
		this.setBinaryExpression(expression);
		this.setIdExpression(expression2);
		this.setType(type);
	}

	/* getter and setter */
	public BinaryExpression getBinaryExpression() {
		return binaryExpression;
	}

	public void setBinaryExpression(BinaryExpression binaryExpression) {
		this.binaryExpression = binaryExpression;
	}

	public IdExpression getIdExpression() {
		return idExpression;
	}

	public void setIdExpression(IdExpression idExpression) {
		this.idExpression = idExpression;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	//chl@2023.02.07 - print template
	public String toString() {
		return this.getClass().getSimpleName() + "-> " + idExpression ;//출력변환
	}
	
	//chl@2023.02.08 - code print override
	public String getCode() {

		if (type == null)
			return "";
			return type.toString() + " " + binaryExpression.getCode();
		
	}

	//chl@2023.02.13 - makeDeclExpression (cannot override)
	public void makeDeclExpression(Expression exp){
		//chl@2023.02.15 - set operand, lhsoperand, type
		if (exp instanceof BinaryExpression) {
			this.binaryExpression = (BinaryExpression)exp;
			if (((BinaryExpression) exp).getLhsOperand() instanceof UnaryExpression) {
				this.idExpression = (IdExpression) ((UnaryExpression) ((BinaryExpression) exp).getLhsOperand()).getOperand();
				this.type = ((IdExpression) ((UnaryExpression) ((BinaryExpression) exp).getLhsOperand()).getOperand()).getType();
			} else if (((BinaryExpression) exp).getLhsOperand() instanceof ArrayExpression) {
				this.idExpression = ((ArrayExpression) ((BinaryExpression) exp).getLhsOperand()).getAtomic();
				this.type =((ArrayExpression) ((BinaryExpression) exp).getLhsOperand()).getAtomic().getType();
			} else if (((BinaryExpression) exp).getLhsOperand() instanceof CastExpression) {
				this.idExpression = (IdExpression) ((CastExpression)((BinaryExpression) exp).getLhsOperand()).getCast();
				this.type = ((IdExpression)((CastExpression)((BinaryExpression) exp).getLhsOperand()).getCast()).getType();
			} else if (((BinaryExpression) exp).getLhsOperand() instanceof BinaryExpression) {
				makeDeclExpression(((BinaryExpression) exp).getLhsOperand());
				//this.idExpression = (IdExpression) ((BinaryExpression)((BinaryExpression) exp).getLhsOperand()).getLhsOperand();
				//this.type = ((IdExpression)((BinaryExpression)((BinaryExpression) exp).getLhsOperand()).getLhsOperand()).getType();
			} else {
				this.idExpression = (IdExpression) ((BinaryExpression) exp).getLhsOperand();
				this.type = ((IdExpression) ((BinaryExpression) exp).getLhsOperand()).getType();
			}

		} else if (exp instanceof UnaryExpression) {
			if (((UnaryExpression) exp).getOperand() instanceof ArrayExpression) {
				this.idExpression = ((ArrayExpression) ((UnaryExpression) exp).getOperand()).getAtomic();
				this.type =((ArrayExpression) ((UnaryExpression) exp).getOperand()).getAtomic().getType();
				this.binaryExpression = new BinaryExpression(new Operator(), exp, new NullExpression());
			} else {
				this.idExpression = (IdExpression) ((UnaryExpression) exp).getOperand();
				this.type = ((IdExpression) ((UnaryExpression) exp).getOperand()).getType();
				this.binaryExpression = new BinaryExpression(new Operator(), exp, new NullExpression());
			}
		} else if (exp instanceof ArrayExpression) {
			this.idExpression = ((ArrayExpression) exp).getAtomic();
			this.type = ((ArrayExpression) exp).getAtomic().getType();
			this.binaryExpression = new BinaryExpression(new Operator(), exp, new NullExpression());
		} else if (exp instanceof IdExpression) {
			this.idExpression = (IdExpression) exp;
			this.type = ((IdExpression) exp).getType();
			this.binaryExpression = new BinaryExpression(new Operator(), exp, new NullExpression());
		} /*else {
			this.idExpression = (IdExpression) exp;
			this.type = ((IdExpression) exp).getType();
			this.binaryExpression = new BinaryExpression(new Operator(), exp, new NullExpression());
		}*/
	}
}
