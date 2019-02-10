package m;

import java.util.*;

public class Entry1
{
	String name;
	ArrayList<Tag1> tags = new ArrayList<>();

	public Entry1(String text)
	{
		int c1 = text.indexOf('\n');
		if(c1 <= 0)
		{
			name = text;
			return;
		}
		name = text.substring(0, c1);
		String[] ta = text.substring(c1 + 2).split("<");
		for(int i = 0; i < ta.length; i++)
			tags.add(new Tag1(ta[i]));
	}
}