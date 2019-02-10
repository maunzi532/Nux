package book.nuxs2.prerender;

import book.nuxs2.render.*;
import java.util.*;
import java.util.stream.*;
import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class Summoned implements Runnable
{
	private Decider decider;
	private EntrySplitter splitter;
	private State2 start;
	private Path2 path;

	public Exec2 exec;
	public Page2 pageL;
	public Page2 pageR;
	public boolean finished;

	public Summoned(ArrayList<String> errors, Path2 path)
	{
		if(errors.size() > 0)
		{
			List<TextBlock2> tb2 = errors.stream()
					.map(e -> new TextBlock2(e, TextColor2.ERROR, TextStyle2.NORMAL)).collect(Collectors.toList());
			pageL = new Page2(tb2, Loader.gibBild("PageBG.png"), null, true);
			for(Slice2 slice2 : pageL.slices)
				for(int i = 0; i < slice2.renders; i++)
					slice2.castImage(true, i);
		}
		pageR = new Page2(Loader.gibBild("Cover.png"), false, path.option);
		for(Slice2 slice2 : pageR.slices)
			for(int i = 0; i < slice2.renders; i++)
				slice2.castImage(false, i);
	}

	public Summoned(Decider decider, EntrySplitter splitter, State2 start, Path2 path)
	{
		this.decider = decider;
		this.splitter = splitter;
		this.start = start;
		this.path = path;
	}

	@Override
	public void run()
	{
		exec = new Exec2(start, path, splitter.entries.get(path.option.ziel), true);
		exec.execute();
		if(endNow())
			return;
		pageL = new Page2(exec.textsL, Loader.gibBild("PageBG.png"), exec.imageL != null ? Loader.gibBild(exec.imageL) : null, true);
		if(endNow())
			return;
		pageR = new Page2(exec.textsR, Loader.gibBild("PageBG.png"), exec.imageR != null ? Loader.gibBild(exec.imageR) : null, false);
		if(endNow())
			return;
		for(Slice2 slice2 : pageL.slices)
			for(int i = 0; i < slice2.renders; i++)
			{
				slice2.castImage(true, i);
				if(endNow())
					return;
			}
		for(Slice2 slice2 : pageR.slices)
			for(int i = 0; i < slice2.renders; i++)
			{
				slice2.castImage(false, i);
				if(endNow())
					return;
			}
		finished = true;
	}

	private boolean endNow()
	{
		return decider.taken != null && decider.taken != path;
	}
}