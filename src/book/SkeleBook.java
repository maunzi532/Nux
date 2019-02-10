package book;

import com.jme3.animation.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;

public class SkeleBook extends AbstractControl
{
	public int pagekeep;
	public int skSize;
	public float pagew;
	public float pageh;
	public Node[] pages;
	public Page[] pages2;
	public Skeleton[] ska;
	public float[] progress;

	public SkeleBook(int pagekeep, int skSize, float pagew, float pageh)
	{
		this.pagekeep = pagekeep;
		this.skSize = skSize;
		this.pagew = pagew;
		this.pageh = pageh;
		pages = new Node[pagekeep * 2];
		pages2 = new Page[pagekeep * 2];
		ska = new Skeleton[pagekeep * 2];
		progress = new float[pagekeep];
		Bone[][] ska1 = new Bone[pagekeep * 2][skSize];
		for(int i = 0; i < pagekeep * 2; i++)
		{
			boolean r = i % 2 > 0;
			Bone lastx = new Bone("W");
			lastx.setBindTransforms(Vector3f.ZERO, new Quaternion().fromAngleAxis(FastMath.HALF_PI * (r ? 1 : -1), Vector3f.UNIT_Z), Vector3f.UNIT_XYZ);
			ska1[i][0] = lastx;
			for(int j = 1; j < skSize; j++)
			{
				Bone newx = new Bone("W");
				newx.setBindTransforms(new Vector3f(0, pagew / (skSize - 1), 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
				lastx.addChild(newx);
				ska1[i][j] = newx;
				lastx = newx;
			}
		}
		for(int i = 0; i < ska1.length; i++)
		{
			for(Bone b : ska1[i])
				b.setUserControl(true);
			ska[i] = new Skeleton(ska1[i]);
		}
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		setEnabled(true);
	}

	public void movePages()
	{
		for(int i = pagekeep - 1; i > 0; i--)
		{
			setPage(pages2[i * 2 - 2], i, true);
			setPage(pages2[i * 2 - 1], i, false);
			progress[i] = progress[i - 1];
		}
	}

	public void setPage(Page page, int dPage, boolean lr)
	{
		int n1 = dPage * 2 + (lr ? 0 : 1);
		if(pages[n1] != null)
		{
			((Node) spatial).detachChild(pages[n1]);
			pages[n1] = null;
			pages2[n1] = null;
		}
		if(page != null)
		{
			Node newPage = new Node();
			pages[n1] = newPage;
			pages2[n1] = page;
			((Node) spatial).attachChild(newPage);
			for(Slice sl : page.slices)
			{
				SliceGeom slg = new SliceGeom(sl, ska[n1], lr, skSize, n1, pagew, pageh);
				newPage.attachChild(slg.geom);
			}
		}
	}

	/*public SkeletonControl controlSetup()
	{
		SkeletonControl skc = new SkeletonControl(sk);
		spatial.getParent().addControl(skc);
		return skc;
	}

	public SkeletonDebugger debugger(AssetManager am)
	{
		SkeletonDebugger skd = new SkeletonDebugger("skd", sk);
		Material skdmat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		skdmat.getAdditionalRenderState().setWireframe(true);
		skdmat.setColor("Color", ColorRGBA.Green);
		skdmat.getAdditionalRenderState().setDepthTest(false);
		skd.setMaterial(skdmat);
		((Node)spatial).attachChild(skd);
		return skd;
	}*/

	@Override
	protected void controlUpdate(float tpf)
	{
		for(int i = 1; i < pagekeep; i++)
			if(progress[i] < 1)
			{
				progress[i] += tpf * 0.1f;
				if(progress[i] >= 1)
				{
					progress[i] = 1;
					//setPage(null, i, true);
					//setPage(null, i, false);
				}
				Quaternion r1 = new Quaternion().fromAngleAxis(FastMath.PI + FastMath.PI * progress[i] * 1.1f, Vector3f.UNIT_Z);
				Quaternion r2 = new Quaternion().fromAngleAxis(FastMath.PI * progress[i], Vector3f.UNIT_Z);
				//sk.getBone((skSize + 1) * 2 * (i - 1)).setUserTransforms(Vector3f.ZERO, r1, Vector3f.UNIT_XYZ);
				//sk.getBone((skSize + 1) * 2 * i - (skSize + 1)).setUserTransforms(Vector3f.ZERO, r2, Vector3f.UNIT_XYZ);
				//sk.getBone((skSize + 1) * 2 * i).setUserTransforms(Vector3f.ZERO, r1, Vector3f.UNIT_XYZ);
				//sk.getBone((skSize + 1) * 2 * i + (skSize + 1)).setUserTransforms(Vector3f.ZERO, r2, Vector3f.UNIT_XYZ);
			}
		/*if(pagekeep > 0)
			sk.updateWorldVectors();*/
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp)
	{

	}
}