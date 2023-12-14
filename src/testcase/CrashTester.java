package testcase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class CrashTester {
    public static void main(String[] args){
        SystemTest systemTest = new SystemTest();

        // args[0]: "\"C:\Users\sselab\Desktop\CodeAnt workspace\data\crash-test\""
        SrcPathRetriever srcPathRetriever = new SrcPathRetriever(args[0]);
        srcPathRetriever.executeCmd();

        List<String> srcList = srcPathRetriever.getSrcList();

        /* the total of pass: 1245 (until 2202)
         no error:  1~428[428],  551~633[83],  635~895[261],  1684~1884[201],  1886~2145[260].  2188[1],  2190~2198[9],
                    2201~2202[2],  2205~2209[5],  2612~ (?),  2727~2730,  2888~3060[172]  , 3062~ */
        // pass(code too long) : 2726. 3061
        for (int i = 3061; i < srcList.size(); i++) {

            String current_file = srcList.get(i);
            System.out.println("###########################################################");
            System.out.printf("######### current testing:  %d(%s) / total file : %d\n", i + 1, current_file, srcList.size());
            System.out.println("###########################################################");
            try{
                systemTest.testing(current_file);
            }catch(Exception e){
                Log.TraceLog("# " + (i+1) + "("+current_file+")\n"+getPrintStackTrace(e));
            }

        }


    }

    public static String getPrintStackTrace(Exception e){
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();
    }
}
