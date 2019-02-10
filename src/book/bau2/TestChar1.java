package book.bau2;

import com.jme3.animation.*;
import com.jme3.math.*;

public class TestChar1 extends AdvGeometry
{
	@Override
	protected void setup()
	{
		AutoNumBone b1 = new AutoNumBone(boneList, Vector3f.ZERO, new Quaternion().fromAngleAxis(1, Vector3f.UNIT_Y), null);
		AutoNumBone b2 = new AutoNumBone(boneList, Vector3f.UNIT_X, new Quaternion().fromAngleAxis(1, Vector3f.UNIT_X), b1);
		DualP2 p1 = new DualP2(new Vector3f(-2, 0, -2), new Vector3f(-1, -1, -1), new Vector2f(0f, 1f), b1);
		DualP2 p2 = new DualP2(new Vector3f(-2, 2, -2), new Vector3f(-1, 1, -1), new Vector2f(0f, 0f), b2);
		bau.indexQ(false, p1);
		bau.indexQ(true, p2);
		bau.cylinder1(false, 0, p1, p2);
		for(Bone b : boneList)
			b.setUserControl(true);
	}
}