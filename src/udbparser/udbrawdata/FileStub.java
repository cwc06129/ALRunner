package udbparser.udbrawdata;

//import jdk.nashorn.internal.parser.Token;
import syntax.statement.Function;
import syntax.variable.Variable;
import util.FunctionCFGPrinter;

public class FileStub {
    private String name;
    private String data;
    private String dataWithFuncTag;
    private String dataWithOutFuncTag;

    public FileStub(){
        this.name = "";
        this.data = "";
    }

    public FileStub(String name, String data){
        this.name = name;
        this.data = data;
    }

    public void convertFuncTagIntoCfg(Function func){
        FunctionCFGPrinter printer = new FunctionCFGPrinter();

        String[] lines = dataWithFuncTag.split("\\n");
        for(int i = 0; i < lines.length; i++){
            String line = lines[i];
            this.data += lines[i];

            String[] funcName = line.split("<FUNCTION>|</FUNCTION>");
            if(funcName.length > 1 && funcName[1].equals(func.getName())) {
                // �뿬湲곗뿉 諛붽퓭移섍린!
                this.data += "";
                //printer.printFunction(func, 0);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataWithFuncTag(){ return dataWithFuncTag; }

    public void setDataWithFuncTag(String dataWithOutFunc){
        this.dataWithFuncTag = dataWithOutFunc;
    }

    public String getDataWithOutFuncTag() {
        return dataWithOutFuncTag;
    }

    public void setDataWithOutFuncTag(String dataWithOutFuncTag) {
        this.dataWithOutFuncTag = dataWithOutFuncTag;
    }
}
