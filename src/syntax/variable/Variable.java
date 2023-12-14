package syntax.variable;

import java.util.ArrayList;
import java.util.Objects;

public class Variable {
    private String type;
    private String name;
    private String fileName;
    private String scope; // Local or Global or Parameter

    //sohee
    private ArrayList<Integer> varScope;

    public ArrayList<Integer> getVarScope() {
        return this.varScope;
    }

    public void setVarScope(ArrayList<Integer> varScope) {
        this.varScope = varScope;
    }

    public Variable(){
        this.type = "";
        this.name = "";
        this.fileName = "";
        this.scope = "";
    }


    public Variable(String type, String name, String scope){
        this.type = type;
        this.name = name;
        this.fileName = "";
        this.scope = scope;
    }

    public Variable(String type, String name, String fileName, String scope){
        this.name = name;
        this.fileName = fileName;
        this.scope = scope;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Variable){
            Variable temp = (Variable) obj;
            return type.equals(temp.type)
                    && name.equals(temp.name)
                    && fileName.equals(temp.fileName)
                    && scope.equals(temp.scope);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, fileName, scope);
    }

    /* Getter and Setter */
    public String getType(){ return type; }

    public void setType(String type){ this.type = type; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
