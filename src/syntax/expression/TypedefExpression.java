package syntax.expression;

import java.util.ArrayList;

public class TypedefExpression extends Expression {
    String prefix;
    String Type;
    String name;
    ArrayList<Expression> expressions = new ArrayList<Expression>();
    String nick;

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Expression> getExpressions() {
		return this.expressions;
	}

    public void setExpressions(ArrayList<Expression> expressions) {
		this.expressions= expressions;
	}

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public void makeDeclExpr(){
        for (int i=0 ; i< this.getExpressions().size() ; i++){
            Expression expr = this.getExpressions().get(i);
            if (expr instanceof IdExpression){
                DeclExpression newdecl = new DeclExpression(new BinaryExpression(new Operator(), expr, new NullExpression()), (IdExpression)expr, ((IdExpression) expr).getType());
                this.getExpressions().remove(i);
                this.getExpressions().add(i, newdecl);
            } else if (expr instanceof LiteralExpression){
                this.getExpressions().remove(i);
                i--;
            } else if (expr instanceof UnaryExpression){
                if (((UnaryExpression)expr).getOperator().getOperator().equals("*") && ((UnaryExpression)expr).getOperand() instanceof IdExpression){
                    IdExpression id = ((IdExpression)((UnaryExpression)expr).getOperand());
                    id.setType(id.getType() + ((UnaryExpression)expr).getOperator().getOperator());
                    DeclExpression newdecl = new DeclExpression(new BinaryExpression(new Operator(), id, new NullExpression()), (IdExpression)id, ((IdExpression) id).getType());
                    this.getExpressions().remove(i);
                    this.getExpressions().add(i, newdecl);
                }
            }
        }
    }

    public String getCode(){
        String code = "";
        if (prefix != null && prefix.isEmpty() == false) code += prefix + " ";
        if (Type != null && Type.isEmpty() == false) code += Type + " ";
        if (name != null && name.isEmpty() == false) code += name + " ";
        return code;
    }
}
