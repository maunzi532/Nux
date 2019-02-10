package m.nuxc2.state;

import java.util.*;

public class Entry2
{
	public int endLine;
	public String name;
	public ArrayList<Tag2> tags = new ArrayList<>();
	public ArrayList<String> errors = new ArrayList<>();

	public Entry2(String text, int line)
	{
		String[] ta = text.split("<");
		int[] al = new int[ta.length];
		for(int i = 0; i < ta.length; i++)
		{
			for(int j = 0; j < ta[i].length(); j++)
				if(ta[i].charAt(j) == '\n')
					al[i]++;
			if(ta[i].charAt(ta[i].length() - 1) == '\n')
				ta[i] = ta[i].substring(0, ta[i].length() - 1);
		}
		name = ta[0];
		for(int i = 1; i < ta.length; i++)
		{
			Tag2 t = new Tag2(ta[i], line);
			tags.add(t);
			if(!t.errors.isEmpty())
				errors.addAll(t.errors);
			line += al[i];
		}
		endLine = line;
	}
}