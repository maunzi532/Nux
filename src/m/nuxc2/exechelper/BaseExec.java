package m.nuxc2.exechelper;

import java.util.*;
import java.util.stream.*;
import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class BaseExec extends ExecHelper
{
	public BaseExec(Exec2 main)
	{
		super(main);
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case IMAGE0:
				main.imageL = tag.value;
				return true;
			case IMAGE1:
				main.imageR = tag.value;
				return true;
			case COLOR:
				try
				{
					main.textcolor = TextColor2.valueOf(tag.value.toUpperCase());
				}catch(Exception e)
				{
					main.textError("Unbekannte Farbe", tag);
				}
				return true;
			case STYLE:
				try
				{
					main.textstyle = TextStyle2.valueOf(tag.value.toUpperCase());
				}catch(Exception e)
				{
					main.textError("Unbekannter Stil", tag);
				}
				return true;
			case T:
				main.textR(tag.value);
				return true;
			case OPTION:
				main.optionDefine = new OptionDefine(main, tag);
				return true;
			case ITEMGET:
				main.itemDefine = new ItemDefine(main, tag);
				return true;
			case ITEMREMOVE:
				List<String> toRemove = main.end.items.values().stream().filter(e -> e.category.contains(tag.value))
						.map(e -> e.name).collect(Collectors.toList());
				toRemove.forEach(e -> main.end.items.remove(e));
				if(toRemove.isEmpty())
					main.textInfo("Keine Items vom Typ " + tag.value + " entfernt", TextColor2.ITEM);
				else if(toRemove.size() == 1 && toRemove.get(0).equals(tag.value))
					main.textInfo("Item " + tag.value + " entfernt", TextColor2.ITEM);
				else
					main.textInfo("Alle " + toRemove.size() + " Items vom Typ " + tag.value + " entfernt", TextColor2.ITEM);
				return true;
			case STATSET:
			case STATCHANGE:
				main.statDefine = new StatDefine(main, tag);
				return true;
			case STATREMOVE:
				main.end.stats.remove(tag.value);
				main.textInfo("Stat " + tag.value + " entfernt", TextColor2.STAT);
				return true;
			case CIRCGET:
				main.end.circs.add(tag.value);
				return true;
			case CIRCREMOVE:
				main.end.circs.remove(tag.value);
				return true;
			case GIVECLEAR:
				main.end.given.clear();
				return true;
			case GIVEGET:
				if(!main.end.given.isEmpty())
				{
					for(Item2 item : main.end.given)
					{
						main.end.items.put(item.name, item);
						main.textInfo("Item " + item.name + " zurückerhalten", TextColor2.ITEM);
					}
					main.end.given.clear();
				}
				return true;
			case MINUS7LEBEN:
				main.textReset(tag.value, 1);
				main.finishingState = 1;
				return true;
			case FINISH:
				main.finishingState = 2;
				return true;
			default:
				return false;
		}
	}
}