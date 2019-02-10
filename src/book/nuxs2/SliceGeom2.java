package book.nuxs2;

import book.bau2.*;
import book.nuxs2.render.*;
import com.jme3.animation.*;
import com.jme3.math.*;
import com.jme3.texture.*;

public class SliceGeom2 extends AdvGeometry
{
	public Slice2 slice;
	public Skeleton sk1;
	public boolean lr;
	public int skSize;
	public float pagew;
	public float pageh;

	public SliceGeom2(Slice2 slice, Skeleton sk1, boolean lr, int skSize, float pagew, float pageh)
	{
		this.slice = slice;
		this.sk1 = sk1;
		this.lr = lr;
		this.skSize = skSize;
		this.pagew = pagew;
		this.pageh = pageh;
		start(sk1);
		geom.setMaterial(Main3.mat1.clone());
		geom.setUserData("Link", slice.link);
		updateTexture(0);
	}

	public void updateTexture(int num)
	{
		geom.getMaterial().setTexture("DiffuseMap", new Texture2D(slice.img2.get(num)));
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
			float texX = (float) i / (skSize - 1);
			if(lr)
			{
				x1 = -x1;
				texX = 1 - texX;
			}
			DualP2 p1 = new DualP2(new Vector3f(x1, 0, y1), Vector3f.UNIT_Y, new Vector2f(texX, 0), new AutoNumBone(i));
			p1.boneBind = 0;
			DualP2 p2 = new DualP2(new Vector3f(x1, 0, y2), Vector3f.UNIT_Y, new Vector2f(texX, 1), new AutoNumBone(i));
			p2.boneBind = 0;
			if(last1 != null)
				bau.indexQ(lr, p1, last1, p2, last2);
			last1 = p1;
			last2 = p2;
		}
	}
}