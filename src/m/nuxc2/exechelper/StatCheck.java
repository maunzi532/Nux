package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class StatCheck extends ExecHelper
{
	private Stat2 stat;

	public StatCheck(Exec2 main, Tag2 tag)
	{
		super(main);
		stat = main.end.stats.get(tag.value);
		if(stat == null)
		{
			Stat2 stat = new Stat2(tag.value);
			main.end.stats.put(stat.name, stat);
			main.textInfo("Stat " + stat.name + " erstellt", TextColor2.STAT);
		}
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case W:
			{
				boolean result = stat.wert == tag.intvalue;
				main.textInfo("Stat " + stat.name + " == " + tag.intvalue + "? " + (result ? "Ja" : "Nein"),
						TextColor2.STAT);
				main.lastifadd(result ? 1 : -1);
				finish();
				return true;
			}
			case ATLEAST:
			{
				boolean result = stat.wert >= tag.intvalue;
				main.textInfo("Stat " + stat.name + " >= " + tag.intvalue + "? " + (result ? "Ja" : "Nein"),
						TextColor2.STAT);
				main.lastifadd(result ? 1 : -1);
				finish();
				return true;
			}
			case ATMOST:
			{
				boolean result = stat.wert <= tag.intvalue;
				main.textInfo("Stat " + stat.name + " <= " + tag.intvalue + "? " + (result ? "Ja" : "Nein"),
						TextColor2.STAT);
				main.lastifadd(result ? 1 : -1);
				finish();
				return true;
			}
			default:
				main.textError("Nach IFSTAT muss W, ATLEAST oder ATMOST kommen", tag);
				main.lastifadd(1);
				return false;
		}
	}

	@Override
	public void finish()
	{
		main.statCheck = null;
	}
}