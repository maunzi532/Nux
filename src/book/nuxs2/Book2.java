package book.nuxs2;

import book.nuxs2.prerender.*;
import com.jme3.app.*;
import com.jme3.collision.*;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.control.*;
import java.util.*;
import m.nuxc2.*;
import m.nuxc2.state.*;

public class Book2 extends AbstractControl implements ActionListener
{
	private SimpleApplication app;
	private ThreadSummoner threadSummoner;
	private SkelePage2 pageL;
	private SkelePage2 pageR;
	private Path2 currentPath;
	private String require;
	private Markable clLink;

	public Book2(SimpleApplication app, ThreadSummoner threadSummoner)
	{
		this.app = app;
		this.threadSummoner = threadSummoner;
		app.getInputManager().addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		app.getInputManager().addListener(this, "Click");

	}

	@Override
	public void setSpatial(Spatial spatial)
	{
		super.setSpatial(spatial);
		setEnabled(true);
	}

	public void updatePages()
	{
		Node node = (Node) spatial;
		if(pageL != null)
			node.detachChild(pageL.getSpatial());
		if(pageR != null)
			node.detachChild(pageR.getSpatial());
		if(threadSummoner.pathed.pageL != null)
		{
			pageL = new SkelePage2(6, 10, 10, threadSummoner.pathed.pageL);
			Node nodeL = new Node("L");
			nodeL.addControl(pageL);
			node.attachChild(nodeL);
		}
		if(threadSummoner.pathed.pageR != null)
		{
			pageR = new SkelePage2(6, 10, 10, threadSummoner.pathed.pageR);
			Node nodeR = new Node("R");
			nodeR.addControl(pageR);
			node.attachChild(nodeR);
		}
		threadSummoner.summonThreads();
		innerUpdate();
	}

	@Override
	protected void controlUpdate(float tpf)
	{
		if(clLink != null)
		{
			if(clLink instanceof Option2)
			{
				currentPath = new Path2((Option2) clLink);
				checkNewPage();
			}
			else if(clLink instanceof Item2)
			{
				if(currentPath != null && !currentPath.given.contains(clLink) &&
						((Item2) clLink).category.contains(require))
				{
					currentPath.given.add((Item2) clLink);
					Collections.sort(currentPath.given);
					checkNewPage();
				}
			}
			else if(clLink instanceof Stat2)
			{
				innerUpdate();
			}
			else if(clLink instanceof Reset2)
			{
				innerUpdate();
			}
			clLink = null;
		}
	}

	private void checkNewPage()
	{
		if(threadSummoner.pathOk(currentPath))
		{
			threadSummoner.decide(currentPath);
			currentPath = null;
			require = null;
			updatePages();
		}
		else
		{
			require = currentPath.option.give.get(currentPath.given.size());
			innerUpdate();
		}
	}

	private void innerUpdate()
	{
		if(pageL != null)
			pageL.innerUpdate(null, threadSummoner.paths, currentPath, require);
		if(pageR != null)
			pageR.innerUpdate(null, threadSummoner.paths, currentPath, require);
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp){}

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{
		if("Click".equals(name) && isPressed)
		{
			Vector3f origin = app.getCamera().getWorldCoordinates(app.getInputManager().getCursorPosition(), 0.0f);
			Vector3f direction = app.getCamera().getWorldCoordinates(app.getInputManager().getCursorPosition(), 0.3f);
			direction.subtractLocal(origin).normalizeLocal();
			Ray ray = new Ray(origin, direction);
			CollisionResults results = new CollisionResults();
			spatial.collideWith(ray, results);
			if(results.size() > 0)
			{
				Geometry g = results.getClosestCollision().getGeometry();
				clLink = g.getUserData("Link");
			}
		}
	}
}