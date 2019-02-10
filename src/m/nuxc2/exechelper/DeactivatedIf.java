package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;

public class DeactivatedIf extends ExecHelper
{
	public DeactivatedIf(Exec2 main)
	{
		super(main);
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case IFCIRC:
			case IFITEM:
			case IFSTAT:
				main.lastifadd(0);
				return true;
			case ELSE:
				if(main.lastif() < 0)
					main.lastifset(1);
				return true;
			case END:
				main.lastifremove();
				return true;
			default:
				return false;
		}
	}
}