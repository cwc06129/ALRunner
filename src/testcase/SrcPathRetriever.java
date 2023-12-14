package testcase;

import udbparser.usingperl.ProcessManager;
import udbparser.usingperl.UnderstandCmdManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SrcPathRetriever {
    private String SRC_DIR_PATH;
    private ArrayList<String> srcList;

    public SrcPathRetriever(String path){
        this.SRC_DIR_PATH = path;
    }

    private List<String> createCmd(){
        List<String> cmdList = new ArrayList<String>();

        if(UnderstandCmdManager.isWindows()){
            cmdList.add("cmd");
            cmdList.add("/c");
            cmdList.add("dir " + this.SRC_DIR_PATH + " /s /b"); // 디렉토리 내 모든 파일의 경로를 상대경로로 출력.
        }
        else{
            cmdList.add("/bin/sh");
            cmdList.add("-c");
            cmdList.add("tree " + this.SRC_DIR_PATH + " -i -f"); // 디렉토리 내 모든 파일의 경로를 상대경로로 출력.
        }

        return cmdList;
    }


    public void executeCmd(){
        setSrcList(new ArrayList<String>());

        ProcessManager pm = new ProcessManager();
        List<String> cmdList = createCmd();
        pm.createProcess(cmdList);
        pm.openReceiver();
        String line;
        while((line = pm.receive()) != null){
            if(isCsrc(line)){
                getSrcList().add(line);
            }
        }

        pm.closeReceiver();
    }


    public List<String> parsing(String str){
        return Arrays.asList(str.split("\n"));
    }

    public boolean isCsrc(String path){
        int size = path.length();

        if(path.length() > 2){
            if(path.substring(size-2, size).equals(".c")){
                return true;
            }
        }
        return false;
    }

    public void setSrcList(ArrayList<String> list){
        this.srcList = list;
    }

    public ArrayList<String> getSrcList(){
        return srcList;
    }

}
