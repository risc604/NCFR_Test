import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

public class writeLogFile 
{
    final String HEXES = "0123456789ABCDEF";
    
    public writeLogFile() 
    {
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
}
