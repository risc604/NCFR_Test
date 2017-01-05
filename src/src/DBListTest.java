package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DBListTest 
{
	private final String fileName = "201711.log";
	private final String HEXES = "0123456789ABCDEF";
	
	List<String>            dbDataList = new ArrayList<>();
    List<String>            dbDateTimeList = new ArrayList<>();
	
	public DBListTest() 
	{
		ArrayList<byte[]> rawDataList = logFileRead(fileName);	//get byte data List.
				
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
		
		for (int i=0; i<dbDateTimeList.size(); i++) 
		{
			System.out.printf("Date Time [%02d]:%S %n", i, dbDateTimeList.get(i) );
			//makeListSaveToDB(rawDataList.get(i));
		}
	
		
	}
	
	public void makeListSaveToDB(byte[] data)
    {
        //--- delete date time

        //--- make Drecord list
        for (int i=5; i<data.length; i+=3)
        {
            String tempStr = String.format("%02X%02X%02X", data[i], data[i+1], data[i+2]);
            //Log.d(TAG, "tempStr: " + tempStr);
            System.out.println("makeListSaveToDB(), tempStr:" + tempStr);
            dbDataList.add(tempStr);
        }
        System.out.println("makeListSaveToDB(), dbDataList length:" + data.length);
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
	   
}
