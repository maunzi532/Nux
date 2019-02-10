package m;

import java.util.*;

public class Option1
{
	public String ziel;
	public String text;
	public ArrayList<String> give = new ArrayList<>();

	public Option1(String ziel)
	{
		this.ziel = ziel;
	}

	public void addText(String t1)
	{
		if(text == null)
			text = t1;
		else
			text = text + t1;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Option1)) return false;

		Option1 option = (Option1) o;

		return ziel.equals(option.ziel);
	}

	@Override
	public int hashCode()
	{
		return ziel.hashCode();
	}
}