package udbparser.usingperl;

import org.junit.BeforeClass;
import org.junit.Test;
import testcase.Log;
import testcase.SrcPathRetriever;
import testcase.SystemTest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UdbInfoRetrieverTest {
    public static UdbInfoRetriever udbInfoRetriever;

    @BeforeClass
    public static void makeInstance() throws Exception{
        udbInfoRetriever = new UdbInfoRetriever();
    }

    @Test
    public void makeFunctionsFromUDBTest() {
        String srcPath;
        //udbInfoRetriever.makeFunctionsFromUDB(srcPath);

    }

    public ArrayList<String> createSrcPath(){

        String path = "\"C:\\Users\\sselab\\Desktop\\CodeAnt workspace\\data\\crash-test\\";
        SrcPathRetriever srcPathRetriever = new SrcPathRetriever(path);
        srcPathRetriever.executeCmd();

        ArrayList<String> srcList = srcPathRetriever.getSrcList();

        for (int i = 3061; i < srcList.size(); i++) {
            String current_file = srcList.get(i);
        }

        return srcList;
    }

    public String getPrintStackTrace(Exception e){
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();
    }
}
