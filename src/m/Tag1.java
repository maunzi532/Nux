package m;

import java.util.*;

public class Tag1
{
	public static HashMap<String, Tag1Type> map = new HashMap<>();

	public static void init()
	{
		for(Tag1Type t : Tag1Type.values())
			map.put(t.name(), t);
	}

	public Tag1Type type;
	public String value;
	public int intvalue;

	public Tag1(String text)
	{
		int c2 = text.indexOf('>');
		if(c2 < 0)
		{
			System.out.println("TAG ERROR " + text);
			return;
		}
		type = map.get(text.substring(0, c2).toUpperCase());
		if(type == null)
		{
			System.out.println("Invalid Tag: <" + text);
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
					System.out.println("Num Error\n" + Arrays.toString(e.getStackTrace()));
				}
		}
	}
}