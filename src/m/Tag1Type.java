package m;

public enum Tag1Type
{
	T(1),
	OPTION(1),
	MINUS7LEBEN(0),
	FINISH(0),
	ENDCREATE(0),

	STATCREATE(1),
	STATCHANGE(1),
	STATREMOVE(1),
	IFSTAT(1),
	W(2),
	DIEONZERO(0),

	ITEMGET(1),
	CATEGORY(1),
	ITEMREMOVE(1),
	IFITEM(1),

	GIVEITEM(1),
	GIVECLEAR(0),
	GIVEGET(0),

	CIRCGET(1),
	CIRCREMOVE(1),
	IFCIRC(1),

	ELSE(0),
	END(0);

	int t2;

	Tag1Type(int t2)
	{
		this.t2 = t2;
	}
}