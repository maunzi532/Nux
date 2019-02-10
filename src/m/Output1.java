package m;

import java.util.*;
import java.util.stream.*;

public class Output1
{
	public ArrayList<TextBlock> blocks = new ArrayList<>();
	public int options;
	public boolean minus7leben;
	public boolean finish;

	public void addText(String text, Integer color)
	{
		blocks.add(new TextBlock(text, color));
	}

	public void addOption(Option1 option)
	{
		blocks.add(new TextBlock(option.text, TextBlock.T_OPTION, false, option));
		for(int i = 0; i < option.give.size(); i++)
			blocks.add(new TextBlock("Item geben: " + option.give.get(i), TextBlock.T_ITEM, true, option));
		options++;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(TextBlock tb : blocks)
		{
			if(tb.link != null)
			{
				sb.append("Option ").append(tb.link.ziel);
				if(tb.text != null)
					sb.append(": \n");
			}
			if(tb.text != null)
				sb.append(tb.text).append('\n');
		}
		sb.append('\n').append(blocks.stream().filter(e -> e.link != null).map(e -> e.link.ziel).collect(Collectors.toList()));
		return sb.toString();
	}
}