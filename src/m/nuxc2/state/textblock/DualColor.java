package m.nuxc2.state.textblock;

import java.awt.*;

public class DualColor
{
	public Color tc;
	public Color bc;

	public DualColor(Color tc)
	{
		this.tc = tc;
	}

	public DualColor(Color tc, Color bc)
	{
		this.tc = tc;
		this.bc = bc;
	}
}