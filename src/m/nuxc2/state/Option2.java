package m.nuxc2.state;

import java.util.*;

public class Option2 implements Markable
{
	public String ziel;
	public ArrayList<String> give = new ArrayList<>();
	public boolean possible;

	public Option2(String ziel)
	{
		this.ziel = ziel;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Option2)) return false;

		Option2 option2 = (Option2) o;

		return ziel.equals(option2.ziel);
	}

	@Override
	public int hashCode()
	{
		return ziel.hashCode();
	}
}