import java.util.ArrayList;

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
		
		ArrayList<String> commandList = new ArrayList<String>();
		commandList.add("ls -al");
		SSHManager.startShell("115.69.229.18", "user", "password", commandList);
		//SSHManager.scp("115.69.229.18", "user", "password", commandList);
	}
}
