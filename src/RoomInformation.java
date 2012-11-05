import java.util.*;


public class RoomInformation {
	private String name;
	private List<String> exits;
	private List<String> items;
	private List<String> monsters;
	
	public RoomInformation(String name,String[] exits,String[] items,String[] monsters)
	{
		this.setName(name);
		
		this.setExits(new ArrayList<String>());
		this.setExits(initialise(exits));
		
		this.setItems(new ArrayList<String>());
		this.setItems(initialise(items));
		
		this.setMonsters(new ArrayList<String>());
		this.setMonsters(initialise(monsters));
	}
	
	public RoomInformation()
	{
		this(null,null,null,null);
	}
	
	public ArrayList<String> initialise(String[] s)
	{
		ArrayList<String> a = new ArrayList<String>();
		
		for(int i = 0;i < s.length; i++)
		{
			a.add(s[i]);
		}
		return a;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExits() {
		return exits;
	}

	public void setExits(List<String> exits) {
		this.exits = exits;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<String> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<String> monsters) {
		this.monsters = monsters;
	}

	
}
