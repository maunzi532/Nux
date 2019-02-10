package book;

import book.bau2.*;
import com.jme3.animation.*;
import com.jme3.math.*;
import com.jme3.texture.*;
import com.jme3.texture.plugins.*;

public class SliceGeom extends AdvGeometry
{
	public Slice slice;
	public Skeleton sk1;
	public boolean lr;
	public int skSize;
	public int skPage;
	public float pagew;
	public float pageh;

	public SliceGeom(Slice slice, Skeleton sk1, boolean lr, int skSize, int skPage, float pagew, float pageh)
	{
		this.slice = slice;
		this.sk1 = sk1;
		this.lr = lr;
		this.skSize = skSize;
		this.skPage = skPage;
		this.pagew = pagew;
		this.pageh = pageh;
		start(sk1);
		geom.setMaterial(Main2.mat1.clone());
		geom.getMaterial().setTexture("DiffuseMap", new Texture2D(new AWTLoader().load(slice.img(lr), false)));
		if(slice instanceof TextSlice && ((TextSlice) slice).tb.link != null)
			geom.setUserData("Link", ((TextSlice) slice).tb.link.ziel);
		geom.setUserData("Page", skPage);
	}

	@Override
	protected void setup()
	{
		DualP2 last1 = null;
		DualP2 last2 = null;
		for(int i = 0; i < skSize; i++)
		{
			float x1 = pagew * i / (skSize - 1);
			float y1 = pageh * slice.offset / slice.page.h;
			float y2 = pageh * (slice.offset + slice.h) / slice.page.h;
			DualP2 p1;
			DualP2 p2;
			if(lr)
			{
				p1 = new DualP2(new Vector3f(-x1, 0, y1), Vector3f.UNIT_Y, new Vector2f(1 - (float) i / (skSize - 1), 0),
						new AutoNumBone(i));
				p1.boneBind = 0;
				p2 = new DualP2(new Vector3f(-x1, 0, y2), Vector3f.UNIT_Y, new Vector2f(1 - (float) i / (skSize - 1), 1),
						new AutoNumBone(i));
				p2.boneBind = 0;
			}
			else
			{
				p1 = new DualP2(new Vector3f(x1, 0, y1), Vector3f.UNIT_Y, new Vector2f((float) i / (skSize - 1), 0),
						new AutoNumBone(i));
				p1.boneBind = 0;
				p2 = new DualP2(new Vector3f(x1, 0, y2), Vector3f.UNIT_Y, new Vector2f((float) i / (skSize - 1), 1),
						new AutoNumBone(i));
				p2.boneBind = 0;
			}
			if(last1 != null)
				bau.indexQ(lr, p1, last1, p2, last2);
			last1 = p1;
			last2 = p2;
		}
	}
}