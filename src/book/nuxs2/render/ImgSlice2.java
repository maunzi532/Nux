package book.nuxs2.render;

import java.awt.*;

public class ImgSlice2 extends Slice2
{
	private Image image;

	public ImgSlice2(Page2 page, Image pageBG, int h, int offset, Image image)
	{
		super(page, pageBG, h, offset);
		this.image = image;
	}

	@Override
	public void img(boolean lr, int num, Graphics2D gd)
	{
		super.img(lr, num, gd);
		gd.drawImage(image, lr ? page.kO : page.kI, 0, null);
	}
}