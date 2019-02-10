package m.nuxc2.state.textblock;

import m.nuxc2.state.*;

public class TextBlock2
{
	public String text;
	public TextColor2 color;
	public TextStyle2 style;
	public Markable link;
	public boolean tabbed;
	public boolean firstTabbed;

	public TextBlock2(String text, TextColor2 color, TextStyle2 style)
	{
		this.text = text;
		this.color = color;
		this.style = style;
	}

	public TextBlock2(String text, TextColor2 color, TextStyle2 style, Option2 link, boolean firstTabbed)
	{
		this.text = text;
		this.color = color;
		this.style = style;
		this.link = link;
		this.firstTabbed = firstTabbed;
		tabbed = link != null;
	}

	public TextBlock2(String text, TextColor2 color, TextStyle2 style, int endType)
	{
		this.text = text;
		this.color = color;
		this.style = style;
		link = new Reset2(endType);
	}

	public TextBlock2(String text, Item2 linkItem)
	{
		this.text = text;
		color = TextColor2.VIEWITEM;
		style = TextStyle2.NORMAL;
		link = linkItem;
		tabbed = true;
	}

	public TextBlock2(String text, Stat2 linkStat)
	{
		this.text = text;
		color = TextColor2.VIEWSTAT;
		style = TextStyle2.NORMAL;
		link = linkStat;
		tabbed = true;
	}

	public TextBlock2(String text, TextBlock2 tb)
	{
		this.text = text;
		color = tb.color;
		style = tb.style;
		link = tb.link;
		tabbed = tb.tabbed;
	}
}