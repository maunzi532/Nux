package m.nuxc2.exechelper;

import m.nuxc2.*;
import m.nuxc2.state.*;
import m.nuxc2.state.textblock.*;

public class ItemDefine extends ExecHelper
{
	private Item2 item;

	public ItemDefine(Exec2 main, Tag2 tag)
	{
		super(main);
		item = new Item2(tag.value);
		main.end.items.put(item.name, item);
	}

	@Override
	public boolean process(Tag2 tag)
	{
		switch(tag.type)
		{
			case CATEGORY:
				item.category.add(tag.value);
				return true;
			default:
				finish();
				return false;
		}
	}

	@Override
	public void finish()
	{
		main.textInfo(item.name + " erhalten", TextColor2.ITEM);
		main.itemDefine = null;
	}
}