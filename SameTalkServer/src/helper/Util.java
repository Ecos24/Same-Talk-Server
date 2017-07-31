package helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util
{
	// To display time in hh:mm:ss
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");

	/**
	 * Display an event (not a message) to the console or the GUI
	 * @param msg
	 */
	public static void displayEvent(String msg)
	{
		String time = sdf.format(new Date()) + " " + msg;
		System.out.println(time);
	}
}
