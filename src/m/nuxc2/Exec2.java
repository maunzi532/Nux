package m.nuxc2;

import java.util.*;
import java.util.stream.*;
import m.nuxc2.exechelper.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class Exec2
{
	public State2 start;
	public Path2 taken;
	public Entry2 entry;
	public boolean visual;

	public Exec2(State2 start, Path2 taken, Entry2 entry, boolean visual)
	{
		this.start = start;
		this.taken = taken;
		this.entry = entry;
		this.visual = visual;
	}

	public State2 end;
	public int finishingState = 0;
	public ArrayList<Option2> options = new ArrayList<>();
	public List<Path2> paths = new ArrayList<>();
	public ArrayList<TextBlock2> textsL;
	public ArrayList<TextBlock2> textsR = new ArrayList<>();
	public String imageL = null;
	public String imageR = null;

	private ArrayList<Integer> ifs = new ArrayList<>();
	public TextColor2 textcolor = TextColor2.STANDARD;
	public TextStyle2 textstyle = TextStyle2.NORMAL;
	public StatCheck statCheck;
	public ItemDefine itemDefine;
	public StatDefine statDefine;
	public OptionDefine optionDefine;

	public Integer lastif()
	{
		if(!ifs.isEmpty())
			return ifs.get(ifs.size() - 1);
		return 1;
	}

	public void lastifset(int w)
	{
		if(!ifs.isEmpty())
			ifs.set(ifs.size() - 1, w);
	}

	public void lastifadd(int w)
	{
		ifs.add(w);
	}

	public void lastifremove()
	{
		if(!ifs.isEmpty())
			ifs.remove(ifs.size() - 1);
	}

	public void execute()
	{
		ExecHelper deactivatedIf = new DeactivatedIf(this);
		ExecHelper activatedIf = new ActivatedIf(this);
		ExecHelper baseExec = new BaseExec(this);
		end = new State2(start);
		end.give(taken);
		if(entry == null)
		{
			textcolor = TextColor2.ERROR;
			textR("Eintrag existiert nicht");
		}
		else
			for(Tag2 tag : entry.tags)
			{
				if(finishingState != 0)
					break;
				if(statCheck != null && statCheck.process(tag))
					continue;
				if(lastif() > 0)
				{
					if(activatedIf.process(tag))
						continue;
					if(itemDefine != null && itemDefine.process(tag))
						continue;
					if(statDefine != null && statDefine.process(tag))
						continue;
					if(finishingState != 0)
						break;
					if(optionDefine != null && optionDefine.process(tag))
						continue;
					if(baseExec.process(tag))
						continue;
					textError("Tag vom Typ " + tag.type.name() + " wurde nicht verarbeitet", tag);
				}
				else
					deactivatedIf.process(tag);
			}
		if(finishingState == 0)
		{
			if(statCheck != null)
				statCheck.finish();
			if(itemDefine != null)
				itemDefine.finish();
			if(statDefine != null)
				statDefine.finish();
			if(optionDefine != null)
				optionDefine.finish();
			paths();
		}
		textsL = end.itemView();
	}

	public void textR(String text)
	{
		if(visual)
			textsR.add(new TextBlock2(text, textcolor, textstyle));
	}

	public void textReset(String text, int endType)
	{
		if(visual)
			textsR.add(new TextBlock2(text, TextColor2.MINUS7, TextStyle2.NORMAL, endType));
	}

	public void textOption(String text, TextColor2 color, TextStyle2 style, Option2 option, boolean firstTabbed)
	{
		if(visual)
			textsR.add(new TextBlock2(text, color, style, option, firstTabbed));
	}

	public void textInfo(String text, TextColor2 type)
	{
		if(visual)
			textsR.add(new TextBlock2(text, type, TextStyle2.NORMAL, optionDefine != null ? optionDefine.option : null, false));
	}

	public void textError(String text, Tag2 tag)
	{
		if(visual)
			textsR.add(new TextBlock2(text + ", Zeile " + tag.line, TextColor2.ERROR, TextStyle2.NORMAL));
	}

	public void paths()
	{
		for(Option2 option : options)
		{
			List<Path2> paths1 = Collections.singletonList(new Path2(option));
			for(int i = 0; i < option.give.size(); i++)
			{
				List<Path2> paths3 = new ArrayList<>();
				for(Path2 path2 : paths1)
					paths3.addAll(diverge(i, option, path2));
				paths1 = paths3;
			}
			option.possible = !paths1.isEmpty();
			paths.addAll(paths1);
		}
		paths = paths.stream().distinct().collect(Collectors.toList());
	}

	public List<Path2> diverge(int d, Option2 option, Path2 path1)
	{
		String category = option.give.get(d);
		return end.items.values().stream().filter(e -> !path1.given.contains(e)).filter(e -> e.category.contains(category))
				.map(e -> new Path2(path1, e)).collect(Collectors.toList());
	}
}