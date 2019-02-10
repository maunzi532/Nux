package book;

import com.jme3.app.*;
import com.jme3.collision.*;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.light.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.system.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import m.*;

public class Main2 extends SimpleApplication implements ActionListener
{
	public static Material mat1;
	public static BufferedImage pageBG;
	public static BufferedImage minus7;
	public static BufferedImage deko;
	public static BufferedImage gewonnen;
	public static BufferedImage cover;
	public static String directory = "Nuxi_2";
	public SkeleBook skeleBook;
	public Wugu wugu;
	public int clPage = -1;
	public String clCode = null;
	public String nextLink;
	public ArrayList<String> require;

	private static int okstart = 0;

	public static void main(String[] args)
	{
		if(args.length > 0)
			directory = args[0];
		Main2 app = new Main2();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		settings.put("Width", 1600);
		settings.put("Height", 900);
		app.setSettings(settings);
		//System.out.println(settings.toString().replace(' ', '\n'));
		app.start();
		M.init(directory + File.separator + "Text");
		M.check();
		okstart = 1;
	}

	public BufferedImage nullableImage(String path)
	{
		try
		{
			return ImageIO.read(new File(directory + File.separator + path));
		}catch(IOException e)
		{
			return null;
		}
	}

	@Override
	public void simpleInitApp()
	{
		setDisplayStatView(false);
		setDisplayFps(false);
		flyCam.setMoveSpeed(10);
		flyCam.setDragToRotate(true);

		inputManager.addMapping(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping(CameraInput.FLYCAM_STRAFELEFT, new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping(CameraInput.FLYCAM_STRAFERIGHT, new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping(CameraInput.FLYCAM_FORWARD, new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping(CameraInput.FLYCAM_BACKWARD, new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping(CameraInput.FLYCAM_RISE, new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_O));
		inputManager.addMapping(CameraInput.FLYCAM_ROTATEDRAG, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		getCamera().setRotation(new Quaternion(0, 5, -5f, 0).normalizeLocal());
		getCamera().setLocation(new Vector3f(0, 15, 6));

		inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Click");

		DirectionalLight light = new DirectionalLight();
		light.setDirection(new Vector3f(-2, -3, -1).normalizeLocal());
		light.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(light);
		AmbientLight light3 = new AmbientLight();
		light3.setColor(ColorRGBA.White.mult(0.2f));
		rootNode.addLight(light3);

		mat1 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		pageBG = nullableImage("PageBG.png");
		minus7 = nullableImage("Minus7leben.png");
		deko = nullableImage("Deko.png");
		gewonnen = nullableImage("Gewonnen.png");
		cover = nullableImage("Cover.png");

		Node n = new Node();
		rootNode.attachChild(n);
		skeleBook = new SkeleBook(1, 6, 10, 10);
		n.addControl(skeleBook);
		//skeleBook.debugger(assetManager);
		//skeleBook.controlSetup();
		skeleBook.setPage(new Page(cover), 0, false);
		//skeleBook.setPage(new Page(new ArrayList<>(), null), 0, true);
		wugu = new Wugu();
		while(okstart == 0);
		nextLink = M.aktuell;
		require = new ArrayList<>();
	}

	@Override
	public void simpleUpdate(float tpf)
	{
		if(okstart == 0)
			return;
		if(okstart == 1)
		{
			inputManager.deleteTrigger(CameraInput.FLYCAM_ROTATEDRAG, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
			okstart = 2;
		}
		if(okstart == 2)
		{
			if(clPage != -1)
			{
				clPage = -1;
				clCode = null;
				okstart = 3;
			}
			return;
		}
		if(clPage != -1)
		{
			if(clCode != null && clPage == 1)
			{
				Optional<TextBlock> x1 = wugu.output.blocks.stream().filter(e -> e.link != null).filter(e -> clCode.equals(e.link.ziel)).findAny();
				if(x1.isPresent())
				{
					require = x1.get().link.give;
					nextLink = x1.get().link.ziel;
				}
			}
			if(clPage == 0 && nextLink != null)
			{
				if(wugu.tryGive(clCode))
					turnPage();
				else
					skeleBook.setPage(new Page(wugu.itemView(), deko), 0, true);
			}
			clPage = -1;
			clCode = null;
		}
		if(nextLink != null && require != null)
		{
			if(wugu.tryOption(require))
				turnPage();
			else
				skeleBook.setPage(new Page(wugu.itemView(), deko), 0, true);
			require = null;
		}
	}

	public void turnPage()
	{
		wugu.run(M.entries.get(nextLink));
		nextLink = null;
		skeleBook.movePages();
		skeleBook.setPage(new Page(wugu.output.blocks, wugu.output.minus7leben ? minus7 :
				wugu.output.finish ? gewonnen : wugu.output.options == 0 ? deko : null), 0, false);
		skeleBook.setPage(new Page(wugu.itemView(), deko), 0, true);
	}

	@Override
	public void simpleRender(RenderManager rm){}

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{
		if("Click".equals(name) && isPressed)
		{
			Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
			Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
			direction.subtractLocal(origin).normalizeLocal();
			Ray ray = new Ray(origin, direction);
			CollisionResults results = new CollisionResults();
			skeleBook.getSpatial().collideWith(ray, results);
			if(results.size() > 0)
			{
				Geometry g = results.getClosestCollision().getGeometry();
				clCode = g.getUserData("Link");
				clPage = g.getUserData("Page");
			}
		}
	}
}