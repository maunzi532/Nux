package m.nuxc2;

import java.util.*;
import m.nuxc2.state.*;

public class Path2
{
	public Option2 option;
	public List<Item2> given = new ArrayList<>();

	public Path2(Option2 option)
	{
		this.option = option;
	}

	public Path2(Path2 prev, Item2 give)
	{
		option = prev.option;
		given = new ArrayList<>(prev.given);
		given.add(give);
		Collections.sort(given);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Path2)) return false;

		Path2 path2 = (Path2) o;

		if(!option.equals(path2.option)) return false;
		return given.equals(path2.given);
	}

	@Override
	public int hashCode()
	{
		int result = option.hashCode();
		result = 31 * result + given.hashCode();
		return result;
	}
}