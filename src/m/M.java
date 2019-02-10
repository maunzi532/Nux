package m;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class M
{
	public static HashMap<String, Entry1> entries = new HashMap<>();
	public static String aktuell;

	public static void main(String[] args)
	{
		init(args[0]);
		check();
		los();
	}

	public static void init(String filename)
	{
		Tag1.init();
		String text;
		try
		{
			text = new String(Files.readAllBytes(new File(filename).toPath()), Charset.forName("UTF-8"));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		text = text.trim();
		String[] input = text.split("\n\n");
		for(int i = 0; i < input.length; i++)
		{
			Entry1 e1 = new Entry1(input[i]);
			if(entries.containsKey(e1.name))
				throw new RuntimeException("Doppelter Schlüssel: " + e1.name);
			entries.put(e1.name, e1);
			if(aktuell == null)
				aktuell = e1.name;
		}
	}

	public static void check()
	{
		HashSet<String> ends = new HashSet<>();
		entries.values().forEach(e -> ends.addAll(e.tags.stream().filter(e1 -> e1.type == Tag1Type.OPTION).map(e1 -> e1.value).collect(Collectors.toList())));
		ArrayList<String> unresolved1 = new ArrayList<>(ends);
		unresolved1.removeAll(entries.keySet());
		if(!unresolved1.isEmpty())
			System.out.println("Referenziert, aber nicht definiert: " + unresolved1);
		ArrayList<String> unresolved2 = new ArrayList<>(entries.keySet());
		unresolved2.removeAll(ends);
		unresolved2.remove(aktuell);
		if(!unresolved2.isEmpty())
			System.out.println("Definiert, aber nicht referenziert: " + unresolved2);
		System.out.println("Anzahl Schlüssel: " + entries.size() + '\n');
	}

	public static void los()
	{
		Wugu wugu = new Wugu();
		Scanner sca = new Scanner(System.in);
		wugu.run(entries.get(aktuell));
		System.out.println(wugu.output.toString());
		while(!wugu.output.minus7leben && !wugu.output.finish && wugu.output.options > 0)
		{
			while(true)
			{
				String s = sca.next();
				if(s.startsWith("c"))
				{
					aktuell = s.substring(1);
					break;
				}
				Optional<TextBlock> x1 = wugu.output.blocks.stream().filter(e -> e.link != null).filter(e -> s.equals(e.link.ziel)).findAny();
				if(x1.isPresent() && giveItems(wugu, x1.get().link, sca))
				{
					aktuell = x1.get().link.ziel;
					break;
				}
			}
			wugu.run(entries.get(aktuell));
			System.out.println(wugu.output.toString());
		}
		if(wugu.output.minus7leben)
			System.out.println("<minus7leben>");
		else if(wugu.output.finish)
			System.out.println("Gewonnen");
		else
			System.out.println("Fehler, keine Optionen");
	}

	public static boolean giveItems(Wugu store, Option1 req, Scanner sca)
	{
		if(req.give.isEmpty())
			return true;
		ArrayList<Item> give1 = new ArrayList<>(store.items);
		ArrayList<Item> give2 = new ArrayList<>();
		for(String req1 : req.give)
		{
			System.out.println("Item geben vom Typ " + req1);
			List<Item> m = give1.stream().filter(e -> e.category.contains(req1)).collect(Collectors.toList());
			if(m.isEmpty())
			{
				System.out.println("Keine passenden Items");
				return false;
			}
			System.out.println("Möglichkeiten:");
			for(int i = 0; i < m.size(); i++)
				System.out.println(i + ": " + m.get(i).name);
			String s = sca.next();
			try
			{
				int n1 = Integer.parseInt(s);
				if(n1 >= 0 && n1 < m.size())
				{
					give1.remove(m.get(n1));
					give2.add(m.get(n1));
					System.out.println(m.get(n1).name + " gegeben\n");
				}
				else
					return false;
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
		store.items = give1;
		store.give = give2;
		return true;
	}
}