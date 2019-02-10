package m.nuxc2.state;

public enum Tag2Type
{
	T(1),
	COLOR(1),
	STYLE(1),
	IMAGE0(1),
	IMAGE1(1),
	OPTION(1),
	MINUS7LEBEN(1),
	FINISH(0),
	ENDCREATE(0),

	W(2),
	ATLEAST(2),
	ATMOST(2),

	STATSET(1),
	STATCHANGE(1),
	STATREMOVE(1),
	IFSTAT(1),

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

	Tag2Type(int t2)
	{
		this.t2 = t2;
	}
}