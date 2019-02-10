package book.nuxs2.render;

import com.jme3.texture.plugins.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import m.nuxc2.state.*;

public class Slice2
{
	public Page2 page;
	public java.awt.Image pageBG;
	public List<com.jme3.texture.Image> img2 = new ArrayList<>();
	public int h;
	public int offset;
	public int renders;
	public Markable link;

	public Slice2(Page2 page, java.awt.Image pageBG, int h, int offset)
	{
		this.page = page;
		this.pageBG = pageBG;
		this.h = h;
		this.offset = offset;
		renders = 1;
	}

	public Slice2(Page2 page, java.awt.Image pageBG, int h, int offset, Markable link)
	{
		this(page, pageBG, h, offset);
		this.link = link;
	}

	public void img(boolean lr, int num, Graphics2D gd)
	{
		if(pageBG != null)
			gd.drawImage(pageBG, lr ? page.w : 0, 0, lr ? 0 : page.w, h, 0, pageBG.getHeight(null) * offset / page.h,
					pageBG.getWidth(null), pageBG.getHeight(null) * (offset + h) / page.h, null);
		else
		{
			gd.setColor(Color.WHITE);
			gd.fillRect(0, 0, page.w, h);
		}
	}

	public void castImage(boolean lr, int num)
	{
		BufferedImage img1 = new BufferedImage(page.w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gd = img1.createGraphics();
		img(lr, num, gd);
		img2.add(new AWTLoader().load(img1, false));
	}
}