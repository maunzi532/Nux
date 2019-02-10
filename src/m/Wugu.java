package m;

import java.util.*;
import java.util.stream.*;

public class Wugu
{
	public HashSet<String> circs = new HashSet<>();
	public ArrayList<Item> items = new ArrayList<>();
	public ArrayList<Stat> stats = new ArrayList<>();

	public ArrayList<Item> give = new ArrayList<>();
	public ArrayList<String> require = null;
	public ArrayList<Item> givenCurrent = new ArrayList<>();
	public ArrayList<Item> itemsLeft = null;

	public Output1 output;
	private Boolean ifokay;
	private Item itemdef;
	private Stat statdef;
	private Option1 optiondef;
	private Integer ifstat;

	public void run(Entry1 entry)
	{
		output = new Output1();
		if(entry == null)
			return;
		ifokay = null;
		itemdef = null;
		statdef = null;
		optiondef = null;
		ifstat = null;
		for(Tag1 tag : entry.tags)
		{
			switch(tag.type)
			{
				case IFITEM:
					enddef();
					ifokay = items.stream().anyMatch(e -> e.category.contains(tag.value));
					output.addText("Item vom Typ " + tag.value + (ifokay ? " verfügbar" : " nicht verfügbar"), TextBlock.T_INFO);
					continue;
				case IFCIRC:
					enddef();
					ifokay = circs.contains(tag.value);
					output.addText("Situation " + tag.value + (ifokay ? "? Ja" : "? Nein"), TextBlock.T_INFO);
					continue;
				case IFSTAT:
					enddef();
					if(stats.contains(new Stat(tag.value)))
						ifstat = stats.get(stats.indexOf(new Stat(tag.value))).wert;
					else
						ifstat = 0;
					output.addText("Wert " + tag.value + ": " + ifstat, TextBlock.T_INFO);
					continue;
				case W:
					if(ifstat != null)
					{
						ifokay = ifstat >= tag.intvalue;
						output.addText("Wert >= " + tag.intvalue + (ifokay ? "? Ja" : "? Nein"), TextBlock.T_INFO);
						continue;
					}
					break;
				case ELSE:
					if(ifokay != null)
						ifokay = !ifokay;
					continue;
				case END:
					ifokay = null;
					continue;
			}
			if(ifokay == null || ifokay)
			{
				if(itemdef != null)
					switch(tag.type)
					{
						case CATEGORY:
							itemdef.category.add(tag.value);
							continue;
						default:
							enddef();
					}
				if(statdef != null)
					switch(tag.type)
					{
						case W:
							statdef.wert += tag.intvalue;
							if(tag.intvalue > 0)
								output.addText("Stat " + statdef.name + ": Wert um " + tag.intvalue + " erhöht", TextBlock.T_STAT);
							if(tag.intvalue < 0)
								output.addText("Stat " + statdef.name + ": Wert um " + (-tag.intvalue) + " gesenkt", TextBlock.T_STAT);
							if(statdef.dieonzero && statdef.wert <= 0)
							{
								output.minus7leben = true;
								return;
							}
							continue;
						case DIEONZERO:
							statdef.dieonzero = true;
							output.addText("Wert " + statdef.name + " muss über 0 bleiben", TextBlock.T_STAT);
							if(statdef.wert <= 0)
							{
								output.minus7leben = true;
								return;
							}
							continue;
						default:
							enddef();
					}
				if(optiondef != null)
					switch(tag.type)
					{
						case T:
							optiondef.addText(tag.value);
							continue;
						case GIVEITEM:
							optiondef.give.add(tag.value);
							continue;
						default:
							enddef();
					}
				switch(tag.type)
				{
					case T:
						output.addText(tag.value, TextBlock.T_STANDARD);
						break;
					case OPTION:
						optiondef = new Option1(tag.value);
						break;
					case MINUS7LEBEN:
						output.minus7leben = true;
						return;
					case FINISH:
						output.finish = true;
						return;
					case CIRCGET:
						circs.add(tag.value);
						break;
					case CIRCREMOVE:
						circs.remove(tag.value);
						break;
					case ITEMGET:
						itemdef = new Item(tag.value);
						break;
					case ITEMREMOVE:
						List<Item> toRemove = items.stream().filter(e -> e.category.contains(tag.value)).collect(Collectors.toList());
						items.removeAll(toRemove);
						if(toRemove.isEmpty())
							output.addText("Keine Items vom Typ " + tag.value + " im Inventar", TextBlock.T_ITEM);
						else if(toRemove.size() == 1 && toRemove.get(0).name.equals(tag.value))
							output.addText("Item " + tag.value + " entfernt", TextBlock.T_ITEM);
						else
							output.addText("Alle " + toRemove.size() + " Items vom Typ " + tag.value + " entfernt", TextBlock.T_ITEM);
						break;
					case STATCREATE:
						statdef = new Stat(tag.value);
						output.addText("Stat " + statdef.name + " erstellt", TextBlock.T_STAT);
						break;
					case STATCHANGE:
						int index = stats.indexOf(new Stat(tag.value));
						if(index >= 0)
							statdef = stats.get(index);
						else
						{
							output.addText("Stat " + tag.value + " nicht existent", TextBlock.T_ERROR);
							statdef = null;
						}
						break;
					case STATREMOVE:
						stats.remove(new Stat(tag.value));
						output.addText("Stat " + tag.value + " entfernt", TextBlock.T_STAT);
						break;
					case GIVEGET:
						if(!give.isEmpty())
						{
							items.addAll(give);
							for(Item i1 : give)
								output.addText("Item " + i1 + " zurückerhalten", TextBlock.T_ITEM);
							give.clear();
						}
						break;
					case GIVECLEAR:
						give.clear();
						break;
					case ENDCREATE:
						break;
					default:
						output.addText("Unbekannter Tag: <" + tag.type + "> Wert: " + tag.value, TextBlock.T_ERROR);
				}
			}
		}
		enddef();
	}

