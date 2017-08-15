package helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *  Class to Read project Properties form .properties file.
 */
public class ReadFromPropertiesFile
{
	private static ResourceBundle res;

    public static void readyProperties()
    {
    	res = ResourceBundle.getBundle("adminDetails");
    }

    /**
     * Gives the value corresponding to parameter key in dbProperties File.
     * @param key
     * @return
     */
    public static String getProp(String key)
    {
        return res.getString(key);
    }
    
    public static void setProp(String key, String value) throws IOException
    {
    	FileInputStream in = new FileInputStream("src/adminDetails.properties");
    	Properties props = new Properties();
    	props.load(in);
    	in.close();

    	FileOutputStream out = new FileOutputStream("src/adminDetails.properties");
    	props.setProperty(key, value);
    	props.store(out, null);
    	out.close();
    	
    	readyProperties();
    }
}
