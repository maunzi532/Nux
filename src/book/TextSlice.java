package book;

import java.awt.*;
import java.awt.image.*;
import m.*;

public class TextSlice extends Slice
{
	int defFontH;
	TextBlock tb;

	public TextSlice(Page page, int h, int offset, int defFontH, TextBlock tb)
	{
		super(page, h, offset);
		this.defFontH = defFontH;
		this.tb = tb;
	}

	public BufferedImage img(boolean lr)
	{
		super.img(lr);
		gd.setFont(new Font(null, Font.PLAIN, h * defFontH / page.tsh));
		gd.setColor(TextBlock.colors[tb.color]);
		if(tb.first)
			gd.drawString(">", lr ? page.kO : page.kI, h * 3 / 4);
		gd.drawString(tb.text, (lr ? page.kO : page.kI) + (tb.tabbed ? page.cls : 0), h * 3 / 4);
		return img;
	}
}