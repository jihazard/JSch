package jscpTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSH {
	public static void main(String[] args) throws JSchException, IOException, InterruptedException {
		String username = "ubuntu";
        String host = "192.168.0.27";
        int port = 22;
        String password = "dbswlghks84!";
        
        System.out.println("==> Connecting to" + host);
        Session session = null;
        Channel channel = null;
     	 JSch jsch = new JSch();
         session = jsch.getSession(username, host, port);
         session.setPassword(password);
    //   try {  
         // 4. 세션과 관련된 정보를 설정한다.
         java.util.Properties config = new java.util.Properties();
         // 4-1. 호스트 정보를 검사하지 않는다.
         config.put("StrictHostKeyChecking", "no");
         session.setConfig(config);
         
         session.connect();
         
 		channel = session.openChannel("shell");
 		channel.setInputStream(null);
//		channel.setInputStream(System.in);
//		channel.setOutputStream(System.out);
//		
//		System.out.println("channel.getId()==>" + channel.getId()) ;
//		System.out.println("channel.getId()==>" + channel.getExitStatus()) ;
//		
 		InputStream inputStream = channel.getInputStream(); // <- 일반 출력 스트림
 		//final InputStream errStream = channel.ge;// <- 일반 에러 스트림
 		//channel.setOutputStream(out);
 		int BUFFER_SIZE= 1024;
 		byte[] buffer = new byte[BUFFER_SIZE];
 		 
 		
 		InputStream userInput = new ByteArrayInputStream("ls".getBytes("UTF-8"));
 		channel.setInputStream(userInput);
 		channel.connect();
		
 		while(true) {
 		    while (inputStream.available() > 0) {
 		        int i = inputStream.read(buffer, 0, BUFFER_SIZE);
 		        if (i < 0) {
 		            break;
 		        }
 		 
 		        System.out.println(new String(buffer, 0, i));
 		    }
// 		 
// 		    while (errStream.available() > 0) {
// 		        int i = errStream.read(buffer, 0, BUFFER_SIZE);
// 		        if (i > 0) {
// 		            System.err.println(new String(buffer, 0, i));
// 		        }
// 		    }
 		 
 		    if (channel.isClosed()) {
 		        if (inputStream.available() > 0) {
 		            continue;
 		        }
 		        break;
 		    }
 		 
 		    TimeUnit.MILLISECONDS.sleep(100);
 		}
 		 
 		final int exitStatus = channel.getExitStatus();
 		System.out.println("Exit Status : " + exitStatus);
 		 
 		channel.disconnect();
 		session.disconnect();
		
		
//        } catch (JSchException e) {
//            e.printStackTrace();
//        } finally {
//            if (channel != null) {
//                channel.disconnect();
//            }
//            if (session != null) {
//                session.disconnect();
//            }
//        }
//       

	

		
	}
}
