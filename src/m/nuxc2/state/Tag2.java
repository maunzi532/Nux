package m.nuxc2.state;

import java.util.*;

public class Tag2
{
	public Tag2Type type;
	public String value;
	public int intvalue;
	public int line;
	public ArrayList<String> errors = new ArrayList<>();

	public Tag2(String text, int line)
	{
		this.line = line;
		int c2 = text.indexOf('>');
		if(c2 < 0)
		{
			errors.add("Kein Tag-Ende " + text + ", Zeile " + line);
			return;
		}
		try
		{
			type = Tag2Type.valueOf(text.substring(0, c2).toUpperCase());
		}catch (IllegalArgumentException e)
		{
			errors.add("Unbekannter Tag: <" + text + ", Zeile " + line);
			return;
		}
		if(type.t2 > 0)
		{
			value = text.substring(c2 + 1);
			if(value.length() > 0 && value.charAt(value.length() - 1) == '\n')
				value = value.substring(0, value.length() - 1);
			if(type.t2 == 2)
				try
				{
					intvalue = Integer.parseInt(value);
				}catch(NumberFormatException e)
				{
					errors.add("Keine Zahl: " + value + ", Zeile " + line);
				}
		}
	}
}