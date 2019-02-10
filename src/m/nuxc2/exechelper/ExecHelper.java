package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;

public abstract class ExecHelper
{
	protected Exec2 main;

	public ExecHelper(Exec2 main)
	{
		this.main = main;
	}

	public abstract boolean process(Tag2 tag);

	public void finish(){}
}