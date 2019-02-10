package book.bau2;

import com.jme3.animation.*;
import com.jme3.math.*;
import java.util.*;

public class AutoNumBone
{
	public int num;
	public Bone b;
	public Transform t;

	public AutoNumBone(ArrayList<Bone> list, Vector3f translation, Quaternion rotation, AutoNumBone ext)
	{
		num = list.size();
		b = new Bone(String.valueOf(num));
		b.setBindTransforms(translation, rotation, Vector3f.UNIT_XYZ);
		if(ext != null)
			ext.b.addChild(b);
		//b.updateModelTransforms();
		//t = b.getModelBindInverseTransform().invert();
		t = new Transform(translation, rotation);
		if(ext != null)
			t.combineWithParent(ext.t);
		list.add(b);
	}

	public AutoNumBone(int num)
	{
		this.num = num;
	}
}