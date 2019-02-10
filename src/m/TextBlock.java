package m;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

public class TextBlock
{
	public static final int T_STANDARD = 0;
	public static final int T_OPTION = 1;
	public static final int T_ITEM = 2;
	public static final int T_STAT = 3;
	public static final int T_INFO = 4;
	public static final int T_ERROR = 5;
	public static final int T_DISABLED = 6;
	public static final int T_OKITEM = 7;

	public static final Color[] colors = new Color[]
			{
					new Color(0, 0, 0),
					new Color(0, 0, 200),
					new Color(90, 120, 0),
					new Color(80, 120, 0),
					new Color(80, 120, 0),
					new Color(255, 120, 120),
					new Color(130, 130, 130),
					new Color(20, 160, 20)
			};

	public String text;
	public int color;
	public Option1 link;
	public boolean tabbed;
	public boolean first;

	public TextBlock(String text, int color)
	{
		this(text, color, false, null);
	}

	public TextBlock(String text, int color, boolean autotabbed, Option1 link)
	{
		this.text = text;
		this.color = color;
		this.link = link;
		tabbed = autotabbed || link != null;
		first = link != null && !autotabbed;
	}

	public TextBlock(String text, TextBlock copy, boolean copyFirst)
	{
		this.text = text;
		color = copy.color;
		link = copy.link;
		tabbed = copy.tabbed;
		first = copyFirst && copy.first;
	}

	public List<TextBlock> cut(FontMetrics fm, int maxW)
	{
		if(text == null)
			return Collections.singletonList(new TextBlock("Weiter", this, true));
		String[] cut1 = text.split("\n");
		List<String> cut2 = new ArrayList<>();
		for(int i = 0; i < cut1.length; i++)
			cut2.addAll(lineCut(cut1[i], fm, maxW));
		List<TextBlock> re = cut2.stream().map(e -> new TextBlock(e, this, false)).collect(Collectors.toList());
		re.get(0).first = first;
		return re;
	}

	public Collection<String> lineCut(String line, FontMetrics fm, int maxW)
	{
		if(fm.stringWidth(line) <= maxW)
			return Collections.singletonList(line);
		String line2 = null;
		while(fm.stringWidth(line) > maxW)
		{
			int l = line.lastIndexOf(" ");
			if(l < 0)
				return Collections.singletonList(line);
			if(line2 == null)
				line2 = line.substring(l + 1);
			else
				line2 = line.substring(l + 1) + " " + line2;
			line = line.substring(0, l);
		}
		Collection<String> r = new ArrayList<>();
		r.add(line);
		r.addAll(lineCut(line2, fm, maxW));
		return r;
	}
}