import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.omg.CORBA.Environment;

public class writeLogFile 
{
    final String HEXES = "0123456789ABCDEF";
    ArrayList<byte[]>   byteData = new ArrayList<>();
    ArrayList<byte[]>   realData = new ArrayList<>();
    
    public writeLogFile() 
    {
	/*
	// TODO Auto-generated constructor stub
	String 	FileName = makeFileName(".log");
	System.out.println("file name: " + FileName);
	BufferedWriter  logFile;
	byte[] data = makeData();
	String tmpStr1 = convertArrayToString(data, 6, 5);
	
	int records = (byteToUnsignedInt(data[11]) * 256) + byteToUnsignedInt(data[12]);
	String tmpStr2 = convertArrayToString(data, 13, (records * 3));
	
	try 
	{
	    logFile = new BufferedWriter(new FileWriter(FileName, false));
	    logFile.write(tmpStr1 + tmpStr2 + "\r\n");
	    logFile.close();
	} 
	catch (Exception e) 
	{
	    // TODO: handle exception
	    System.out.println("Error: " + e.getMessage());
	}
	*/
	
	readFile();
	covertTmp(realData);
    }
    
    public byte[] makeData()
    {
	String tmpStr = new String("4D510013A380100A1F000A0003244E00244F00244E0071");
	byte[] tmpByte = hexStringToByteArray(tmpStr);
	return tmpByte;
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
    
    public String getHexToString(byte[] raw)
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

    public byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2)
        {
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
    
    public String makeFileName(String subName)
    {
	byte[] tmpByte = getYMDhms();
	String tmpStr = "";
	System.out.println("tmpByte: " + String.valueOf(tmpByte));
	
	for(int i=0; i<tmpByte.length-3; i++)
	{
	    tmpStr += Byte.toString(tmpByte[i]);
	}
	System.out.println("make name: " + tmpStr);
	
	return (tmpStr + subName);
    }

    public byte[] getYMDhms() 
    {
	Calendar	mCal=Calendar.getInstance();
	byte[]	tmpByte = {	(byte)(mCal.get(Calendar.YEAR)-2000), 
				(byte)(mCal.get(Calendar.MONTH)+1),
				(byte)(mCal.get(Calendar.DATE)), 
				(byte)(mCal.get(Calendar.HOUR)),
				(byte)(mCal.get(Calendar.MINUTE)), 
				(byte)(mCal.get(Calendar.SECOND)),
				};
	return tmpByte;
	
    }
    
    public BufferedWriter openLogFile(String name)
    {
	try 
	{
	    FileWriter	logFile = new FileWriter(new File(name), false);
	    BufferedWriter	logWriter = new BufferedWriter(logFile);
	    return logWriter;
	} 
	catch (Exception e) 
	{
	    System.out.println("Error: " + e.getMessage());
	    // TODO: handle exception
	    return null;
	}
    }
    
    public void readFile()
    {
        //File sdcard = ".\"; //Environment.getExternalStorageDirectory();
        ArrayList<String>   textData = new ArrayList<>();
        

        byteData.clear();
        realData.clear();
        //Get the text file
        //File file = new File(sdcard, "20161024.log");
        File file = new File("20161024.log");
        System.out.println("readFile()" + file);

        //Read text from file
        //StringBuilder text = new StringBuilder();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null)
            {
                textData.add(line);
                //text.append('\n');
            }
            br.close();
        }
        catch (IOException e)
        {
            //You'll need to add proper error handling here
            System.out.println("readFile()" + e.toString());
        }
        // debug message
        for (int i=0; i<textData.size(); i++)
        {
            //byte[]  tmpbyte = new byte[textData.];
            byteData.add(hexStringToByteArray(textData.get(i)));
            System.out.println("ReadFile(), List[" + i + "]: " + textData.get(i));
            
            realData.add(separateTime(byteData.get(i)));
            //for (int j=0; j < byteData.get(i).length-4; j++)

            //    realData.get(i)[j] = byteData.get(i)[j+5];
            //Log.d("ReadFile()", "byte List[" + i + "]: " + realData.get(i));
        }
        
        for (int i=0; i<realData.size(); i++)
        {
            System.out.println("readFile(), realData[" + i +"]: " + getHexToString(realData.get(i)));
        }

        byte[] dateTime = new byte[5];
        for (int i=0; i<5; i++)
        	dateTime[i] = byteData.get(0)[i];
        System.out.println("readFile(), dateTime: " + getHexToString(dateTime));
        
        //covertTmp(realData);
        //return textData;
    }
    
    public byte[] separateTime(byte[] data)
    {
	int leng = data.length-5;
	byte[] tmp = new byte[leng];
	
	for(int i=0; i<leng; i++)
	{
	    tmp[i] = data[5+i];
	}
	//System.out.println("separateTime(), tmp: " + getHexToString(tmp));
	return tmp;
    }
    int cnt=0;
    public ArrayList<Integer> covertTmp(List<byte[]> data)
    {
	int size = data.size();
	//int[] tmp = new int[0];
	ArrayList<Integer> tmplist = new ArrayList<>();
	
	System.out.println("covertTmp(), size: " + size + ", cnt: " + (cnt++));
	
	for(int i=0; i<size; i++)
	{
	    int tmp = 0;
	    int leng = data.get(i).length;
	    System.out.println("covertTmp(), data[" + i +"], lengh: " + leng);
	    for(int j=0; j<(leng/3); j++)
	    {
		int idx= (j*3);
		tmp = byteToUnsignedInt(data.get(i)[0 + idx]) * 100 + 
				byteToUnsignedInt(data.get(i)[1+idx]);
		tmplist.add(tmp);
	    }
	    //tmplist.add(tmp);
	    //System.out.println("covertTmp(), tmplist[" + i +"]: " + tmplist.get(i));
	}
	System.out.println("covertTmp(), tmplist size:" + tmplist.size());
	for(int i=0; i<tmplist.size(); i++)
	    System.out.println("covertTmp(), tmplist[" + i +"]: " + tmplist.get(i));
	return tmplist;
    }

}
