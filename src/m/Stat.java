package m;

public class Stat
{
	public String name;
	public int wert;
	public boolean dieonzero;

	public Stat(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name + " = " + wert + (dieonzero ? ", muss über 0 bleiben" : "");
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Stat)) return false;

		Stat stat = (Stat) o;

		return name.equals(stat.name);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}