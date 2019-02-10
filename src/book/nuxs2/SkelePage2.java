package book.nuxs2;

import book.nuxs2.render.*;
import com.jme3.animation.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.Node;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import java.util.*;
import java.util.stream.*;
import m.nuxc2.*;
import m.nuxc2.state.*;

public class SkelePage2 extends AbstractControl
{
	private Skeleton ska;
	private int skSize;
	private float pageW;
	private float pageH;
	public Page2 page;
	public Map<Markable, List<SliceGeom2>> links;

	public SkelePage2(int skSize, float pageW, float pageH, Page2 page)
	{
		this.skSize = skSize;
		this.pageW = pageW;
		this.pageH = pageH;
		this.page = page;
		Bone[] ska1 = new Bone[skSize];
		Bone lastx = new Bone("W");
		lastx.setBindTransforms(Vector3f.ZERO, new Quaternion().fromAngleAxis(FastMath.HALF_PI * (page.lr ? -1 : 1), Vector3f.UNIT_Z), Vector3f.UNIT_XYZ);
		ska1[0] = lastx;
		for(int j = 1; j < skSize; j++)
		{
			Bone newx = new Bone("W");
			newx.setBindTransforms(new Vector3f(0, pageW / (skSize - 1), 0), Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
			lastx.addChild(newx);
			ska1[j] = newx;
			lastx = newx;
		}
		for(Bone b : ska1)
			b.setUserControl(true);
		ska = new Skeleton(ska1);
	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		ArrayList<SliceGeom2> slg1 = new ArrayList<>();
		for(Slice2 sl : page.slices)
		{
			SliceGeom2 slg = new SliceGeom2(sl, ska, page.lr, skSize, pageW, pageH);
			slg1.add(slg);
			((Node) spatial).attachChild(slg.geom);
		}
		links = slg1.stream().filter(e -> e.slice.link != null).collect(Collectors.groupingBy(e -> e.slice.link));
	}

	public void innerUpdate(Markable marked, List<Path2> paths, Path2 currentPath, String require)
	{
		for(Map.Entry<Markable, List<SliceGeom2>> entry : links.entrySet())
		{
			int w = 0;
			if(entry.getKey() instanceof Option2)
			{
				if(paths.stream().filter(e -> e.option == entry.getKey()).count() == 0)
					w = 2;
				else if(currentPath != null && currentPath.option == entry.getKey())
					w = 4;
			}
			else if(entry.getKey() instanceof Item2)
			{
				if(currentPath != null)
				{
					if(currentPath.given.contains(entry.getKey()))
						w = 2;
					else if(((Item2) entry.getKey()).category.contains(require))
						w = 4;
				}
			}
			/*else if(entry.getKey() instanceof Stat2)
			{

			}
			else if(entry.getKey() instanceof Reset2)
			{

			}*/
			int w2 = w + (entry.getKey() == marked ? 1 : 0);
			entry.getValue().forEach(e -> e.updateTexture(w2));
		}
	}

	@Override
	protected void controlUpdate(float tpf)
	{

	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp)
	{

	}
}