import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
//import java.time.format.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class logFileObject 
{
    final String HEXES = "0123456789ABCDEF";
    
    //ArrayList<byte[]>   realData = new ArrayList<>();
    
    public logFileObject() 
    {
	/*
	// TODO Auto-generated constructor stub
	
	*/
		
       	byte[] dateTime = new byte[8];
        dateTime = getYMDhms();
        for(int i=0; i<dateTime.length; i++)
        System.out.printf("Time[%02d] = %04d\n", i, dateTime[i]);
       	
        ArrayList<byte[]>	rawDataList = logFileRead("2016112.log");	//get byte data List.
        ArrayList<byte[]>	dateTimeList = getDateTimeList(rawDataList);
        //ArrayList<Integer>	temperatureList = getTemperatureList(rawDataList);
        //ArrayList<Long>		secondList = getSecondList(dateTimeList);
        
        ArrayList<Date> dtList= dtTosecond(dateTimeList);
       	ArrayList<Integer>	hourIndexList = foundHourList(dateTimeList);
       	debugPrintList1("hour Index:", hourIndexList);
       	
       	//ArrayList<Long> longList = diffSecList(dtList);
       	ArrayList<Integer> displayList = get180RecordList(dtList);
       	
       	//SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm");
       	for(int i=0; i<displayList.size(); i++)
            System.out.printf("Time[%02d] = %s (%04d) %n", i, 
            		/*sdf.format(*/dtList.get(displayList.get(i))/*)*/, displayList.get(i) );
        
        //dataParser(rawDataList);
	//separateTime(byteData);
	//covertTmp(realData);
    }
    
    
    public static ArrayList<Integer> get180RecordList(ArrayList<Date> dateList)
    {
    	ArrayList<Integer> indexList = new ArrayList<>();
    	long timeStemp = 3 * 86400;	//over 3 days
    	
    	//for(int i=0; i<dateList.size()-1; i++)
    	int i=0, k;
    	do
    	{
    		if(i>(dateList.size()-2))
    			k = dateList.size()-1;
    		else
    			k = i+1;
    		
    		long now = TimeUnit.MILLISECONDS.toSeconds(
    					dateList.get(k).getTime() - dateList.get(i).getTime());
    		
    		if((now < timeStemp) && (i<dateList.size()))
    		{
    			indexList.add(i);
    		}

    		//System.out.printf("get180RecordList(), indexList[%02d]: %d %n", i, indexList.get(i));
    		//System.out.printf("diffSecList(), MILLISECONDS.toSeconds.[%02d]: %d, dateList[%02d]:{%s} %n",
    		//			i, now, i, sdf.format((dateList.get(0).getTime() + longList.get(i-1))));
    		i++;
    	}while(i<(dateList.size()));
    	
    	for(int j=0; j<indexList.size(); j++)
    	{
    		System.out.printf("get180RecordList(), indexList[%02d]: %d %n", j, indexList.get(j));
    	}
    	
    	return indexList;
    }
    
    public static ArrayList<Long> diffSecList(ArrayList<Date> dateList) 
    {
    	ArrayList<Long> longList = new ArrayList<>();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm");
    	
    	//for(int i=0; i<dateList.size(); i++)
    	//{
    	//	long now = TimeUnit.MILLISECONDS.toHours(dateList.get(i).getTime());
    	//	longList.add(now);
    	//	System.out.printf("diffSecList(), MILLISECONDS.toHours.[%02d]: %08d %n", i, now);
    	//}
    	
    	for(int i=0; i<dateList.size()-1; i++)
    	{
    		long now = TimeUnit.MILLISECONDS.toSeconds(dateList.get(i+1).getTime() - 
    											dateList.get(i).getTime());
    		longList.add(now);

    		System.out.printf("diffSecList(), MILLISECONDS.toSeconds.[%02d]: %d %n", i, now);
    		//System.out.printf("diffSecList(), MILLISECONDS.toSeconds.[%02d]: %d, dateList[%02d]:{%s} %n",
    		//			i, now, i, sdf.format((dateList.get(0).getTime() + longList.get(i-1))));
    	}
    	
    	long now = dateList.get(0).getTime() ;
    	System.out.printf("diffSecList(), dateList[%02d]:{%s}, %d, %d %n",
				0, sdf.format(now), 0, now/1000);
    	for(int i=0; i<longList.size()-1; i++)
    	{
    		//long now = TimeUnit.MILLISECONDS.toMinutes(dateList.get(i+1).getTime() - 
    		//									dateList.get(0).getTime());
    		//longList.add(now);
    		//System.out.printf("diffSecList(), MILLISECONDS.toMinutes.[%02d]: %d %n", i, now);
    		
    		
    		now += (longList.get(i).longValue()*1000);
    		System.out.printf("diffSecList(), dateList[%02d]:{%s}, %d, %d %n",
					i+1, sdf.format(now), longList.get(i).longValue(), now/1000);
    	}
    	///TimeUnit.SECONDS.toMinutes(dateList.get(i))
    	
    	//for(int i=1; i<longList.size(); i++)
    	//{
    	//	System.out.println("duration:[" + i + "]: " + longList.get(i) + " Minutes");
    	//}
    	
		return longList;
    }
    
    public String logFileWrite()
    {
	String 	fileName = makeFileName(".log");
	System.out.println("file name: " + fileName);
	BufferedWriter  logFile;
	byte[] data = makeData();
	String tmpStr1 = convertArrayToString(data, 6, 5);
	
	int records = (byteToUnsignedInt(data[11]) * 256) + byteToUnsignedInt(data[12]);
	String tmpStr2 = convertArrayToString(data, 13, (records * 3));
	
	try 
	{
	    logFile = new BufferedWriter(new FileWriter(fileName, false));
	    logFile.write(tmpStr1 + tmpStr2 + "\r\n");
	    logFile.close();
	} 
	catch (Exception e) 
	{
	    // TODO: handle exception
	    System.out.println("Error: " + e.getMessage());
	}
	
	return fileName;
    }
    
    public ArrayList<byte[]> logFileRead(String name)
    {
    	ArrayList<byte[]>   byteData = new ArrayList<>();
    	//int lineCunts=0;
        //File sdcard = ".\"; //Environment.getExternalStorageDirectory();
       	//name = "2016112.log";
        //Get the text file
        //File file = new File(sdcard, "20161024.log");
        File file = new File(name);
        System.out.println("readFile()" + file);

        //Read text from file
        //StringBuilder text = new StringBuilder();

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null)
            {
        	byteData.add(hexStringToByteArray(line));
        	//System.out.println("readFile(),[" + (lineCunts++) + "] raw data line: " + line);
        	
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
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + 
        	    		 Character.digit(s.charAt(i+1), 16));
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
	Calendar mCal = Calendar.getInstance();
	byte[]	tmpByte = {	(byte)(mCal.get(Calendar.YEAR)-2000), 
				(byte)(mCal.get(Calendar.MONTH)+1),
				(byte)(mCal.get(Calendar.DATE)), 
				(byte)(mCal.get(Calendar.HOUR_OF_DAY)),
				(byte)(mCal.get(Calendar.MINUTE)), 
				(byte)(mCal.get(Calendar.SECOND)),
				(byte)(mCal.get(Calendar.WEEK_OF_MONTH))
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
    
    public ArrayList<byte[]> getDateTimeList(ArrayList<byte[]> dataList)
    {
	ArrayList<byte[]> DateList = new ArrayList<>();
	
	for (int i=0; i<dataList.size(); i++)    
        //for (int i=0; i<40; i++)    
        {
            //rcdCounts = 0;
            int leng = dataList.get(i).length;
            byte[] tmpDate = new byte[5];
            
            //if(leng > 8)
            //{
            //	rcdCounts = (leng - 8)/3;
            //}
            //System.out.println("dataParser(), data[" + i + "] length: " + leng + ", rcdCounts: " + rcdCounts);
           	   
            for(int j=0; j<5; j++)
            {
        	tmpDate[j] = dataList.get(i)[j];
            }
            
            //tmpDateList.add(tmpDate);
            for(int k=0; k<=(leng - 8)/3; k++)
            {
        	if(k>0) 
        	{
        	    tmpDate[4]++;
        	    if(tmpDate[4] > 0x3B)	//over 60 minute
        	    {
        		tmpDate[3]++;		// hour increase
        		tmpDate[4] -= 0x3C;	// minute minus 60
        	    }
        	}
        	
        	byte[] tmpNewDate = tmpDate.clone();
        	System.out.println("getDateTimeList(), dateTime[" + k + "]: " + getHexToString(tmpDate));
        	DateList.add(tmpNewDate);
            }   
        }
	
	//System.out.println("debugPrintList() ...");
	debugPrintList(DateList);
	
	return DateList;
    }
    
    public ArrayList<Integer> getTemperatureList(ArrayList<byte[]> dataList)
    {
	ArrayList<Integer> temperatureList = new ArrayList<>();
	
	for (int i=0; i<dataList.size(); i++)    
	//for (int i=0; i<40; i++)    
	{
	    int leng = dataList.get(i).length;
	    
	    for(int j=0; j<=((leng - 8)/3); j++)
            {
            	int tmpInt = 0;
            	tmpInt = byteToUnsignedInt(dataList.get(i)[5 + j*3]) * 100 + 
            		 byteToUnsignedInt(dataList.get(i)[6 + j*3]);
            	temperatureList.add(tmpInt);
            }
	}
	
	debugPrintList1("temperatureList()", temperatureList);
	return temperatureList;
    }
    
    /*
    ArrayList<byte[]> tmpDateList = new ArrayList<>();
    ArrayList<Integer> temperatureList = new ArrayList<>();
    public void dataParser(List<byte[]> data)
    {
	tmpDateList.clear();
	temperatureList.clear();
	int rcdCounts;
	
	for (int i=0; i<data.size(); i++)    
	//for (int i=0; i<40; i++)    
	{
	    rcdCounts = 0;
	    int leng = data.get(i).length;
	    byte[] tmpDate = new byte[5];
	    
	    if(leng > 8)
	    {
	    	rcdCounts = (leng - 8)/3;
	    }
	    //System.out.println("dataParser(), data[" + i + "] length: " + leng + ", rcdCounts: " + rcdCounts);
	   	   
	    for(int j=0; j<5; j++)
	    {
		tmpDate[j] = data.get(i)[j];
	    }
	    
	    //tmpDateList.add(tmpDate);
	    for(int k=0; k<=(leng - 8)/3; k++)
	    {
		if(k>0) tmpDate[4]++;
		//byte[] tmpNewDate = new byte[tmpDate.length];
		//tmpNewDate = tmpDate.clone();
		byte[] tmpNewDate = tmpDate.clone();
		System.out.println("dataParser(), dateTime: " + getHexToString(tmpDate));
        	tmpDateList.add(tmpNewDate);        	
      	    }
	    
	    for(int j=0; j<=((leng - 8)/3); j++)
	    {
		int tmpInt = 0;
		tmpInt = byteToUnsignedInt(data.get(i)[5 + j*3]) * 100 + 
			byteToUnsignedInt(data.get(i)[6 + j*3]);
		temperatureList.add(tmpInt);
	    }
	}
	
	debugPrintList(tmpDateList);
	//getSecondList(tmpDateList);
	dtTosecond(tmpDateList);
		
	System.out.printf(" Date time List size: %04d, temperature List size: %04d \n", 
		tmpDateList.size(), temperatureList.size());
	
	//debugPrintList1(temperatureList);
    }
    */
    
    //public ArrayList<byte[]> foundHourList(ArrayList<byte[]> timeList)
    public ArrayList<Integer> foundHourList(ArrayList<byte[]> timeList)
    {
	ArrayList<byte[]> hourList = new ArrayList<>();
	ArrayList<Integer> hourIndexList = new ArrayList<>();
	
	System.out.printf("hour List size: %02d%n", hourList.size());
	for(int i=0; i<timeList.size(); i++)
	{
	    if((timeList.get(i)[4] & 0xff) == 0)
	    {
		hourList.add(timeList.get(i));
		hourIndexList.add(i);
	    }    
	}
	
	for(int i=0; i<hourList.size(); i++)
	{
	    System.out.printf("hour List[%02d]: %s%n", i, getHexToString(hourList.get(i)));
	}
	//return hourList;
	return hourIndexList;
    }
    
    public ArrayList<Long> getSecondList(ArrayList<byte[]> timeList)
    {
	ArrayList<Long>  secondList = new ArrayList<>();
	
	long baseScand = ((byteToUnsignedInt(timeList.get(0)[3]) * 60) + 
			byteToUnsignedInt(timeList.get(0)[4])) * 60;
	
	System.out.printf("timeList.get(0)([3], [4]): (%02Xh, %02Xh) = %08d \n", 
		timeList.get(0)[3], timeList.get(0)[4], baseScand);
	
	for (int i=0; i<timeList.size(); i++) 
	{	    	    
	    long tmpScand = ((byteToUnsignedInt(timeList.get(i)[3]) * 60) + 
			byteToUnsignedInt(timeList.get(i)[4])) * 60;		
	    if((tmpScand - baseScand) < 0 )
	    {
		long tmpH = ((24 - byteToUnsignedInt(timeList.get(0)[3])) +
				byteToUnsignedInt(timeList.get(i)[3])) * 3600;
		tmpScand += tmpH;
	    }
	    secondList.add(tmpScand - baseScand);
	}
	
	debugPrintList1("Second", secondList);
	return secondList;
    }
    
    //public void dtTosecond(ArrayList<byte[]> data)
    public ArrayList<Date> dtTosecond(ArrayList<byte[]> data)
    {
		 //Date today = new Date(); 
		ArrayList<Date> dateList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		
		for(int i=0; i<data.size(); i++)
		//for(int i=0; i<40; i++)
		{
		    String tmpDate = String.format("%02d%02d%02d%02d%02d", data.get(i)[0], 
			    data.get(i)[1], data.get(i)[2], data.get(i)[3], data.get(i)[4]);
		    Date tmpTime = new Date();
		    try 
		    {
		    	tmpTime = sdf.parse(tmpDate);
		    } 
		    catch (ParseException e) 
		    {
		    	// TODO Auto-generated catch block
		    	e.printStackTrace();
		    }
		   
		    dateList.add(tmpTime);
		    System.out.printf("date[%d]: %s, minutes: %d %n", i, sdf.format(tmpTime), 
		    		TimeUnit.MILLISECONDS.toMinutes(tmpTime.getTime()));
		}
		
		return dateList;
		
		/*
		 //SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
		 //System.out.println("系統時間為:" + today );
		 //System.out.println("時間格式調整後:" + sdf.format(today));
		
		ArrayList<Long> diffSecondList = new ArrayList<>();
		for(int i=0; i<dateList.size(); i++)
		//for(int i=0; i<40; i++)
		{
		    long tmpDiffSec = dateList.get(i).getTime() - dateList.get(0).getTime();
		    diffSecondList.add(tmpDiffSec);
		    System.out.printf("diff Sec[%02d]: %d%n", i, tmpDiffSec/1000);
		}
		*/
    }
    
    public void debugPrintList(List<byte[]> data)
    {
	for(int i=0; i<data.size(); i++)
	{
	    System.out.println("List[" + i +"]: " + getHexToString(data.get(i)));
	}
    }
    
    public void debugPrintList1(String msg ,ArrayList<?> data)
    {
	System.out.printf(" %s List size: %04d%n", msg, data.size());
	for(int l=0; l<data.size(); l++)
	{
	    System.out.format("%s, List[%04d]: %3d%n", msg, l, data.get(l));
	    //System.out.printf("%3d", 123456789);
	    //System.out.println(msg + ", List[" + l + "]: " + (long)data.get(l));
	}
    }
    
    
    public void separateTime(List<byte[]> data)
    {
	ArrayList<byte[]> timeList = new ArrayList<>();
	for (int i=0; i<data.size(); i++)    
	{
	    int leng = data.get(i).length-5;
	    byte[] tmp = new byte[leng];
	    for(int j=0; j<leng; j++)
	    {
		tmp[j] = data.get(i)[5+j];
	    }
	    timeList.add(tmp);
	    System.out.println("separateTime(), tmp: " + getHexToString(tmp));
	}
    }
    
    int cnt=0;
    public ArrayList<Integer> covertTmp(List<byte[]> data)
    {
	int size = data.size();
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
