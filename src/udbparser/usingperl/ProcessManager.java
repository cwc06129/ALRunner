package udbparser.usingperl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class ProcessManager {
   private ProcessBuilder builder;
   private Process process;
   private BufferedReader reader; 
   private PrintWriter writer; 

   public ProcessManager() {
      builder = null;
      process = null;
   }

   public void createProcess(List<String> commandList) {

      // Create Process by ProcessBuilder
      try {
         setBuilder(new ProcessBuilder(commandList));
         setProcess(getBuilder().start());
      } catch (IOException e1) {
         e1.printStackTrace();
      }

      getBuilder().redirectErrorStream(true); // if true, error stream will merged to stdin
      
   }

   public int finishProcess() {
      int result = -1;

      System.out.flush();
      
      try {
         result = getProcess().waitFor();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return result;
   }

   public void send(String msg) {
      getWriter().println(msg);
   }
   
   public String receive() {
      String msg = null;
      
      try {
         msg = getReader().readLine();
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      return msg;
   }
   
   public void openSender() {
	      setWriter(new PrintWriter(getProcess().getOutputStream()));
   }
   
   public void openReceiver() {
	      setReader(new BufferedReader(new InputStreamReader(getProcess().getInputStream())));
   }
   
   public void closeSender() {
      getWriter().close();
   }
   
   public void closeReceiver() {
      try {
         getReader().close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   
   /* Getter and Setters */
   public ProcessBuilder getBuilder() {
      return builder;
   }

   public void setBuilder(ProcessBuilder builder) {
      this.builder = builder;
   }

   public Process getProcess() {
      return process;
   }

   public void setProcess(Process process) {
      this.process = process;
   }
   
   public BufferedReader getReader() {
      return reader;
   }

   public void setReader(BufferedReader reader) {
      this.reader = reader;
   }

   public PrintWriter getWriter() {
      return writer;
   }

   public void setWriter(PrintWriter writer) {
      this.writer = writer;
   }

}