package book;

import java.awt.*;
import java.awt.image.*;

public class Slice
{
	public Page page;
	public BufferedImage img;
	public Graphics2D gd;
	public int h;
	public int offset;

	public Slice(Page page, int h, int offset)
	{
		this.page = page;
		this.h = h;
		this.offset = offset;
	}

	public BufferedImage img(boolean lr)
	{
		img = new BufferedImage(page.w, h, BufferedImage.TYPE_INT_ARGB);
		gd = img.createGraphics();
		/*gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, page.w, h);*/
		gd.drawImage(Main2.pageBG, lr ? page.w : 0, 0, lr ? 0 : page.w, h, 0, Main2.pageBG.getHeight() * offset / page.h,
				Main2.pageBG.getWidth(), Main2.pageBG.getHeight() * (offset + h) / page.h, null);
		return img;
	}
}