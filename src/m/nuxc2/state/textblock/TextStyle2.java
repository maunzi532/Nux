package m.nuxc2.state.textblock;

import java.awt.*;

public enum TextStyle2
{
	NORMAL(Font.PLAIN),
	DIALOG(Font.ITALIC);

	public int code;

	TextStyle2(int code)
	{
		this.code = code;
	}
}