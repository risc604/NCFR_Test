package src;

/**
 * 
 */

/**
 * @author tomcat
 *
 */
public class testMain 
{
    /**
     * 
     */
    public testMain() 
    {
	// TODO Auto-generated constructor stub
    }
    
    public static void testCalculateDT() 
    {
		// TODO Auto-generated method stub
		CalculateDateTime	testDT = new CalculateDateTime();
    }
    
    public static void testDateLocale() 
    {
		// TODO Auto-generated method stub
		DateLocale	testDate = new DateLocale();
    }
    
    public static void testCScheckSum() 
    {
		// TODO Auto-generated method stub
		CScheckSum	testData = new CScheckSum();	
    }
    
    public static void testParserNCFRData() 
    {
		// TODO Auto-generated method stub
		ParserRawData	ncfr = new ParserRawData();
		
		ncfr.parserList();
		ncfr.stringToHex();
    }
    
    public static void testWriteLogFile() 
    {
    	logFileObject	filelog = new logFileObject();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
    	//testParserNCFRData();
	
    	//testWriteLogFile();
    	//logFileObject	filelog = new logFileObject();
    	//System.out.println("filelog: " + filelog);
    	
    	//--- DB List test.
    	//DBListTest	dbList = new DBListTest();
    	//System.out.println("oneRawData: " + dbList.getOneRawData());
    	
    	//--- DB data parser
    	//DBParser	dbParserData = new DBParser();
    	//System.out.println("oneRawData: " + dbParserData.toString());
    	
    	//--- CScheckSum test
    	//testCScheckSum();
    	
    	//--- Date local test
    	//testDateLocale();
    	
    	//--- Calculate Date Time
    	testCalculateDT();
    	
    	// TODO Auto-generated method stub
    	System.out.println("Hello Java !");
    }

}
