package model;
import java.util.*;

import model.object.Item;
import model.object.Monster;

/**
 * Class Room - a room in an adventure game.
 * 
 * This class is part of the "World of Zuul" application. "World of Zuul" is a
 * very simple, text based adventure game.
 * 
 * A "Room" represents one location in the scenery of the game. It is connected
 * to other rooms via exits. The exits are labelled north, east, south, west.
 * For each direction, the room stores a reference to the neighboring room, or
 * null if there is no exit in that direction.
 * 
 */
public class Room {
	private String description;
	private HashMap<String, Wall> walls;
	private boolean visited;

	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The room's description.
	 */
	public Room(String description) {
		this.description = description;
		initWalls();
		visited = false;
	}
	
	/**
	 * Initialises four walls
	 */
	private void initWalls()
	{
		String[] direction = new Parser().getDirections();
		walls = new HashMap<String, Wall>();
		for(int i = 0; i < direction.length; i++)
		{
			walls.put(direction[i], new Wall());
		}
	}

	/**
	 * Define the exits of this room and ends them to corresponding wall. Every direction either leads to another
	 * room or is null (no exit there).
	 * 
	 * @param north
	 *            The north exit.
	 * @param east
	 *            The east east.
	 * @param south
	 *            The south exit.
	 * @param west
	 *            The west exit.
	 */
	public void setExits(String direction, Room neighbor) {
		if(walls.get(direction).hasExit())
			walls.get(direction).setExit(neighbor);
		else
			System.out.println("Exit Override Error:\nExit " + neighbor.getDescription() + " not added to " + this.description + " " + direction +" wall.");
	}

	/**
	 * @return The description of the room.
	 */
	public String getDescription() {
		return description;
	}

	public String getExitString() {

		String s = "Exits : ";
		Set<String> keys = walls.keySet();
		for (String exit : keys) {
			if(walls.get(exit).hasExit())
				s += " " + exit;
		}

		return s + "\n";

	}

	/**
	 * Return a long description of this room, of the form : you are in the
	 * kitchen Exits : north west
	 * 
	 * @return A description of the room, include exits
	 * 
	 */
	public String getLongDescription() {
		return ("You are at the " + description + ".\n" + getExitString() + getItemString() + "\n" + getMonstersString());
	}

	public Room getExit(String direction) {
		return walls.get(direction).getExit();
	}

	public void addItem(Item item, String wall) {
		if(walls.get(wall).hasItem(null))
			walls.get(wall).setItem(item);
		else
			System.out.println("Item Override Error:\nItem " + item.getItemName() + " not added to " + this.description + " " + wall +" wall.");
	}

	private String getItemString() {
		String itemString = "Items in room : ";
		Set<String> keys = walls.keySet();
		for (String item : keys) {
			Item i = walls.get(item).getItem();
			itemString += " Key : " + i.getItemName() + " Description : "
					+ i.getItemName() + " Weight : "
					+ i.getItemWeight() + "\n";
		}
		return itemString;
	}

	/**
	 * Author: Sean
	 * @return returns a list of the monsters in the room and their health
	 */
	private String getMonstersString() {
		String ret = "Monsters in room:\n";
		Set<String> keys = walls.keySet();
		for (String wall : keys) {
			if (walls.get(wall).getMonster().isAlive()) {
				ret += "- Name : " + walls.get(wall).getMonster().getName() + " (" + walls.get(wall).getMonster().getHealth() + ")\n";
			} else {
				ret += "- Name : " + walls.get(wall).getMonster().getName() + " (DEAD)\n";
			}
		}
		return ret;
	}

	public void removeItem(String itemKey) {
		Set<String> keys = walls.keySet();
		for (String wall : keys)
			if(walls.get(wall).hasItem(itemKey))
				walls.get(wall).setItem(null);
	}

	public Item getItem(String itemKey) {
		Set<String> keys = walls.keySet();
		for (String wall : keys)
			if(walls.get(wall).hasItem(itemKey))
				return walls.get(wall).getItem();
		return null;
	}

	public boolean containsItem(String itemKey) {
		Set<String> keys = walls.keySet();
		for (String wall : keys)
			if(walls.get(wall).hasItem(itemKey))
				return true;
		return false;
	}

	/**
	 * Modification by Ethan
	 * Adds monster to wall in room
	 * 
	 * Modification by Sean
	 * Adds a monster to the room
	 * @param key
	 * @param monster
	 */
	public void addMonster(Monster monster, String wall) {
		if(walls.get(wall).hasMonster(null))
			walls.get(wall).setMonster(monster);
		else
			System.out.println("Monster Override Error:\nMonster " + monster.getName() + " not added to " + this.description + " " + wall +" wall.");
	}

	/**
	 * Modification by Sean
	 * Removes a monster with from the room
	 * @param key
	 */
	public void removeMonster(String key) {
		Set<String> keys = walls.keySet();
		for (String wall : keys)
			if(walls.get(wall).hasMonster(key))
				walls.get(wall).setMonster(null);
	}

	/**
	 * Modification by Sean Byron
	 * Gets a monster from the room
	 * @param key
	 * @return
	 */
	public Monster getMonster(String key) {
		Set<String> keys = walls.keySet();
		for (String wall : keys)
			if(walls.get(wall).hasMonster(key))
				return walls.get(wall).getMonster();
		return null;
	}
	public HashMap<String, Monster> getMonsterList(){
		return monsters;
	}
	
	/**
	 * Addition by Sean Byron
	 * Sets the room as having been visited by the user
	 */
	public void visit() {
		visited = true;
	}
	
	/**
	 * Addition by Sean Byron
	 * @return true if the room has been visited
	 * @return false if the player hasn't visited the room yet
	 */
	public boolean hasBeenVisited() {
		return visited;
	}

}
