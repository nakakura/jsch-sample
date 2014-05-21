import java.util.ArrayList;
import java.io.*;

public class Main {
	public static void main(String args[]){
		NetworkAddress nw = new NetworkAddress("115.69.229.18", 29);
		ArrayList<String> list = nw.addressList();
		for(String address: list){
			try{
				if(Ping.ping(address)){
					System.out.println("exist: " + address);
					//同一サブネット上のマシンを一括で操作する場合はここで
				}
			} catch(Exception e){
				
			}
		}
		
		Commands command = new Commands("./command.txt");
		ArrayList<String> commandList = command.commands();
		SSHManager.startShell("115.69.229.18", "user", "password", commandList);
		//SSHManager.scp("115.69.229.18", "user", "password", commandList);
	}
}

class Commands{
	ArrayList<String> commandList = new ArrayList<String>();
	
	public Commands(String filePath){
		this._fileRead(filePath);
	}
	
	private void _fileRead(String filePath) {
	    FileReader fr = null;
	    BufferedReader br = null;
	    try {
	        fr = new FileReader(filePath);
	        br = new BufferedReader(fr);
	 
	        String line;
	        while ((line = br.readLine()) != null) {
	        	commandList.add(line);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            br.close();
	            fr.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public ArrayList<String> commands(){
		return commandList;
	}
}