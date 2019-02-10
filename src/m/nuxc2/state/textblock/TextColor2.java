package m.nuxc2.state.textblock;

import java.awt.*;

public enum TextColor2
{
	STANDARD(new DualColor[]{new DualColor(Color.BLACK)}),
	OPTION(new DualColor[]{new DualColor(Color.BLUE), new DualColor(Color.BLUE),
			new DualColor(Color.GRAY), new DualColor(Color.GRAY),
			new DualColor(Color.MAGENTA), new DualColor(Color.MAGENTA)}),
	ITEM(new DualColor[]{new DualColor(Color.GREEN), new DualColor(Color.GREEN),
			new DualColor(Color.GRAY), new DualColor(Color.GRAY),
			new DualColor(Color.GREEN), new DualColor(Color.GREEN)}),
	STAT(new DualColor[]{new DualColor(Color.GREEN)}),
	INFO(new DualColor[]{new DualColor(Color.GREEN)}),
	ERROR(new DualColor[]{new DualColor(Color.RED, Color.GRAY)}),
	MINUS7(new DualColor[]{new DualColor(Color.RED, Color.BLACK)}),
	VIEW(new DualColor[]{new DualColor(Color.BLACK, Color.BLUE)}),
	VIEWITEM(new DualColor[]{new DualColor(Color.BLACK), new DualColor(Color.BLACK),
			new DualColor(Color.GRAY), new DualColor(Color.GRAY),
			new DualColor(Color.GREEN), new DualColor(Color.GREEN)}),
	VIEWSTAT(new DualColor[]{new DualColor(Color.BLACK), new DualColor(Color.BLACK)});

	public DualColor[] dca;

	TextColor2(DualColor[] dca)
	{
		this.dca = dca;
	}
}