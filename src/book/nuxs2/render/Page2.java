package book.nuxs2.render;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class Page2
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

	public boolean lr;
	public ArrayList<Slice2> slices = new ArrayList<>();

	public Page2(Image img, boolean lr, Option2 link)
	{
		this.lr = lr;
		slices.add(new Slice2(this, img, h, 0, link));
	}

	public Page2(List<TextBlock2> blocks, Image pageBG, Image img, boolean lr)
	{
		this.lr = lr;
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

		ArrayList<TextBlock2> blocks2 = new ArrayList<>();
		if(blocks != null)
		{
			Graphics2D t1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
			t1.setFont(FONT);
			FontMetrics fm = t1.getFontMetrics();
			for(TextBlock2 tb : blocks)
				blocks2.addAll(cut(tb, fm, w - kI - kO));
		}

		slices.add(new Slice2(this, pageBG, kU, 0));
		if(!blocks2.isEmpty() && spaceh > 0)
		{
			if(blocks2.size() * tsh > spaceh)
			{
				for(int i = 0; i < blocks2.size(); i++)
					slices.add(new TextSlice2(this, pageBG, (spaceh * (i + 1)) / blocks2.size() - (spaceh * i) / blocks2.size(),
							kU + (spaceh * i) / blocks2.size(), FONT.getSize(), blocks2.get(i)));
			}
			else
			{
				for(int i = 0; i < blocks2.size(); i++)
					slices.add(new TextSlice2(this, pageBG, tsh, kU + tsh * i,
							FONT.getSize(), blocks2.get(i)));
				if(blocks2.size() * tsh < spaceh)
					slices.add(new Slice2(this, pageBG, spaceh - blocks2.size() * tsh, kU + blocks2.size() * tsh));
			}
		}
		else if(blocks2.isEmpty())
			slices.add(new Slice2(this, pageBG, spaceh, kU));
		if(img != null)
			slices.add(new ImgSlice2(this, pageBG, imgh, h - kL - imgh, img));
		slices.add(new Slice2(this, pageBG, kL, h - kL));
	}

	public List<TextBlock2> cut(TextBlock2 tb, FontMetrics fm, int maxW)
	{
		String[] cut1 = tb.text.split("\n");
		List<String> cut2 = new ArrayList<>();
		for(int i = 0; i < cut1.length; i++)
			cut2.addAll(lineCut(cut1[i], fm, maxW));
		List<TextBlock2> re = cut2.stream().map(e -> new TextBlock2(e, tb)).collect(Collectors.toList());
		re.get(0).firstTabbed = tb.firstTabbed;
		return re;
	}

	public Collection<String> lineCut(String line, FontMetrics fm, int maxW)
	{
		if(fm.stringWidth(line) <= maxW)
			return Collections.singletonList(line);
		String line2 = null;
		while(fm.stringWidth(line) > maxW)
		{
			int l = line.lastIndexOf(" ");
			if(l < 0)
				return Collections.singletonList(line);
			if(line2 == null)
				line2 = line.substring(l + 1);
			else
				line2 = line.substring(l + 1) + " " + line2;
			line = line.substring(0, l);
		}
		Collection<String> r = new ArrayList<>();
		r.add(line);
		r.addAll(lineCut(line2, fm, maxW));
		return r;
	}
}