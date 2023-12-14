/**
 * 2023-03-23(Thu) SoheeJung
 * ClassName : InterestVarList
 * Active Learning automation interest variable list class
 */
package ALautomation;

import java.util.ArrayList;

public class InterestVarList {
    private String type; // INPUT, STATE, OUTPUT
    private ArrayList<String> nameList;
    // 2023-05-25(Thu) SoheeJung
    // key interest variable checking
    private boolean keyCheck = false;
    // 2023-04-09(Sun) SoheeJung
    // remove idExpression list (this is solved by traverse)
    // private ArrayList<IdExpression> idExpressions;

    // constructor
    public InterestVarList() {
        this.type = null;
        this.nameList = new ArrayList<String>();
        // this.idExpressions = new ArrayList<IdExpression>();
    }

    public InterestVarList(String type, ArrayList<String> nameList, boolean key) {
        this.type = type;
        this.nameList = nameList;
        // this.idExpressions = idExpressions;
    }

    // getter & setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public boolean isKeyCheck() {
        return keyCheck;
    }

    public void setKeyCheck(boolean keyCheck) {
        this.keyCheck = keyCheck;
    }

    // public ArrayList<IdExpression> getIdExpressions() {
    //     return idExpressions;
    // }

    // public void setIdExpressions(ArrayList<IdExpression> idExpressions) {
    //     this.idExpressions = idExpressions;
    // }
}
