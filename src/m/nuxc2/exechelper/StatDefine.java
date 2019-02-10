package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class StatDefine extends ExecHelper
{
	private Stat2 stat;
	private boolean created;
	private boolean statadd;
	private int statprev;
	private boolean atlc;
	private boolean atmc;
	private boolean atlt;
	private boolean atmt;

	public StatDefine(Exec2 main, Tag2 tag)
	{
		super(main);
		if(tag.type == Tag2Type.STATCHANGE)
			statadd = true;
		stat = main.end.stats.get(tag.value);
		if(stat == null)
		{
			stat = new Stat2(tag.value);
			main.end.stats.put(stat.name, stat);
			created = true;
		}
		statprev = stat.wert;
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case W:
				if(statadd)
					stat.wert += tag.intvalue;
				else
					stat.wert = tag.intvalue;
				return true;
			case ATLEAST:
				stat.atleast = tag.intvalue;
				atlc = true;
				atlt = true;
				return true;
			case ATMOST:
				stat.atmost = tag.intvalue;
				atmc = true;
				atmt = true;
				return true;
			case T:
				if(atlt || atmt)
				{
					if(atlt)
						stat.atlt = tag.value;
					atlt = false;
					if(atmt)
						stat.atmt = tag.value;
					atmt = false;
					return true;
				}
			default:
				finish();
				return false;
		}
	}

	@Override
	public void finish()
	{
		if(created)
			main.textInfo("Stat " + stat.name + " erstellt, Wert " + stat.wert, TextColor2.STAT);
		else if(!statadd)
			main.textInfo("Stat " + stat.name + " auf " + stat.wert + " gesetzt", TextColor2.STAT);
		else if(stat.wert > statprev)
			main.textInfo("Stat " + stat.name + " um " + (stat.wert - statprev) + " erhöht", TextColor2.STAT);
		else if(statprev > stat.wert)
			main.textInfo("Stat " + stat.name + " um " + (statprev - stat.wert) + " gesenkt", TextColor2.STAT);
		if(stat.atleast != null && stat.atmost != null && stat.atleast > stat.atmost)
		{
			stat.atleast = null;
			stat.atmost = null;
			stat.atlt = null;
			stat.atmt = null;
			main.textInfo("Stat " + stat.name + ", Limits entfernt", TextColor2.STAT);
		}
		else
		{
			if(atlc)
				main.textInfo("Stat " + stat.name + ", unteres Limit auf " + stat.atleast + " gesetzt", TextColor2.STAT);
			if(atmc)
				main.textInfo("Stat " + stat.name + ", oberes Limit auf " + stat.atmost + " gesetzt", TextColor2.STAT);
		}
		if(stat.atleast != null && stat.wert < stat.atleast)
		{
			if(stat.atlt != null)
				main.textReset(stat.atlt, 1);
			else
				main.textReset("Stat " + stat.name + " außerhalb von unterem Limit " + stat.atleast, 1);
			main.finishingState = 1;
		}
		if(stat.atmost != null && stat.wert > stat.atmost)
		{
			if(stat.atmt != null)
				main.textReset(stat.atmt, 1);
			else
				main.textReset("Stat " + stat.name + " außerhalb von oberem Limit " + stat.atmost, 1);
			main.finishingState = 1;
		}
		main.statDefine = null;
	}
}