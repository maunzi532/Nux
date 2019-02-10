package book.nuxs2.prerender;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.imageio.*;

public class Loader
{
	private static HashMap<File, String> text = new HashMap<>();
	private static HashMap<File, BufferedImage> bild = new HashMap<>();
	private static String prefix;

	public static void setPrefix(String s)
	{
		prefix = s + File.separator;
	}

	public static String gibText(String f)
	{
		return gibText(new File(prefix + f));
	}

	public static String gibText(File f)
	{
		if(text.containsKey(f))
			return text.get(f);
		try
		{
			String re = new String(Files.readAllBytes(f.toPath()), Charset.forName("UTF-8"));
			text.put(f, re);
			return re;
		}catch(IOException e)
		{
			text.put(f, null);
			return null;
		}
	}

	public static BufferedImage gibBild(String f)
	{
		return gibBild(new File(prefix + f));
	}

	public static BufferedImage gibBild(File f)
	{
		if(bild.containsKey(f))
			return bild.get(f);
		try
		{
			BufferedImage re = ImageIO.read(f);
			bild.put(f, re);
			return re;
		}catch(IOException e)
		{
			bild.put(f, null);
			return null;
		}
	}
}