	private void enddef()
	{
		if(itemdef != null)
		{
			output.addText("Item erhalten: " + itemdef, TextBlock.T_ITEM);
			items.add(itemdef);
			itemdef = null;
		}
		if(statdef != null)
		{
			if(!stats.contains(statdef))
				stats.add(statdef);
			statdef = null;
		}
		if(optiondef != null)
		{
			output.addOption(optiondef);
			optiondef = null;
		}
	}

	public boolean tryOption(ArrayList<String> require1)
	{
		if(require1.isEmpty())
		{
			require = null;
			itemsLeft = null;
			givenCurrent.clear();
			return true;
		}
		require = require1;
		itemsLeft = new ArrayList<>(items);
		givenCurrent.clear();
		return false;
	}

	public boolean tryGive(String item)
	{
		if(item == null)
		{
			require = null;
			itemsLeft = null;
			givenCurrent.clear();
			return false;
		}
		int index = itemsLeft.indexOf(new Item(item));
		if(index >= 0)
		{
			givenCurrent.add(itemsLeft.remove(index));
			require.remove(0);
			if(require.isEmpty())
			{
				give = givenCurrent;
				items = itemsLeft;
				require = null;
				itemsLeft = null;
				givenCurrent = new ArrayList<>();
				return true;
			}
			return false;
		}
		throw new RuntimeException("Item " + item + " muss existieren");
	}

	public ArrayList<TextBlock> itemView()
	{
		ArrayList<TextBlock> textBlocks = new ArrayList<>();
		boolean red = require != null && items.stream().noneMatch(e -> e.category.contains(require.get(0)));
		if(!items.isEmpty())
		{
			int clr = red ? TextBlock.T_ERROR : require != null ? TextBlock.T_OPTION : TextBlock.T_STANDARD;
			textBlocks.add(new TextBlock("Items", clr));
		}
		for(Item item : items)
		{
			int clr = givenCurrent.contains(item) ? TextBlock.T_DISABLED : red ? TextBlock.T_ERROR : TextBlock.T_STANDARD;
			if(clr != TextBlock.T_DISABLED && require != null && item.category.contains(require.get(0)))
				clr = TextBlock.T_OKITEM;
			textBlocks.add(new TextBlock(item.name, clr, true, clr == TextBlock.T_OKITEM ? new Option1(item.name) : null));
		}
		if(!stats.isEmpty())
			textBlocks.add(new TextBlock((items.isEmpty() ? "" : "\n") + "Stats", 0));
		for(Stat stat : stats)
			textBlocks.add(new TextBlock(stat.toString(), stat.wert <= 0 ? TextBlock.T_ERROR : TextBlock.T_STANDARD, true, null));
		return textBlocks;
	}
}