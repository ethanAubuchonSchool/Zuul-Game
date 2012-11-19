package model;

import model.object.*;

public class Wall {
	private Room exit;
	private Item item;
	private Monster monster;
	
	public Wall(Room exit, Item item, Monster monster)
	{
		setExit(exit);
		setItem(item);
		setMonster(monster);
	}
	
	public Wall(Room exit)
	{
		this(exit,null,null);
	}
	
	public Wall()
	{
		this(null);
	}

	public Room getExit() {
		return exit;
	}

	public void setExit(Room exit) {
		this.exit = exit;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}
	
	public boolean hasMonster(String name)
	{
		if(hasMonster())
			return monster.getName().equals(name);
		
		return false;
	}
	
	public boolean hasMonster()
	{
		return monster!=null;
	}
	
	public boolean hasItem(String name)
	{
		if(hasItem())
			return item.getItemName().equals(name);
		
		return false;
	}
	
	public boolean hasItem()
	{
		return item!=null;
	}
	
	public boolean hasExit()
	{
		return exit!=null;
	}
}
