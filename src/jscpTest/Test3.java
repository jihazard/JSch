package jscpTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Test3 {
    String host="yoonjh238.iptime.org";
    String user="ubuntu";
    String password="dbswlghks84!";
    Channel myChannel = null;
    PrintWriter toChannel = null;
    Session myLocalSession= null;
    static String ss;
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
    	Test3 test= new Test3();
    	test.connect("192.168.0.27");
    	
    	
    	while(true) {
    		String command = sc.next();
    		System.out.println(command);
    		
    		if(command.equals("exit")){
    			System.out.println("===============종료");
    			test.disconnected();
    			break;
    		}else if(command.equals("print")){
    			System.out.println("=============print======================================");
    			
    			System.out.println("ss==>?" +ss);
    			
    			System.out.println("=============print======================================");
        			
    		}else {
    			test.sendCommand(command);
    		}
    	}
	}
	public void connect (final String host){
	    if(host.isEmpty())
	        return;
	 
	    
	  	    try{
	        JSch jsch=new JSch();
	       

	        myLocalSession=jsch.getSession(user, host, 22);
	        //myLocalSession=jsch.getSession(user, "192.168.1.104", 22);

	        myLocalSession.setPassword("dbswlghks84!");

	        myLocalSession.setConfig("StrictHostKeyChecking", "no");

	        myLocalSession.connect(5000);   // making a connection with timeout.
	        myChannel = myLocalSession.openChannel("shell");

	        InputStream inStream = myChannel.getInputStream();

	        OutputStream outStream = myChannel.getOutputStream();
	        toChannel = new PrintWriter(new OutputStreamWriter(outStream), true);

	        myChannel.connect();
	        readerThread(new InputStreamReader(inStream));


	        Thread.sleep(100);
	        sendCommand("cd test");
	      }
	    catch(JSchException e){
	        String message = e.getMessage();
	        if(message.contains("UnknownHostException"))
	            System.out.println(">>>>> Unknow Host. Please verify hostname.");
	        else if(message.contains("socket is not established"))
	        	  System.out.println(">>>>> Can't connect to the server for the moment.");
	        else if(message.contains("Auth fail"))
	        	  System.out.println(">>>>> Please verify login and password");
	        else if(message.contains("Connection refused"))
	        	  System.out.println(">>>>> The server refused the connection");
	        else
	            System.out.println("*******Unknown ERROR********");

	        System.out.println(e.getMessage());
	        System.out.println(e + "****connect()");
	      }
	    catch(IOException e)
	    {
	        System.out.println(e);
	        System.out.println(">>>>> Error when reading data streams from the server");
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	}
	public void sendCommand(final String command)
	{
	    if(myLocalSession != null && myLocalSession.isConnected())
	    {
	        try {
	            toChannel.println(command);
	        } catch(Exception e){
	            e.printStackTrace();
	        }
	    }
	}
	
	String readerThread(final InputStreamReader tout)
	{
		 Thread read2 = new Thread(){
	    @Override
	    public void run(){
	        StringBuilder line = new StringBuilder();
	        char toAppend = ' ';
	        try {
	            while(true){
	                try {
	                    while (tout.ready()) {
	                        toAppend = (char) tout.read();
	                        if(toAppend == '\n')
	                        {
	                            System.out.print(line.toString());
	                            
	                            ss += line.toString();
	                            line.setLength(0);
	                        }
	                        else
	                            line.append(toAppend);
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    System.out.println("\n\n\n************errorrrrrrr reading character**********\n\n\n");
	                }
	                Thread.sleep(1000);
	                
	              
	            }
	           
	        }catch (Exception ex) {
	            System.out.println(ex);
	            try{
	                tout.close();
	            }
	            catch(Exception e)
	            {}
	        }
	    }
	    };
	    read2.start();
		return ss;
	}
	void disconnected(){
		  if (myChannel != null) {
			  myChannel.disconnect();
          }
          if (myLocalSession != null) {
        	  myLocalSession.disconnect();
          }
	}
	void readerThread2(final InputStreamReader tout) throws IOException
	{
	    System.out.println("====?" + tout.toString());
	    int data = tout.read();
	    while(data != -1){
	        char theChar = (char) data;
	        if(theChar == '\n') {
	        	  
	        	   System.out.println("");  	     	
	        }else {
	        	System.out.print((char)data);
	        }
	     
	        data = tout.read();
	        
	       }

	    tout.close();
	}
}
