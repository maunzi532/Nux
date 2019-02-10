package book;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import m.*;

public class Page
{
	public final int w = 3000;
	public final int h = 3000;
	public final int kU = 300;
	public final int kL = 300;
	public final int kI = 400;
	public final int kO = 200;
	public final int cls = 200;
	public final int tsh = 100;
	public final Font FONT = new Font(null, Font.PLAIN, 80);

	public ArrayList<Slice> slices = new ArrayList<>();

	public Page(Image img)
	{
		slices.add(new ImgSlice(this, img.getScaledInstance(w, h, 0)));
	}

	public Page(ArrayList<TextBlock> blocks, Image img)
	{
		int imgh = 0;
		if(img != null)
		{
			img = img.getScaledInstance(w - kI - kO, -1, 0);
			imgh = img.getHeight(null);
			if(imgh > h - kU - kL)
			{
				img = img.getScaledInstance(-1, h - kU - kL, 0);
				imgh = img.getHeight(null);
			}
		}
		int spaceh = h - kU - kL - imgh;

		List<TextBlock> blocks2 = new ArrayList<>();
		if(blocks != null)
		{
			Graphics2D t1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
			t1.setFont(FONT);
			FontMetrics fm = t1.getFontMetrics();
			for(TextBlock tb : blocks)
				blocks2.addAll(tb.cut(fm, w - kI - kO));
		}

		slices.add(new Slice(this, kU, 0));
		if(!blocks2.isEmpty() && spaceh > 0)
		{
			if(blocks2.size() * tsh > spaceh)
			{
				for(int i = 0; i < blocks2.size(); i++)
					slices.add(new TextSlice(this, (spaceh * (i + 1)) / blocks2.size() - (spaceh * i) / blocks2.size(),
							kU + (spaceh * i) / blocks2.size(), FONT.getSize(), blocks2.get(i)));
			}
			else
			{
				for(int i = 0; i < blocks2.size(); i++)
					slices.add(new TextSlice(this, tsh, kU + tsh * i,
							FONT.getSize(), blocks2.get(i)));
				if(blocks2.size() * tsh < spaceh)
					slices.add(new Slice(this, spaceh - blocks2.size() * tsh, kU + blocks2.size() * tsh));
			}
		}
		else if(blocks2.isEmpty())
			slices.add(new Slice(this, spaceh, kU));
		if(img != null)
			slices.add(new ImgSlice(this, imgh, h - kL - imgh, img));
		slices.add(new Slice(this, kL, h - kL));
	}
}