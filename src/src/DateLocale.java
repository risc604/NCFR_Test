package src;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateLocale 
{
	public DateLocale() 
	{
		makeDateData();
	}
	
	private String makeDateData()
	{
		int[]	dateNow = currentDateTime();
		String dateString = String.format("%04d/%02d/%02d %02d:%02d:%02d", 
				dateNow[0], dateNow[1], dateNow[2], dateNow[3], dateNow[4], dateNow[5]);
		System.out.println( "dateString: " + dateString);
		
		format00(dateString);
		
		for (int i=0; i<10; i++) 
		{		
			format01(dateString, i);
		}
		
		return ("OK...");
	}
	

    private int[] currentDateTime()
    {
        Calendar    mCal = Calendar.getInstance();
        int[]       tmp = new int[7];

        tmp[0] = mCal.get(Calendar.YEAR);
        tmp[1] = mCal.get(Calendar.MONTH)+1;
        tmp[2] = mCal.get(Calendar.DATE);
        tmp[3] = mCal.get(Calendar.HOUR_OF_DAY);
        tmp[4] = mCal.get(Calendar.MINUTE);
        tmp[5] = mCal.get(Calendar.SECOND);
        tmp[6] = mCal.get(Calendar.WEEK_OF_MONTH);
        return tmp;
    }
    
    private String format00(String srcDate)
    {
    	//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.getDefault());
	    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.FRANCE);
	    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.FRANCE);
	    //String formatted = simpleDateFormat.format(900000);
	    try 
	    {
	    	//System.out.println(simpleDateFormat.parse(formatted));
	    	System.out.println("format00(): " + simpleDateFormat.parse(srcDate));
	    	return simpleDateFormat.parse(srcDate).toString();
	    } 
	    catch (ParseException e) 
	    {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	    
	    return "";
    }
    
    private String format01(String srcDate, int mode) 
    {
		// TODO Auto-generated method stub
    	
    	Locale	local=null;
    	//String	message = "";
    	
    	//DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
    	switch (mode) 
    	{
			case 0:
				local = new Locale("en", "US");
				//message = local.toString();
				break;
				
			case 1:
				local = new Locale("fr", "FRANCE");
				break;
				
			case 2:
				local = new Locale("de", "GERMANY");
				break;	

			case 3:
				local = new Locale("ja", "JAPAN");
				break;
				
			case 4:
				local = new Locale("fi", "FI");
				break;	
			
			case 5:
				local = new Locale("it", "ITALY");
				break;	
				
			case 6:
				local = new Locale("zh", "TRADITIONAL_CHINESE");
				break;	
				
			case 7:
				local = new Locale("ru", "RU");
				break;	
				
			case 8:
				local = new Locale("sk", "SK");
				break;	
				
			case 9:
				local = new Locale("hu", "HU");
				break;	
				
			default:
				break;
		}
    	
    	DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, local);
    	//Date dt = new Date();
    	//String formattedDate = f.format(dt, "yyyy/MM/DD hh:mm:ss", "");
    	String formattedDate = f.format(new Date());
   
    	System.out.println("format01(), Date: " + formattedDate + ",\t" + local.toString());
    	
    	return formattedDate;
	}

}
