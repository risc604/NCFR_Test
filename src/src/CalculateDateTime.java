package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class CalculateDateTime 
{
	public CalculateDateTime() 
	{
		// TODO Auto-generated constructor stub
		int[] tmpIntArrays = convertDTtoInt("1103070F2B");
		String endDTString = calcuteEndDT(tmpIntArrays, 85);
		
		System.out.println("endDTString " + endDTString);
	}
	
	
	private int[] convertDTtoInt(String srcDT) 
	{
		byte[]	byteArrayDT = Utils.hexStringToByteArray(srcDT); 
		int[]	intArrayDT = new int[byteArrayDT.length];
		
		for (int i=0; i<byteArrayDT.length; i++) 
		{
			intArrayDT[i] = Utils.byteToUnsignedInt(byteArrayDT[i]);
		}
		
		System.out.println("Date Time int[]: " + Arrays.toString(intArrayDT));
		
		return intArrayDT;
	}
	
	private String calcuteEndDT(int[] srcArray, int loops)
	{
		String tmpString = String.format("%04d%02d%02d%02d%02d",
                (srcArray[0]+2000), srcArray[1], srcArray[2], srcArray[3], srcArray[4]);
        System.out.println("calculateEndTime(), date Time String: " + tmpString);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date startDateTime = null;
		
        try
        {
            startDateTime = sdf.parse(tmpString);
            System.out.println("startDateTime: " + startDateTime.toString());
        }
        catch (java.text.ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        int tmpLong = startDateTime.getMinutes() + loops;
        System.out.println(startDateTime.getMinutes() + " + " + loops + " = tmpLong: " + tmpLong);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateTime);
        System.out.println("calendar, start Time date: " + calendar.getTime().toString());

        calendar.add(Calendar.MINUTE, loops);//minute + 156
        System.out.println("calendar, add records: " + calendar.getTime().toString());

        int[] intDTime = new int[5];
        intDTime[0] = calendar.getTime().getYear() + 1900 - 2000;
        intDTime[1] = calendar.getTime().getMonth()+1;
        intDTime[2] = calendar.getTime().getDate();
        intDTime[3] = calendar.getTime().getHours();
        intDTime[4] = calendar.getTime().getMinutes();
        System.out.println(	"Year: " + intDTime[0] + ", Month: " + intDTime[1] + ", Date: " + intDTime[2] +
                    		", Hour: " + intDTime[3] + ", Minute: " + intDTime[4]);

        //lastTime = String.format("%04d%02d%02d%02d%02d",
        //		intDTime[0], intDTime[1], intDTime[2], intDTime[3], intDTime[4]);
        String lastTime = String.format("%02X%02X%02X%02X%02X", intDTime[0], intDTime[1], intDTime[2], 
        								intDTime[3], intDTime[4]);
        System.out.println("lastTime : " + lastTime);

		return lastTime;
	}
		

}
