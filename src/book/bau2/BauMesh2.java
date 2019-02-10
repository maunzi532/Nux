package book.bau2;

import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.util.*;
import java.util.*;

public class BauMesh2
{
	private ArrayList<Vector3f> positions = new ArrayList<>();
	private ArrayList<Index3> indexes = new ArrayList<>();
	private ArrayList<Vector3f> normals = new ArrayList<>();
	private ArrayList<Vector2f> texCoord = new ArrayList<>();
	private ArrayList<Byte> bIndices = new ArrayList<>();
	private ArrayList<Float> bWeights = new ArrayList<>();
	private int maxNumW = 1;

	protected void fertig(Mesh mesh)
	{
		HashSet<Position2> filled = new HashSet<>();
		int[] indexes2 = new int[indexes.size() * 3];
		for(int i = 0; i < indexes.size(); i++)
			for(int j = 0; j < 3; j++)
			{
				Position2 p2 = indexes.get(i).data[j];
				if(!filled.contains(p2))
				{
					fillPosition(p2);
					filled.add(p2);
				}
				indexes2[i * 3 + j] = p2.num;
			}
		mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes2));
		mesh.setBuffer(VertexBuffer.Type.Position, 3,
				BufferUtils.createFloatBuffer(positions.toArray(new Vector3f[positions.size()])));
		mesh.setBuffer(VertexBuffer.Type.Normal, 3,
				BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[positions.size()])));
		mesh.setBuffer(VertexBuffer.Type.TexCoord, 2,
				BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[positions.size()])));
		mesh.updateBound();

		VertexBuffer indicesHW = new VertexBuffer(VertexBuffer.Type.HWBoneIndex);
		indicesHW.setUsage(VertexBuffer.Usage.CpuOnly);
		mesh.setBuffer(indicesHW);
		VertexBuffer weightsHW = new VertexBuffer(VertexBuffer.Type.HWBoneWeight);
		weightsHW.setUsage(VertexBuffer.Usage.CpuOnly);
		mesh.setBuffer(weightsHW);

		byte[] bIndices2 = new byte[bIndices.size()];
		for(int i = 0; i < bIndices2.length; i++)
			bIndices2[i] = bIndices.get(i);
		VertexBuffer indicesBuf = new VertexBuffer(VertexBuffer.Type.BoneIndex);
		indicesBuf.setupData(VertexBuffer.Usage.CpuOnly, 4,
				VertexBuffer.Format.UnsignedByte, BufferUtils.createByteBuffer(bIndices2));
		mesh.setBuffer(indicesBuf);
		float[] bWeights2 = new float[bWeights.size()];
		for(int i = 0; i < bWeights2.length; i++)
			bWeights2[i] = bWeights.get(i);
		VertexBuffer weightsBuf = new VertexBuffer(VertexBuffer.Type.BoneWeight);
		weightsBuf.setupData(VertexBuffer.Usage.CpuOnly, 4,
				VertexBuffer.Format.Float, BufferUtils.createFloatBuffer(bWeights2));
		mesh.setBuffer(weightsBuf);
		mesh.setMaxNumWeights(maxNumW);
		mesh.generateBindPose(true);
	}

	public void fillPosition(Position2 p2)
	{
		p2.num = positions.size();
		positions.add(p2.positionT());
		normals.add(p2.normalT());
		texCoord.add(p2.texCoord);
		if(maxNumW < p2.boneC.length)
			maxNumW = p2.boneC.length;
		for(int i = 0; i < 4; i++)
			if(i < p2.boneC.length)
			{
				bIndices.add((byte) p2.boneC[i].bone.num);
				bWeights.add(p2.boneC[i].weight);
			}
			else
			{
				bIndices.add((byte) 0);
				bWeights.add(0f);
			}
	}

	public void indexPoly(boolean inv, Position2... p)
	{
		for(int i = 2; i < p.length; i++)
			indexes.add(new Index3(p[0], p[i - 1], p[i], inv));
	}

	private class Index3
	{
		public Position2[] data;

		public Index3(Position2 i0, Position2 i1, Position2 i2, boolean inv)
		{
			if(inv)
				data = new Position2[]{i0, i2, i1};
			else
				data = new Position2[]{i0, i1, i2};
		}
	}
}