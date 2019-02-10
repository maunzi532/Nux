package m.nuxc2.state;

import java.util.*;
import javax.annotation.*;

public class Item2 implements Comparable<Item2>, Markable
{
	public String name;
	public HashSet<String> category = new HashSet<>();

	public Item2(String name)
	{
		this.name = name;
		category.add(name);
	}

	public Item2(Item2 copy)
	{
		name = copy.name;
		category = (HashSet<String>) copy.category.clone();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Item2)) return false;

		Item2 item2 = (Item2) o;

		if(!name.equals(item2.name)) return false;
		return category.equals(item2.category);
	}

	@Override
	public int hashCode()
	{
		int result = name.hashCode();
		result = 31 * result + category.hashCode();
		return result;
	}

	@Override
	public int compareTo(@Nonnull Item2 item2)
	{
		return name.compareTo(item2.name);
	}
}