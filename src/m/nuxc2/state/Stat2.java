package m.nuxc2.state;

import javax.annotation.*;

public class Stat2 implements Comparable<Stat2>, Markable
{
	public String name;
	public int wert;
	public Integer atleast;
	public String atlt;
	public Integer atmost;
	public String atmt;

	public Stat2(String name)
	{
		this.name = name;
	}

	public Stat2(Stat2 copy)
	{
		name = copy.name;
		wert = copy.wert;
		atleast = copy.atleast;
		atlt = copy.atlt;
		atmost = copy.atmost;
		atmt = copy.atmt;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Stat2)) return false;

		Stat2 stat2 = (Stat2) o;

		if(wert != stat2.wert) return false;
		if(!name.equals(stat2.name)) return false;
		if(atleast != null ? !atleast.equals(stat2.atleast) : stat2.atleast != null) return false;
		return atmost != null ? atmost.equals(stat2.atmost) : stat2.atmost == null;
	}

	@Override
	public int hashCode()
	{
		int result = name.hashCode();
		result = 31 * result + wert;
		result = 31 * result + (atleast != null ? atleast.hashCode() : 0);
		result = 31 * result + (atmost != null ? atmost.hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(@Nonnull Stat2 stat2)
	{
		return name.compareTo(stat2.name);
	}
}