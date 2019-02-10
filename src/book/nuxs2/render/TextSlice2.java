package book.nuxs2.render;

import java.awt.*;
import m.nuxc2.state.textblock.*;

public class TextSlice2 extends Slice2
{
	private int defFontH;
	public TextBlock2 tb;

	public TextSlice2(Page2 page, Image pageBG, int h, int offset, int defFontH, TextBlock2 tb)
	{
		super(page, pageBG, h, offset);
		this.defFontH = defFontH;
		this.tb = tb;
		renders = tb.color.dca.length;
		link = tb.link;
	}

	@Override
	public void img(boolean lr, int num, Graphics2D gd)
	{
		super.img(lr, num, gd);
		if(tb.text != null)
		{
			gd.setFont(new Font(null, tb.style.code, h * defFontH / page.tsh));
			if(tb.color.dca[num].bc != null)
			{
				gd.setColor(tb.color.dca[num].bc);
				int[] xp = new int[]{0, lr ? page.kO : page.kI, page.w - (lr ? page.kI : page.kO),
						page.w, page.w - (lr ? page.kI : page.kO), lr ? page.kO : page.kI};
				int[] yp = new int[]{h / 2, 0, 0, h / 2, h, h};
				gd.fillPolygon(xp, yp, 6);
			}
			gd.setColor(tb.color.dca[num].tc);
			if(tb.firstTabbed)
				gd.drawString(">", lr ? page.kO : page.kI, h * 3 / 4);
			gd.drawString(tb.text, (lr ? page.kO : page.kI) + (tb.tabbed ? page.cls : 0), h * 3 / 4);
		}
	}
}