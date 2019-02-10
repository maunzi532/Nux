package book.nuxs2.prerender;

import java.util.*;
import m.nuxc2.*;
import m.nuxc2.state.*;

public class ThreadSummoner
{
	private EntrySplitter splitter;
	public State2 state;
	public List<Path2> paths;
	public List<Option2> options;

	private Decider decider;
	private HashMap<Path2, Summoned> summoned = new HashMap<>();
	public Summoned pathed;

	public ThreadSummoner(EntrySplitter splitter)
	{
		this.splitter = splitter;
		state = new State2();
		options = Collections.singletonList(new Option2(splitter.start));
		paths = Collections.singletonList(new Path2(options.get(0)));
		pathed = new Summoned(splitter.errors, paths.get(0));
	}

	public boolean pathOk(Path2 path)
	{
		return paths.contains(path);
	}

	public void decide(Path2 path)
	{
		path = paths.get(paths.indexOf(path));
		decider.taken = path;
		pathed = summoned.get(path);
		while(!pathed.finished)
			try
			{
				Thread.sleep(1);
			}catch(InterruptedException e){}
	}

	public void summonThreads()
	{
		summoned.clear();
		if(pathed.exec != null)
		{
			state = pathed.exec.end;
			paths = pathed.exec.paths;
			options = pathed.exec.options;
		}
		pathed = null;
		decider = new Decider();
		for(Path2 path2 : paths)
		{
			Summoned s2 = new Summoned(decider, splitter, state, path2);
			summoned.put(path2, s2);
			new Thread(s2).start();
		}
	}
}