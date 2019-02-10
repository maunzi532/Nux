package book.bau2;

import com.jme3.math.*;
import java.util.*;

public class DualP2 extends Position2
{
	public static final int CLONE = 0;
	public static final int MIRROR_X = 1; //1->2inv 3->4inv
	public static final int MIRROR_Z = 3; //1->4inv 2->3inv
	public static final int SPIN_TEX_R = 2; //1->3 2->4
	public static final int SPIN_TEX_L = -2; //3->1 4->2
	public static final int EDGEC = 256;

	public HashMap<Integer, Position2> clones;

	public DualP2(Vector3f position, Vector2f texCoord, AutoNumBone bone)
	{
		super(position, texCoord, bone);
		clones = new HashMap<>();
	}

	public DualP2(Vector3f position, Vector3f normal, Vector2f texCoord, AutoNumBone bone)
	{
		super(position, normal, texCoord, bone);
		clones = new HashMap<>();
	}

	public DualP2(Vector3f position, Vector2f texCoord, BoneC2... boneC)
	{
		super(position, texCoord, boneC);
		clones = new HashMap<>();
	}

	public DualP2(Vector3f position, Vector3f normal, Vector2f texCoord, BoneC2... boneC)
	{
		super(position, normal, texCoord, boneC);
		clones = new HashMap<>();
	}

	public DualP2 autoNormal(Vector3f p0, Vector3f p1, boolean inv)
	{
		if(inv)
			normal = p1.cross(p0).normalizeLocal();
		else
			normal = p0.cross(p1).normalizeLocal();
		return this;
	}

	public DualP2 autoNormal()
	{
		normal = position.normalize();
		return this;
	}

	public Position2 sCl(int type)
	{
		return sCl(type, boneC);
	}

	public Position2 sCl(int type, AutoNumBone bone1)
	{
		return sCl(type, new BoneC2(bone1, 1));
	}

	public Position2 sCl(int type, BoneC2... boneC1)
	{
		if(type == CLONE)
			return this;
		if(clones.containsKey(type))
			return clones.get(type);
		Position2 clone = createClone(type, boneC1);
		clones.put(type, clone);
		return clone;
	}

	public Position2 createClone(int type, BoneC2... boneC1)
	{
		Vector3f position1;
		Vector3f normal1;
		Vector2f texCoord1;
		if(type > EDGEC)
		{
			int edgec = type / EDGEC;
			int edgn = type % EDGEC;
			Transform t1 = new Transform(new Quaternion().fromAngleAxis(FastMath.TWO_PI * edgn / edgec, Vector3f.UNIT_Y));
			position1 = t1.transformVector(position, null);
			normal1 = t1.transformVector(normal, null);
			texCoord1 = new Vector2f(texCoord.x + (float) edgn / edgec, texCoord.y);
		}
		else switch(type)
		{
			case MIRROR_X:
				position1 = new Vector3f(-position.x, position.y, position.z);
				normal1 = new Vector3f(-normal.x, normal.y, normal.z);
				if(texCoord.x < 0.5f)
					texCoord1 = new Vector2f(0.5f - texCoord.x, texCoord.y);
				else
					texCoord1 = new Vector2f(1.5f - texCoord.x, texCoord.y);
				break;
			case MIRROR_Z:
				position1 = new Vector3f(position.x, position.y, -position.z);
				normal1 = new Vector3f(normal.x, normal.y, -normal.z);
				texCoord1 = new Vector2f(1f - texCoord.x, texCoord.y);
				break;
			case SPIN_TEX_R:
				position1 = new Vector3f(-position.x, position.y, -position.z);
				normal1 = new Vector3f(-normal.x, normal.y, -normal.z);
				texCoord1 = new Vector2f(texCoord.x + 0.5f, texCoord.y);
				break;
			case SPIN_TEX_L:
				position1 = new Vector3f(-position.x, position.y, -position.z);
				normal1 = new Vector3f(-normal.x, normal.y, -normal.z);
				texCoord1 = new Vector2f(texCoord.x - 0.5f, texCoord.y);
				break;
			default:
				position1 = position;
				normal1 = normal;
				texCoord1 = texCoord;
		}
		return new Position2(position1, normal1, texCoord1, boneC1, boneBind);
	}
}