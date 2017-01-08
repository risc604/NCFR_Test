package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBParser 
{
	private String dbData = "4D510022A38011010815360008185C00186200186300186300186300186100185D00185B0010";
	private String startTime = "1101081536";
	private String endTime = "";
	private List<String> timeStrList;
	private List<Integer> intTemp;
	private List<byte[]> rangeTimeList;
	
	public DBParser() 
	{
		byte[] byteTemp = removeHeader(dbData);
		intTemp = new ArrayList<>();
		intTemp = getTemperatureList(byteTemp);
		
		endTime = Utils.calculateEndTime(intTemp.size(), startTime);
		System.out.println("endTime: " + endTime);
		
		timeStrList = new ArrayList<>();
		timeStrList.add(startTime);
		timeStrList.add(endTime);
		rangeTimeList = getTimeList(timeStrList);
		System.out.println("rangeTimeList size: " + rangeTimeList.size());

		System.out.println("DBParser constructor ...");
	}
	
	private byte[] removeHeader(String dataSrc)
	{
		byte[] data = Utils.hexStringToByteArray(dataSrc);
		String dateTime = Utils.convertArrayToString(data, 6, 5);
		System.out.println("dateTime: " + dateTime);
		
        int records = (Utils.byteToUnsignedInt(data[11]) * 256) + Utils.byteToUnsignedInt(data[12]);
        String temperature = Utils.convertArrayToString(data, 13, (records * 3));
        System.out.println("temperature: " + temperature);
        
        //byte[] byteTemp = Utils.hexStringToByteArray(temperature);
        
        return Utils.hexStringToByteArray(temperature);
	}
	
	//private ArrayList<Integer> getTemperatureList(ArrayList<byte[]> data)
	private ArrayList<Integer> getTemperatureList(byte[] data)
    {
        //int size = data.size();
        int size = data.length;
        ArrayList<Integer> tmplist = new ArrayList<>();
        //Log.d(TAG, "getTemperatureList(), data list size: " + size );
        System.out.println("getTemperatureList(), data list size: " + size );
        
        //--- calculate temperature to integer list.
        //for(int i=0; i<size; i++)
        {
            int tmp = 0;
            int leng = data.length;
            //Log.d(TAG, " raw data[" + i +"], lengh: " + leng);
            System.out.println(" raw data lengh: " + leng);

            for(int j=0; j<(leng/3); j++)
            {
                int idx= (j*3);

                if (data[idx+2] == 00)
                {
                	tmp = Utils.byteToUnsignedInt(data[0 + idx]) * 100 + Utils.byteToUnsignedInt(data[1+idx]);
                }
                else
                	System.out.println("Error!! this record data fail. not 00, is " + data[idx+0] + data[idx+1] + data[idx+2]);
                System.out.printf("tmp[%02d]: %04d, [%02d] = %02d %n", j, tmp, idx+2, data[idx+2]);
                tmplist.add(tmp);
            }
        }

        //Log.d(TAG, "tmplist size:" + tmplist.size());
        //Log.d(TAG, "getTemperatureList(), tmplist size:" + tmplist.size() +
        //        ", tmpList[0]: " + tmplist.get(0) +
        //        ", tmpList[" + (tmplist.size() - 1) + "]: " + tmplist.get(tmplist.size()-1) );

        //--- debug message
        //for(int i=0; i<tmplist.size(); i++)
        //{
        //    Log.d(TAG, "tmplist[" + i + "]: " + tmplist.get(i));
        //}

        return tmplist;
    }
	
	//private List<byte[]> getTimeList(List<byte[]> timeList)
	private List<byte[]> getTimeList(List<String> timeList)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDDHHmm");
		List<byte[]> tmpList = new ArrayList<>();
		Date[]	tmpDate = new Date[timeList.size()];
		Calendar[] calendar = new Calendar[timeList.size()];
		
		for (int i=0; i<timeList.size(); i++) 	//debug
		{
			System.out.printf("timeList[%02d]: %s %n", i, timeList.get(i));
			byte[] tmpByte = Utils.hexStringToByteArray(timeList.get(i));
			try 
			{
				String tmpString = String.format("%04d%02d%02d%02d%02d",
									(tmpByte[0]+2000), tmpByte[1], tmpByte[2], tmpByte[3], tmpByte[4]);
				tmpDate[i] = sdf.parse(tmpString);
				System.out.printf("tmpDate[%02d]: %s %n", i, tmpDate[i].toString());
			} 
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			calendar[i] = Calendar.getInstance();
			calendar[i].setTime(tmpDate[i]);
			System.out.printf("calendar[%02d]: %s %n", i, calendar[i]);
				
			//tmpList.add(Utils.hexStringToByteArray(timeList.get(i)));
		}
		
		//int tmpRecords = calendar[1].getTime().getDate() - calendar[0].getTime().getDate();
		long tmpRecords = tmpDate[1].getTime() - tmpDate[0].getTime();
		System.out.printf("tmpRecords: %02d %n", tmpRecords);
		
		for (int i=0; i<tmpRecords; i++)
		{
			byte[] tmpByte = new byte[5];
			//tmpByte[0] = 
		}
		
		
		return tmpList;
		
	}
	

}
