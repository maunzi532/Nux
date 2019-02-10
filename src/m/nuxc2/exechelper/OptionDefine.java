package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class OptionDefine extends ExecHelper
{
	public Option2 option;
	private TextColor2 color = TextColor2.OPTION;
	private TextStyle2 style = TextStyle2.NORMAL;
	private boolean firstTabbed = true;

	public OptionDefine(Exec2 main, Tag2 tag)
	{
		super(main);
		option = new Option2(tag.value);
		main.options.add(option);
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case GIVEITEM:
				option.give.add(tag.value);
				return true;
			case COLOR:
				try
				{
					color = TextColor2.valueOf(tag.value.toUpperCase());
				}catch(Exception e)
				{
					main.textError("Unbekannte Farbe", tag);
				}
				return true;
			case STYLE:
				try
				{
					style = TextStyle2.valueOf(tag.value.toUpperCase());
				}catch(Exception e)
				{
					main.textError("Unbekannter Stil", tag);
				}
				return true;
			case T:
				main.textOption(tag.value, color, style, option, firstTabbed);
				firstTabbed = false;
				return true;
			case ENDCREATE:
				finish();
				return true;
			default:
				finish();
				return false;
		}
	}

	@Override
	public void finish()
	{
		if(firstTabbed)
			main.textOption("Weiter", color, style, option, true);
		if(option.give.size() > 1)
		{
			main.textInfo("Items benötigt:", TextColor2.ITEM);
			for(String g1 : option.give)
				main.textInfo("Typ " + g1, TextColor2.ITEM);
		}
		else if(!option.give.isEmpty())
			main.textInfo("Item vom Typ " + option.give.get(0) + " benötigt", TextColor2.ITEM);
		main.optionDefine = null;
	}
}