package m.nuxc2;

import java.util.*;
import m.nuxc2.state.*;

public class EntrySplitter
{
	public String start;
	public HashMap<String, Entry2> entries = new HashMap<>();
	public ArrayList<String> errors = new ArrayList<>();

	public EntrySplitter(String input)
	{
		input = input.trim();
		int line = 0;
		String[] a1 = input.split("\n\n");
		for(String a2 : a1)
		{
			Entry2 entry2 = new Entry2(a2, line);
			if(entries.containsKey(entry2.name))
				errors.add("Doppelter Schlüssel: " + entry2.name + ", Zeile " + line);
			else
				entries.put(entry2.name, entry2);
			errors.addAll(entry2.errors);
			if(start == null)
				start = entry2.name;
			line = entry2.endLine + 2;
		}
	}
}