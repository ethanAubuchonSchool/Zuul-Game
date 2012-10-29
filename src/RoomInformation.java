import java.util.*;


public class RoomInformation {
	private String name;
	private List<String> exits;
	private List<String> items;
	private List<String> monsters;
	
	public RoomInformation(String name,String[] exits,String[] items,String[] monsters)
	{
		this.name = name;
		
		this.exits = new ArrayList<String>();
		this.exits = intialize(exits);
		
		this.items = new ArrayList<String>();
		this.items = intialize(items);
		
		this.monsters = new ArrayList<String>();
		this.monsters = intialize(monsters);
	}
	
	public ArrayList<String> intialize(String[] s)
	{
		ArrayList<String> a = new ArrayList<String>();
		for(int i = 0; i < s.length; i++)
		{
			a.add(s[i]);
		}
		return a;
	}
}
