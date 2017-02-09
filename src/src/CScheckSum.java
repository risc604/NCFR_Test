package src;

public class CScheckSum 
{
	byte[] dataByte;
	
	public CScheckSum()
	{
		makeData();
		//getDataLength();
		checkSum();
	}
	
	private void makeData()
	{
		//String rawString = "4D51001FA38111020809300007224200224200224E00215D00213600212A00210400206300D8224E00215D00213600212A00210400206300D8"; 
		//String rawString = "4D510031A3811102080930000D224200224200224E00215D00213600212A002104002063002215224E00215D00213600212A00210400206300221500222300222E00234200231400231C009700222300222E00234200231400231C0097";
		//String rawString = "4D51001FA381110208102F0007235D00235D00234100234400234F00234800233300231400F7";
		String rawString = "4D51001FA38011020811170007210800210800210800205700204000203C00203C00203900203F009A";
		
		System.out.printf("String length: %04d %n", rawString.length());
		dataByte = new byte[rawString.length()/2];
		dataByte = Utils.hexStringToByteArray(rawString);
		System.out.printf("dataByte length: %04d %n", dataByte.length);
	}
	
	private void getDataLenth()
	{
		int length = Utils.byteToUnsignedInt(dataByte[2]) +	Utils.byteToUnsignedInt(dataByte[3]);
		System.out.printf("dataByte length: %04d %n", dataByte.length);
	}
	
	private void checkSum()
	{
		int sum=0;
		
		for (int i=0; i<(dataByte.length-1); i++) 
		{
			System.out.printf("[%04d]:%02X, ", i, dataByte[i]);
			sum += dataByte[i];
		}
		
		System.out.printf("%n CS: %04d, %04X H %n", sum, sum);
		
	}
	
	
}
