import java.io.IOException;

public class Ping{
	public Ping(){
		
	}
	
	public static boolean ping(String targetAddress) throws IOException, InterruptedException{
		// Windows の場合
		//String[] command = {"ping", "-n", "1", "-w", "1000", targetAddress};
		// Linux の場合
		String[] command = {"ping", "-c", "1", "-t", "1", targetAddress};
		
		return new ProcessBuilder(command).start().waitFor() == 0;
	}
}
