import java.util.ArrayList;
import java.io.*;

public class Main {
	public static void main(String args[]){
		if(args.length != 5){
			System.out.println("java -jar Main.jar USERNAME PASSWORD NETWORKADDRESS MASK COMMANDFILEPATH");
			System.exit(0);
		}
		
		String user = args[0];
		String pass = args[1];
		String network = args[2];
		int mask = Integer.parseInt(args[3]);
		String commandFilePath = args[4];
		NetworkAddress nw = new NetworkAddress(network, mask);
		Commands command = new Commands(commandFilePath);
		
		ArrayList<String> commandList = command.commands();
		ArrayList<String> list = nw.addressList();
		for(String address: list){
			try{
				if(Ping.ping(address)){
					System.out.println("exist: " + address);
					SSHManager.startShell(address, "user", "password", commandList);
				}
			} catch(Exception e){
				
			}
		}
		
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