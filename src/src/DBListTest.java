package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DBListTest 
{
	//private final String fileName = "2017112.log";
	private final String fileName = "2016111.log";
	private final String HEXES = "0123456789ABCDEF";
	//4D51002EA3801101050F0B000C195600194D00194700194500194200194000194100193F00193B00193700193000191C0047
	
	private final String testStr = "4D51002EA3801101050F0B000C195600194D00194700194500194200194000194100193F00193B00193700193000191C0047";
	private final String startTimeStr = "1701061516";
	
	List<String>            dbDataList = new ArrayList<>();
    List<String>            dbDateTimeList = new ArrayList<>();
    
    String oneRawData = new String();
   	
	public DBListTest() 
	{
		ArrayList<byte[]> rawDataList = logFileRead(fileName);	//get byte data List.
		
		timeListCheck(testStr);
		
		//byte[] tmpByte2 = hexStringToByteArray(tmpRawData);
		//makeListSaveToDB(tmpByte2);
		
		//--- parser raw data to list
		for (int i=0; i<rawDataList.size(); i++) 
		{
			//--- get start time to list.
			if (dbDateTimeList.size() < 1)
	            makeDBDateTimeList(rawDataList.get(i));
			
			System.out.printf("[%02d]:%s %n", i, getHexToString(rawDataList.get(i)) );
			makeListSaveToDB(rawDataList.get(i));
		}
		
		//--- check list is OK.
		for (int i=0; i<dbDataList.size(); i++) 
		{
			System.out.printf("[%02d]:%S %n", i, dbDataList.get(i) );
			//makeListSaveToDB(rawDataList.get(i));
		}
		
		//--- check list is OK
		for (int i=0; i<dbDateTimeList.size(); i++) 
		{
			System.out.printf("Date Time [%02d]:%S %n", i, dbDateTimeList.get(i) );
			//makeListSaveToDB(rawDataList.get(i));
		}
		
		dbDateTimeList.add( calculateEndTime(dbDataList.size(), dbDateTimeList.get(0)));
		System.out.printf("Date Time [%02d]:%S %n", 1, dbDateTimeList.get(1) );
				
		
		for (int i=0; i<dbDataList.size(); i++)
		{
			oneRawData += dbDataList.get(i);
		}
		System.out.println("oneRawData: " + oneRawData);
		
		
	}
	
	public void makeListSaveToDB(byte[] data)
    {
		//--- delete date time

        //--- make Drecord list
        for (int i=0; i<(data.length); i+=3)
        {
            String tempStr = String.format("%02X%02X%02X", data[i], data[i+1], data[i+2]);
            //Log.d(TAG, "tempStr: " + tempStr);
            System.out.println("makeListSaveToDB(), tempStr:" + tempStr);
            dbDataList.add(tempStr);
        }
        System.out.println("makeListSaveToDB(), dbDataList size:" + dbDataList.size());
        //Log.d(TAG, "dbDataList length: " + data.length);
    }
	
	public void makeDBDateTimeList(byte[] data) 
	{
        //--- delete date time
        //if (dbDateTimeList.size() < 1) {
            String tmpStr = String.format("%02x%02x%02x%02x%02x",
                    data[0], data[1], data[2], data[3], data[4]);
            dbDateTimeList.add(tmpStr);
        //}
    }
	
	@SuppressWarnings("deprecation")
	public String calculateEndTime(int records, String startTime)
	{
		String lastTime = new String();
		byte[] dateTime = new byte[5];
		
		dateTime = hexStringToByteArray(startTime);
		String tmpString = String.format("%04d%02d%02d%02d%02d", 
				(dateTime[0]+2000), dateTime[1], dateTime[2], dateTime[3], dateTime[4]);
		System.out.println("tmpString: " + tmpString);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHmm"); 
		Date startDateTime = null;
		
		try 
		{
			startDateTime = sdf.parse(tmpString);
			//startDateTime = sdf.parse(startTime);
			System.out.println("startDateTime: " + startDateTime.toString() + ", records: " + records);
			
			int tmpLong = startDateTime.getMinutes() + records;
			System.out.printf("tmpLong: %d %n", tmpLong);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDateTime);
			calendar.add(Calendar.MINUTE, records);//minute + 156
			int[] intDTime = new int[5];
			
			intDTime[0] = calendar.getTime().getYear() + 1900 - 2000;
			intDTime[1] = calendar.getTime().getMonth()+1;
			intDTime[2] = calendar.getTime().getDate();
			intDTime[3] = calendar.getTime().getHours();
			intDTime[4] = calendar.getTime().getMinutes();
			System.out.printf("Year:%02d%n Month: %02d%n Date: %02d%n Hour:%02d%n Minute: %02d%n ", 
					intDTime[0], intDTime[1], intDTime[2], intDTime[3], intDTime[4]);
			
			//lastTime = String.format("%02X%02X%02X%02X%02X. %n", 
			//		intDTime[0], intDTime[1], intDTime[2],
			//		intDTime[3], intDTime[4]);	
			//System.out.printf("intDTime year: %4d %n", intDTime[0]);
			
			byte[] byteDTime = new byte[5];
			for(int i=0; i<byteDTime.length; i++)
			{
				byteDTime[i] = (byte) (intDTime[i] & 0xFF);
			}
			
			//for(int i=0; i<byteDTime.length; i++)
			//{
			//	System.out.printf("%02X", byteDTime[i]);
			//}
			//System.out.printf("%n");
			
			lastTime = getHexToString(byteDTime);
			
			//startDateTime = Timestamp.Timestamp(tmpLong);
			//System.out.printf("int year: %4d %n", 
			//		calendar.getTime().getYear()+1900);
			//System.out.printf("int date: %02d%02d%02d%02d%02d %n", 
			//		calendar.getTime().getYear()+1900, calendar.getTime().getMonth(), calendar.getTime().getDate(), 
			//		calendar.getTime().getHours(), calendar.getTime().getMinutes());
			System.out.println("calendar: " + calendar.getTime().toString());
			System.out.println("lastTime : " + lastTime);
			
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lastTime;
	}
	
	public String getOneRawData() 
	{
		return oneRawData;
	}
	
	public int byteToUnsignedInt(byte b)
    {
        return 0x00 << 24 | b & 0xff;
    }
	
	public String convertArrayToString(byte[] data, int start, int length)
    {
        byte[] tmpbyte = new byte[length];

        for (int i=0; i<length; i++)
            tmpbyte[i] = data[start+i];
        String tmpStr = getHexToString(tmpbyte);

        return(tmpStr);
    }
	
	private void timeListCheck(String timeString) 
	{
		byte[] tmpByte = hexStringToByteArray(timeString);
		String DTime = convertArrayToString(tmpByte, 6, 5);
		System.out.println("Start Time: " + DTime);
		
		int records = (byteToUnsignedInt(tmpByte[11]) * 256) + byteToUnsignedInt(tmpByte[12]);
		String tmpRawData = convertArrayToString(tmpByte, 13, records*3);
		System.out.println("tmpRawData: " + tmpRawData + ", length: " + tmpRawData.length());
	
		//tmpByte = StringToByteArray(startTimeStr);
		tmpByte = startTimeStr.getBytes();
		System.out.printf("Test startTimeStr: %s: ->[0]:%d, [1]:%d, [2]:%d, [3]:%d, [4]:%d %n", 
				startTimeStr, tmpByte[0], tmpByte[1], tmpByte[2], tmpByte[3], tmpByte[4]);
		
		String endTime = calculateEndTime(records, DTime);
		System.out.println("end Time: " + endTime);
	}


	private ArrayList<byte[]> logFileRead(String name)
    {
    	ArrayList<byte[]>   byteData = new ArrayList<>();
    	int lineCunts=0;
        //File sdcard = ".\"; //Environment.getExternalStorageDirectory();
       	
        File file = new File(name);
        System.out.println("readFile()" + file);

        //Read text from file
        //StringBuilder text = new StringBuilder();

        try
        {
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String line, tmpLine;
	
	        line = "";
	        tmpLine = "";
	        while ((line = br.readLine()) != null)
	        {
	        	//System.out.println("readFile(), line: " + line +", tmpLine: " + tmpLine + 
	        	//		", indexOf: " + line.indexOf(tmpLine) );
	        	if(!line.equalsIgnoreCase(tmpLine))
	        	{
	        		tmpLine = line;
	        		//System.out.println("readFile(),[" + (lineCunts++) + "] raw data line: " + tmpLine);
	        		byteData.add(hexStringToByteArray(tmpLine));
	        	}
	        	
	        }
	        br.close();
        }
        catch (IOException e)
        {
            //You'll need to add proper error handling here
            System.out.println("logFileRead()" + e.toString());
        }
        // debug message
        System.out.println("logFileRead(), read " + byteData.size() + " lines");
        
        return byteData;
        
        //byte[] dateTime = new byte[5];
        //for (int i=0; i<5; i++)
        //	dateTime[i] = byteData.get(0)[i];
        //System.out.println("logFileRead(), dateTime: " + getHexToString(dateTime));
        
        //covertTmp(realData);
        //return textData;
    }
	
	private String getHexToString(byte[] raw)
    {
        //StringBuilder sb= new StringBuilder(responInfo.length);
        //for (byte indx: responInfo)
        //{
        //    sb.append(format("%02X", indx));
        //}

        if (raw == null)
        {
            return null;
        }

        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw)
        {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

	private byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2)
        {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + 
        	    		 Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
	
	private byte[] StringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2)
        {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 10) << 4) +
                    Character.digit(s.charAt(i+1), 10));
        }

        return data;
    }
    
	   
}
