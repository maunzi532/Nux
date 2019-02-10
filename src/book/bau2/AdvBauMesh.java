package book.bau2;

public class AdvBauMesh extends BauMesh2
{
	public void indexQ(boolean inv, Position2... pZ)
	{
		indexPoly(inv, pZ[0], pZ[1], pZ[3], pZ[2]);
	}

	public void indexQ(boolean inv, DualP2 p0)
	{
		indexPoly(inv, p0, p0.sCl(1), p0.sCl(2), p0.sCl(3));
	}

	public void cylinder1(boolean inv, int type, DualP2... p2a)
	{
		switch(type)
		{
			case 0:
				cylinder(inv, p2a, 0, 0, 1, 0, 0, 1, 1, 1, 0, 2, 1, 2, 0, 3, 1, 3);
				break;
			case 1:
				cylinder(inv, p2a, 0, 0, 2, 0, 1, 0, 3, 0, 1, 1, 3, 1, 0, 1, 2, 1,
						0, 2, 2, 2, 1, 2, 3, 2, 1, 3, 3, 3, 0, 3, 2, 3);
				break;
		}
	}

	public void cylinder2(boolean inv, int edgc, DualP2... p2a)
	{
		int[] dec = new int[edgc * 8];
		for(int i = 0; i < edgc; i++)
		{
			dec[i * 4] = 0;
			dec[i * 4 + 1] = i + edgc * DualP2.EDGEC;
			dec[i * 4 + 2] = 1;
			dec[i * 4 + 3] = i + edgc * DualP2.EDGEC;
			dec[i * 4 + 4] = 0;
			dec[i * 4 + 5] = (i + 1) % edgc + edgc * DualP2.EDGEC;
			dec[i * 4 + 6] = 1;
			dec[i * 4 + 7] = (i + 1) % edgc + edgc * DualP2.EDGEC;
		}
		cylinder(inv, p2a, dec);
	}

	public void cylinder(boolean inv, DualP2[] p2a, int... dec)
	{
		int div = 4;
		int dv2 = 2;
		int s = dec.length / div;
		for(int i = 0; i < s; i++)
		{
			Position2[] pZ = new Position2[4];
			for(int k = 0; k < 4; k++)
			{
				int index = i * div + dv2 * k;
				if(index >= s * div)
					index -= s * div;
				pZ[k] = p2a[dec[index]].sCl(dec[index + 1]);
			}
			indexQ(inv, pZ);
		}
	}

	public void cylinder(boolean inv, DualP2[] p2a, AutoNumBone[] nb, int... dec)
	{
		int div = 6;
		int dv2 = 3;
		int s = dec.length / div;
		for(int i = 0; i < s; i++)
		{
			Position2[] pZ = new Position2[4];
			for(int k = 0; k < 4; k++)
			{
				int index = i * div + dv2 * k;
				if(index >= s * div)
					index -= s * div;
				pZ[k] = p2a[dec[index]].sCl(dec[index + 1], nb[index + 2]);
			}
			indexQ(inv, pZ);
		}
	}

	public void abdeck(boolean inv, int edgc, DualP2 p2a)
	{
		Position2[] p = new Position2[edgc];
		for(int i = 0; i < edgc; i++)
			p[i] = p2a.sCl(i + edgc * DualP2.EDGEC);
		indexPoly(inv, p);
	}
}