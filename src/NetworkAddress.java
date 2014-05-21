import java.util.ArrayList;

public class NetworkAddress {
	long networkAddress = -1;
	long addressesInSubmet = -1;
	int mask = -1;

	long firstOctedBase = (long)java.lang.Math.pow(2.0, 24.0);
	long secondOctedBase = (long)java.lang.Math.pow(2.0, 16.0);
	long thirdOctedBase = (long)java.lang.Math.pow(2.0, 8.0);
	long forthOctedBase = (long)java.lang.Math.pow(2.0, 0.0);

	public NetworkAddress(String address, int mask){
		this.mask = mask;
		this._calcNetworkAddress(address, mask);
		this.addressList();
	}

	private long _longAddress(String address){
		String[] addresses = address.split("\\.");
		long lAddress = 0;
		lAddress += Integer.parseInt(addresses[0]) * firstOctedBase;
		lAddress += Integer.parseInt(addresses[1]) * secondOctedBase;
		lAddress += Integer.parseInt(addresses[2]) * thirdOctedBase;
		lAddress += Integer.parseInt(addresses[3]) * forthOctedBase;
		return lAddress;
	}

	private String _stringAddress(long address){
		int firstOcted = (int)(address / firstOctedBase);
		int secondOcted = (int)(address % firstOctedBase / secondOctedBase);
		int thirdOcted = (int)(address % secondOctedBase / thirdOctedBase);
		int forthOcted = (int)(address % thirdOctedBase);
		return String.format("%d.%d.%d.%d", firstOcted, secondOcted, thirdOcted, forthOcted);
	}
	
	private void _calcNetworkAddress(String sAddress, int mask){
		String maskString = "";
		String subnetAddressesString = "";

		for(int i = 0; i < mask; i++){
			maskString += "1";
		}

		for(int i = mask; i < 32; i++){
			maskString += "0";
			subnetAddressesString += "1";
		}

		long lAddress = this._longAddress(sAddress);
		long lMask = Long.parseLong(maskString, 2);
		networkAddress = lAddress & lMask;
		addressesInSubmet = Long.parseLong(subnetAddressesString, 2);
	}

	public ArrayList<String> addressList(){
		ArrayList<String> addressList = new ArrayList<String>();

		for(long l = networkAddress + 1; l < networkAddress + addressesInSubmet; l++){
			addressList.add(this._stringAddress(l));
		}

		return addressList;
	}
}
