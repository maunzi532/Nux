package book;

import java.awt.*;
import java.awt.image.*;

public class ImgSlice extends Slice
{
	Image image;
	boolean all;

	public ImgSlice(Page page, Image image)
	{
		super(page, page.h, 0);
		this.image = image;
		all = true;
	}

	public ImgSlice(Page page, int h, int offset, Image image)
	{
		super(page, h, offset);
		this.image = image;
	}

	public BufferedImage img(boolean lr)
	{
		super.img(lr);
		gd.drawImage(image, all ? 0 : lr ? page.kO : page.kI, 0, null);
		return img;
	}
}