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
		//String rawString = "4D51001FA38011020811170007210800210800210800205700204000203C00203C00203900203F009A";
		//String rawString = "4D510028A3801102090F30000A250C00242B00235F00243C00263D00251F00243400235A00235600DA";
		//String rawString = "4D510028A3801102090F30000A260F00250C00242B00235F00243C00263D00251F00243400235A00235600DA";
		//String rawString = "4D510025A38011020914210009151900151900151900151B00151C00151E00151F00152000151F00151E";
		//String rawString = "4D51001FA38111020915070007286100286100291E00292B00293600294300293B00E3";
		//String rawString = "4D510016A380110209151B00042A0600295C002A04002A17004B";
		String rawString = "4D51000DA3811102091528000127190069";
		
		
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
		
		System.out.printf("%n CS: %04d, %04X H, [%02d]: %02X H %n", sum, sum, 
				dataByte.length-1, dataByte[dataByte.length-1]);
		
	}
	
	
}
