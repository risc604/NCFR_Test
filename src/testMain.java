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

    
    public static void testParserNCFRData() 
    {
	// TODO Auto-generated method stub
	ParserRawData	ncfr = new ParserRawData();
	
	ncfr.parserList();
	ncfr.stringToHex();
    }
    
    
    public static void testWriteLogFile() 
    {
	writeLogFile	filelog = new writeLogFile();
    }
    /**
     * @param args
     */
    public static void main(String[] args) 
    {
	//testParserNCFRData();
	
	testWriteLogFile();
	
	// TODO Auto-generated method stub
	System.out.println("Hello Java !");
    }

}
