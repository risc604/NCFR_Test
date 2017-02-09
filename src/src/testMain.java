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
    	testCScheckSum();
    	
    	// TODO Auto-generated method stub
    	System.out.println("Hello Java !");
    }

}
