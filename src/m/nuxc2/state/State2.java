package m.nuxc2.state;

import java.util.*;
import java.util.stream.*;
import m.nuxc2.*;
import m.nuxc2.state.textblock.*;

public class State2
{
	public HashSet<String> circs;
	public TreeMap<String, Stat2> stats;
	public TreeMap<String, Item2> items;
	public List<Item2> given;

	public State2()
	{
		circs = new HashSet<>();
		stats = new TreeMap<>();
		items = new TreeMap<>();
		given = new ArrayList<>();
	}

	public State2(State2 copy)
	{
		circs = (HashSet<String>) copy.circs.clone();
		stats = new TreeMap<>();
		copy.stats.values().forEach(e -> stats.put(e.name, new Stat2(e)));
		items = new TreeMap<>();
		copy.items.values().forEach(e -> items.put(e.name, new Item2(e)));
		given = copy.given.stream().map(Item2::new).collect(Collectors.toList());
	}

	public void give(Path2 path2)
	{
		for(Item2 item : path2.given)
		{
			items.remove(item.name);
			given.add(item);
		}
	}

	public boolean visualEquals(State2 state2)
	{
		return items.equals(state2.items) && stats.equals(state2.stats);
	}

	public ArrayList<TextBlock2> itemView()
	{
		ArrayList<TextBlock2> blocks = new ArrayList<>();
		if(!items.isEmpty())
			blocks.add(new TextBlock2("Items", TextColor2.VIEW, TextStyle2.NORMAL));
		for(Item2 item : items.values())
			blocks.add(new TextBlock2(item.name, item));
		if(!stats.isEmpty())
			blocks.add(new TextBlock2("Stats", TextColor2.VIEW, TextStyle2.NORMAL));
		for(Stat2 stat : stats.values())
			blocks.add(new TextBlock2(stat.name + " = " + stat.wert +
					(stat.atleast != null ? ", unteres Limit: " + stat.atleast : "") +
					(stat.atmost != null ? ", oberes Limit: " + stat.atmost : ""), stat));
		return blocks;
	}
}