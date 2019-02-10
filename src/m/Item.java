package m;

import java.util.*;

public class Item
{
	public String name;
	public ArrayList<String> category = new ArrayList<>();

	public Item(String name)
	{
		this.name = name;
		category.add(name);
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof Item)) return false;

		Item item = (Item) o;

		return name.equals(item.name);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}