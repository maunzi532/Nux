package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class ActivatedIf extends ExecHelper
{
	public ActivatedIf(Exec2 main)
	{
		super(main);
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case IFCIRC:
			{
				boolean result = main.end.circs.contains(tag.value);
				main.textInfo("Situation " + tag.value + "? " + (result ? "Ja" : "Nein"), TextColor2.INFO);
				main.lastifadd(result ? 1 : -1);
				return true;
			}
			case IFITEM:
			{
				boolean result = main.end.items.values().stream().anyMatch(e -> e.category.contains(tag.value));
				main.textInfo("Item vom Typ " + tag.value + " vorhanden? " + (result ? "Ja" : "Nein"), TextColor2.ITEM);
				main.lastifadd(result ? 1 : -1);
				return true;
			}
			case IFSTAT:
				main.statCheck = new StatCheck(main, tag);
				return true;
			case ELSE:
				main.lastifset(-1);
				return true;
			case END:
				main.lastifremove();
				return true;
			default:
				return false;
		}
	}
}