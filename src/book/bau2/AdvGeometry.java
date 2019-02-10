package book.bau2;

import com.jme3.animation.*;
import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.scene.debug.*;
import java.util.*;

public abstract class AdvGeometry
{
	public Geometry geom;
	public Skeleton sk;
	public AdvBauMesh bau;
	public ArrayList<Bone> boneList;

	public void start()
	{
		bau = new AdvBauMesh();
		boneList = new ArrayList<>();
		setup();
		sk = new Skeleton(boneList.toArray(new Bone[boneList.size()]));
		Mesh m = new Mesh();
		bau.fertig(m);
		geom = new Geometry("W", m);
	}

	public void start(Skeleton sk)
	{
		bau = new AdvBauMesh();
		this.sk = sk;
		setup();
		Mesh m = new Mesh();
		bau.fertig(m);
		geom = new Geometry("W", m);
	}

	protected abstract void setup();

	public SkeletonControl controlSetup()
	{
		SkeletonControl skc = new SkeletonControl(sk);
		geom.getParent().addControl(skc);
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
		geom.getParent().attachChild(skd);
		return skd;
	}
}