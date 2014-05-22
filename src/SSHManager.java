import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.ChannelSftp;

import java.util.Hashtable;

public class SSHManager {
	public static void startShell(String address, String user, String passwd, ArrayList<String> commands){
		try{
			JSch jsch = new JSch();
			Hashtable config = new Hashtable();
			config.put("StrictHostKeyChecking", "no");
			jsch.setConfig(config);
			
			Session session = jsch.getSession(user, address, 22);
			session.setPassword(passwd);

			session.connect(30000); 

			Channel channel = session.openChannel("shell");

			PipedInputStream pip = new PipedInputStream(40);
			channel.setInputStream(pip);

			PipedOutputStream pop = new PipedOutputStream(pip);
			PrintStream print = new PrintStream(pop);   

			ByteBuffer buf = ByteBuffer.allocate(10);
			OutputStream os = new ByteBufferBackedOutputStream(buf);

			channel.setOutputStream(new ByteBufferBackedOutputStream(buf));

			channel.connect(30000);
			
			for(String command: commands){
				System.out.println(command);
				print.println(command);
				Thread.sleep(100);
			}
		}
		catch(com.jcraft.jsch.JSchException e){
			System.out.println(e.getMessage());
			if(e.getMessage().matches(".*timed out.*")){
				System.out.println("retry");
				startShell(address, user, passwd, commands);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void scp(String address, String user, String passwd, ArrayList<String> filePaths){
		try{
			JSch jsch = new JSch();
			Hashtable config = new Hashtable();
			config.put("StrictHostKeyChecking", "no");
			jsch.setConfig(config);
			
			Session session = jsch.getSession(user, address, 22);
			session.setPassword(passwd);

			session.connect(30000); 

			ChannelSftp channel=(ChannelSftp)session.openChannel("sftp");
			channel.connect(30000);
			
			for(String file: filePaths){
				System.out.println("upload " + file);
				channel.put(file, "./");
			}
			
			channel.disconnect();
			session.disconnect();
		}
		catch(com.jcraft.jsch.JSchException e){
			System.out.println(e.getMessage());
			if(e.getMessage().matches(".*timed out.*")){
				System.out.println("retry");
				scp(address, user, passwd, filePaths);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public static abstract class MyUserInfo
	implements UserInfo, UIKeyboardInteractive{
		public String getPassword(){ return null; }
		public boolean promptYesNo(String str){ return false; }
		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return false; }
		public boolean promptPassword(String message){ return false; }
		public void showMessage(String message){ }
		public String[] promptKeyboardInteractive(String destination,
				String name,
				String instruction,
				String[] prompt,
				boolean[] echo){
			return null;
		}
	}
}

class ByteBufferBackedOutputStream extends OutputStream{
	StringBuilder sb;
	ByteBufferBackedOutputStream( ByteBuffer buf){
		this.sb = new StringBuilder();
	}
	public synchronized void write(int b) throws IOException {
		this.sb.append((char)b);
		if(b == 10){
			//System.out.print("--");
			//System.out.print(getString());
		}
	}

	public String getString(){
		String retString = sb.toString();
		sb = new StringBuilder();
		return retString;
	}

	public void close() throws IOException{
		System.out.println("close################");
		System.out.println("close################");
		System.out.println("close################");
		System.out.println("close################");
	}
}
