package src;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class ParserRawData 
{
    final String HEXES = "0123456789ABCDEF";
    
    byte[]	A0RevData;
    byte[]	A1RevData;
    byte[]	A2RevData;
    byte[]	A0RevDataError;
    byte[]	csErrorInfo;
    List<byte[]>	rawDataList = new ArrayList<>();
    
    public ParserRawData() 
    {
    	// TODO Auto-generated constructor stub
    	rawDataList.clear();
    	makeReceiveData();
    }
    
    public void makeReceiveData()
    {
    	A0RevData = new byte[]{	0x4D, 0x41, 0x00, 0x0A, (byte)0xA0, 0x09, (byte)0xC4, 0x0E, 
				0x74, 0x19, (byte)0x88, 0x24, 0x10, (byte)0x5C};
		//A0RevData[A0RevData.length-1] = countCS(A0RevData);
		
		A0RevDataError = new byte[]{	0x4D, 0x41, 0x00, 0x0A, (byte)0xA0, (byte)0xFF, (byte)0xFF, (byte)0xFF, 
						(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x81, (byte)0xB2};
		//A0RevDataError[A0RevDataError.length-1] = countCS(A0RevDataError);
		
		A1RevData = new byte[]{	0x4D, 0x41, 0x00, 0x0A, (byte)0xA1,  0x18, 0x7A, (byte)0x93, 
					0x02, (byte)0xCE, 0x0E, 0x00, (byte)0xC8, (byte)0x02};
		//A1RevData[A1RevData.length-1] = countCS(A1RevData);
			
		A2RevData = new byte[]{	0x4D, 0x41, 0x00, 0x0A, (byte)0xA2, 0x1B, (byte)0xF0, 0x17, 0x20, 
					0x0E, 0x38, 0x09, (byte)0xC4, (byte)0x8F };
		//A2RevData[A2RevData.length-1] = countCS(A2RevData);
		
		csErrorInfo = new byte[]{	0x4D, 0x41, 0x00, 0x0A, (byte)0xA1, 0x18, (byte)0x7A, (byte)0x93, 
						0x02, (byte)0xBF, (byte)0x89, 0x00, (byte)0xB4, (byte)0x9c};
		//csErrorInfo[csErrorInfo.length-1] = countCS(csErrorInfo);
		
		rawDataList.add(A1RevData);
		rawDataList.add(A0RevData);
		rawDataList.add(A0RevDataError);
		rawDataList.add(A2RevData);
		rawDataList.add(csErrorInfo);
		System.out.println("make demo recevice data Ok.");
    }
    
    public void parserList()
    {
    	if (rawDataList.size() > 0) 
    	{
    		System.out.println("\n");
            for (int i=0; i<rawDataList.size(); i++)
            {
                System.out.print("rawDataList[" + i + "]:" + getHexToString(rawDataList.get(i)));
                if(checkSumState(rawDataList.get(i)))
                    parserRawData(rawDataList.get(i));
            }
            System.out.println("paser rawDataList ok.");
    	}
    }
    
    private void parserRawData(byte[] dataInfo)
    {
        String  A0Message = "";
        String  A1Message = "";
        String  A2Message = "";
        
        switch (dataInfo[4])
        {
            case (byte) 0xA0:
                if ((dataInfo[12] & 0x0080) == 0x0080)  //check Error
                {
                    A0Message = errorMessage((byte) (dataInfo[12] & 0x003F));
                }
                else
                {
                    int     tmpIntWord = byte2Word(dataInfo[5], dataInfo[6]);
                    String  ambient = getTemperature(tmpIntWord);
                    String  workModeStr = workMode((byte) ((dataInfo[7] & 0x0080) >>> 7));

                    tmpIntWord = byte2Word((byte) (dataInfo[7] & 0x007F), dataInfo[8]);
                    String  measure = getTemperature(tmpIntWord);
                    String  ncfrDate = measureTime((dataInfo[9]), dataInfo[10], dataInfo[11],
                                                    (byte) (dataInfo[12] & 0x003F));
                    String  feverState = ((dataInfo[12] & 0x0040) == 0x0040) ? "fever" : "no fever";

                    A0Message = "Amb=" + ambient + ", " + workModeStr + "= " +
                                measure + ",\r\n" + feverState +", " + ncfrDate;
                }
                break;

            case (byte) 0xA1:
                A1Message = getMACAddress(dataInfo) +
                            workMode(dataInfo[11]) + ", " + batteryState(dataInfo[12]);
                break;

            case (byte) 0xA2:
                int     CA2Intemp = byte2Word(dataInfo[5], dataInfo[6]);
                String  CA2Parameter = String.format("CA2 = %04Xh = %04d", CA2Intemp, CA2Intemp);

                int     CA3Intemp = byte2Word(dataInfo[7], dataInfo[8]);
                String  CA3Parameter = String.format("CA3 = %04Xh = %04d", CA3Intemp, CA3Intemp);

                int     CA3Vol = byte2Word(dataInfo[9], dataInfo[10]);
                float   tmpVoltage = ((float)CA3Vol / 1000.0f);
                String  CA3Voltage = String.format("CA3 Voltage = %4.3f uV", tmpVoltage);

                String  CA3Ambient = getTemperature(byte2Word(dataInfo[11], dataInfo[12]));

                A2Message = CA2Parameter + ", " + CA3Parameter + "\n"
                          + CA3Voltage + ", CA3 temp: " + CA3Ambient;
                break;

            default:
                break;
        }
        System.out.println(A1Message + A0Message + A2Message + "\r\n");
    }

    private String getMACAddress(byte[] data)
    {    	
    	return String.format("MAC %02X:%02X:%02X:%02X:%02X:%02X\t",
    						(byte)data[5], data[6], data[7], data[8], data[9], data[10]);
    }
    
    private String getMACAddress2(byte[] data)
    {    	
    	String	macAddr = new String();
    	
    	for(int i=0; i<6; i++)
    	{
    		macAddr += String.format("%02X", (byte)data[5+i]);
    		if(i<5) macAddr += String.format("%c", ':');
    	}
    	System.out.println("MAC Address: " + macAddr);
    	return	("MAC addr: " + macAddr + "\t "); 
    }
    
	private String workMode(byte mode)
    {
        String[] tmpStr= new String[]{"Body", "Object", "Memory", "CAL"};

        if ((mode & 0x0080) == 0x80)  mode = 0x01;
        else if (mode > tmpStr.length)
            return ("error mode, code: " + mode);

        //return (tmpStr[mode & 0x00ff] + " mode");
        String tmpMode = tmpStr[mode & 0x00ff] + " mode";
        //Log.d("work Mode()", "work mode: " + tmpMode);
        System.out.println("work Mode(), work mode: " + tmpMode);
        return tmpMode;
    }

    private String batteryState(byte deviceBatt)
    {
	System.out.println(String.format("deviceBatt: %d, %04Xh",  (deviceBatt&0x00FF) , deviceBatt));
        float   BatteryVoltage = ((float) ((int)(deviceBatt & 0x00ff) + 100) / 100.0f);
        String  tmpString = String.format("%4.3fV", BatteryVoltage);
        //Log.d("batteryState", "Batter Voltage: " + tmpString);
        System.out.println("batteryState(), Batter Voltage: " + tmpString);
        return (tmpString);
    }

    private int byte2Word(byte dataH, byte dataL)
    {
        int     tmpValue=0;

        tmpValue |= (int) (dataH & 0x00ff);
        tmpValue <<= 8;
        tmpValue |= (int) (dataL & 0x00ff);
        //Log.d("byte2Word", " merge 2 byte: " + tmpValue);
        System.out.println( String.format("byte2Word(), merge 2 byte: %04Xh, %04d",
        			(tmpValue & 0x0000ffff), tmpValue));
        return (tmpValue & 0x0000ffff);
    }

    private String getTemperature(int value)
    {
        float   ftmp = ((float) value) / 100;
        String  tmpString = String.format("%4.2f�J", ftmp);
        //Log.d("getTemperature", " Temperature: " + tmpString);
        System.out.println("getTemperature(), Temperature: " + tmpString);
        return(tmpString);
    }

    private String measureTime(byte mDay, byte mHour, byte mMinute, byte mYear)
    {
        if (((mDay & 0x00FF) == 0xFF) || ((mHour & 0x00FF) ==0xFF) || ((mMinute & 0x00FF)==0xFF))
        {
            return("Date/Time some one is 0xFF");
        }

        byte tmpMonth = 0;
        byte tmpYear = (byte)(mYear & 0x003F);
        byte tmpHour = (byte)(mHour & 0x003F);
        byte tmpDay =  (byte)(mDay & 0x003F);

        tmpMonth |= ((mHour & 0x00C0) >>> 2);
        tmpMonth |= (byte) (mDay & 0x00C0);
        tmpMonth >>= 4;

        return(String.format("%04d/%02d/%02d, %02d:%02d",
                ((int)tmpYear + 2000), (int)tmpMonth, (int)tmpDay, (int)tmpHour, (int)mMinute));
    }

    private String errorMessage(byte info)
    {
        String[]  ErrorMessage = {"amb H", "amb L", "body H", "body L"};

        if (info > ErrorMessage.length)
            return ("Unknown, Error! Code: " +  Integer.toHexString(info & 0x003f));
        else
            return ("Error for " + ErrorMessage[(info & 0x003f)] );
    }
    
    private int countCS(byte[] data)
    {
	int tmpCS=0;
	
	for (int i=0; i<(data.length-1); i++) 
	{
	    tmpCS += Byte.toUnsignedInt(data[i]);
	}
	//System.out.println(String.format("countCS(): %04Xh", (tmpCS & 0x00ff)));
	return (tmpCS);
    }
    
    private boolean checkSumState(byte[] data)
    {
	int tmpCS=0;
	
	tmpCS = countCS(data);
	System.out.printf(String.format("\t checkSumState(): %02Xh (%04Xh)", (tmpCS & 0x00FF), tmpCS));
	if ((tmpCS & 0x00ff) == (data[data.length-1] & 0x00ff))
	{    
	    System.out.println();
	    return true;
	}
	else
	{
	    System.out.println(",  check sum error !");
	    return false;
	}
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
    
    public String getHexToString(byte[] raw)
    {
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
    
    public void stringToHex() 
    {
	String s = "0x56 0x49 0x4e 0x31 0x32 0x33 0x46 0x4f 0x52 0x44 0x54 0x52 0x55 0x43 0x4b 0x00 0x38";
	StringBuilder sb = new StringBuilder();
	String[] components = s.split(" ");
	for (String component : components) {
	    int ival = Integer.parseInt(component.replace("0x", ""), 16);
	    sb.append((char) ival);
	}
	//String string = sb.toString();
	//for(int i=0; i<sb.length(); i++)
	    System.out.println("sb:" + sb.toString());
    }
}

