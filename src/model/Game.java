package model;

import java.util.*;

import javax.swing.JOptionPane;

import controller.FPMouseListener;

import model.command.Command;
import model.command.CommandStack;

import model.object.*;

import view.FirstPersonItem;
import view.FirstPersonMonster;
import view.FirstPersonRoom;
import view.FirstPersonView;
import view.MapRoom;
import view.MapView;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 */

public class Game extends Observable implements Observer
{
    private final static String PLAYER_DESCRIPTION = "Me";
    private final static int MAX_WEIGHT = 1000;
    private final static String DEFAULT_START_ROOM = "entrance";
    private final static int STARTING_HEALTH = 100;

	private Parser parser;
    private Player player1;
    private HashMap<String,Room> rooms;
    private CommandStack redoStack;
    private CommandStack undoStack;
    private FPMouseListener listener;
    //private String commandFrom;

    
    /**
     * Create the game and initialize its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        rooms = new HashMap<String,Room>();
        listener = new FPMouseListener();
        listener.addObserver(this);
        initializeGame();
        undoStack = new CommandStack();
        redoStack = new CommandStack();
        
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void initializeGame()
    {
        Room gallery,waitingroom, workshop, lobby, entrance, dinningroom,studio,theater, dressingroom,technician;
        
        // create the rooms
        rooms.put("gallary",gallery = new FirstPersonRoom("Gallery", listener));
        rooms.put("workshop",workshop = new FirstPersonRoom("Workshop", listener));
        rooms.put("lobby",lobby = new FirstPersonRoom("Lobby", listener));
        rooms.put("entrance",entrance = new FirstPersonRoom("Entrance", listener));
        rooms.put("dinning room",dinningroom = new FirstPersonRoom("Dinning Room", listener));
        rooms.put("studio",studio = new FirstPersonRoom("Studio", listener));
        rooms.put("theater",theater = new FirstPersonRoom("Theater", listener));
        rooms.put("dressing room",dressingroom = new FirstPersonRoom("Dressing Room", listener));
        rooms.put("technician room",technician = new FirstPersonRoom("Technician Room", listener));
        rooms.put("waiting room",waitingroom = new FirstPersonRoom("Waiting Room", listener));

        

        // Initialize room exits
        gallery.setExits("south",workshop);
        
        workshop.setExits("north",gallery);
        workshop.setExits("east",dressingroom);
        
        dressingroom.setExits("west",workshop);
        dressingroom.setExits("east", technician);
        
        technician.setExits("west",dressingroom);
        technician.setExits("north",studio);
        
        studio.setExits("south",technician);
        studio.setExits("west",theater);
        studio.setExits("north",dinningroom);
        
        dinningroom.setExits("south", studio);
        dinningroom.setExits("west", lobby);
        
        lobby.setExits("east",dinningroom);
        lobby.setExits("south",theater);
        lobby.setExits("west",waitingroom);
        lobby.setExits("north",entrance);
        
        waitingroom.setExits("east",lobby);
        
        theater.setExits("north",lobby);
        theater.setExits("east",studio);
        entrance.setExits("south",lobby);
        
        //create the items
        Item plant = new FirstPersonItem("Plant",2.0,"PogoStick.jpg");
        Item sword = new FirstPersonItem("Sword", 7.0,"excalibur-sword.jpg");
        Item pogoStick = new FirstPersonItem("PogoStix", 5.0, "rubber-plant.jpg");
        
        //Add Items
        entrance.addItem(plant, "north");
        workshop.addItem(sword, "south");
        dressingroom.addItem(pogoStick, "east");
        
        //Create monsters
        Monster kracken = new FirstPersonMonster("Kracken",10,"Kracken.jpg");
        Monster grendel = new FirstPersonMonster("Grendel", 8, "Grendel___old_by_nguy0699.jpg");
        Monster goblin = new FirstPersonMonster("Goblin",3, "troll.jpg");
        
        
        //Add Monsters to room
        entrance.addMonster(kracken, "west");
        workshop.addMonster(grendel, "north");
        dinningroom.addMonster(goblin, "east");
        
        String playerName = JOptionPane.showInputDialog("Please enter your name:");
        player1 = new Player(playerName,PLAYER_DESCRIPTION,MAX_WEIGHT,STARTING_HEALTH);
        
        rooms.get(DEFAULT_START_ROOM).visit();
        player1.setCurrentRoom(rooms.get(DEFAULT_START_ROOM));  // start game outside

    }

    /**
     *  Main play routine.  Loops until end of play.
     * @throws CloneNotSupportedException 
     */
    public void play()
    {            
        printWelcome();

        //Notify observers
        setChanged();
        notifyObservers(player1);
        
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            undoStack.add(command);
            //commandFrom = "player";
            finished = processCommand(command);
            if(gameOver()){
            	System.out.println("GAME OVER!YOU'RE DEAD!!!");
            	finished = true;
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo(player1);
    }

    private void printLocationInfo(Player player){
        System.out.println(player.getCurrentPlayerRoom().getLongDescription());
        System.out.println(player1.getPlayerName() + "'s stamina :" + player1.getStamina());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     * @throws CloneNotSupportedException 
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command==null || command.getCommandWord()==null) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        if(parser.isReversible(command.getCommandWord()))
        {
        	redoStack.empty();
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")){
            look();
        }
        else if (commandWord.equals("undo")){
            undo();
        }
        else if (commandWord.equals("redo")){
            redo();
        }
        else if (commandWord.equals("pick")){
            pick(command);
            //checkMonsterAttack();
        }
        else if (commandWord.equals("drop")){
            drop(command);
            //checkMonsterAttack();
        } 
        else if (commandWord.equals("attack")) {
        	attack(command);
        	//checkMonsterAttack();
        }        
        else if (commandWord.equals("heal")) {
        	heal(command);
        	//checkMonsterAttack();
        }
        
        //Notify observers
        setChanged();
        notifyObservers(player1);
        
        return wantToQuit;
    }

    private void undo(){
        Command temp = undoStack.pop();
        if(temp!=null)
        {
        	redoStack.add(temp);
        	//commandFrom = "undo";
        	processCommand(temp);
        	
        }
    }
    

    private void redo(){
    	Command temp = redoStack.pop();
    	if(temp!=null)
    	{
    		undoStack.add(temp);
    		//commandFrom = "player";
    		processCommand(temp);
    	}
    }
        
    /**
     * Attack a monster that is in the room
     * @param command
     */
    private void attack(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't who to attack
            System.out.println("Attack what?");
            return;
        }
        
        Room currentRoom = player1.getCurrentPlayerRoom();
        Monster monster = currentRoom.getMonster(command.getSecondWord());
        
        if (monster == null) {
            // There is no monster by that name in the room
            System.out.println("There is no monster called " + command.getSecondWord() + "!");
            return;
        }
        
        //Decrease the monster's health
        
        monster.decreaseHealth();
        
        if (!monster.isAlive()) {
        	//currentRoom.removeMonster(command.getSecondWord());
        	System.out.println("Good job! You've killed " + command.getSecondWord());
        	return;
        } else {
        	System.out.println(command.getSecondWord() + " health decreased to " + monster.getHealth());
        	//player1.pushLastMonsterAttacked(monster.getName());
        }

	}
    
    /**
     * Un-attack monster
     * @param command
     */
    private void heal(Command command) {
        Room currentRoom = player1.getCurrentPlayerRoom();
        Monster monster = currentRoom.getMonster(command.getSecondWord());
        
        if (monster == null) {
            // There is no monster by that name in the room
            System.out.println("There is no monster called " + command.getSecondWord() + "!");
            return;
        }
        monster.increaseHealth();
        //monster.increaseHealth();
    }

    
    private void drop(Command command){
    	Item item = player1.drop(command.getSecondWord());
    	if (item != null) {
    		System.out.println(item.getItemName() + " has been dropped by " + player1.getPlayerName());
    		//TODO: change "north" in expression bellow to current view direction
		    player1.getCurrentPlayerRoom().addItem(item, "north");
		    player1.printItemsAndWeight();
    	} else {
    		System.out.println("You cannot drop an item you're not carrying!");
    	}
    }

    private void look(){
        System.out.println(player1.getCurrentPlayerRoom().getLongDescription());
        System.out.println(player1.getPlayerName() + "'s stamina :" + player1.getStamina());
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic messagego and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.showCommands());
    }


    private void pick(Command command) {

   
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Pick what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player1.getCurrentPlayerRoom().getItem(itemName);

        // Try to pick up the item.
        
        if(player1.getCurrentPlayerRoom().containsItem(itemName)&&player1.pick(itemName,item)){
            System.out.println(item.getItemName() + " has been picked by " + player1.getPlayerName());
            player1.getCurrentPlayerRoom().removeItem(itemName);
            player1.printItemsAndWeight();
        }else{
            System.out.println("item could not be picked ");
        }
        System.out.println();//
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        Room nextRoom = player1.getCurrentPlayerRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            // Try to leave current room.
            //player1.setPreviousRoom(player1.getCurrentPlayerRoom());
            player1.setCurrentRoom(nextRoom);
            //monsterMove();
            printLocationInfo(player1);
            nextRoom.visit();
        }
        
        //Notify observers
        setChanged();
        notifyObservers(player1.getCurrentPlayerRoom());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    public static void main(String args[]) {
    	//Create a 3D First Person View
    	FirstPersonView view = new FirstPersonView("World of Zuul");
    	
    	Game game = new Game();
    	game.addObserver(view);
    	view.addObserver(game);
    	
    	
    	view.show();
		game.play();
    }

	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Command) {
			Command command = (Command)arg1;
			processCommand(command);
		}
	}
	/*
	public void monsterMove(){		
		for(Monster m : monsters.values()){
			while(true){
				String monsterExit = m.randomMove();
				//System.out.println(monsterExit);
				if(m.getCurrentRoom().getExitString().contains(monsterExit)){
			
					m.getCurrentRoom().removeMonster(m.getName());
					m.getCurrentRoom().getExit(monsterExit).addMonster(m);
					m.setCurrentRoom(m.getCurrentRoom().getExit(monsterExit));
					break;
				}
			}
		}
		
	}*/
	/*public void monsterAttack(){
		for(Monster m : player1.getCurrentPlayerRoom().getMonsterList().values()){
			player1.attacked(m.getName());	
		}
		player1.addStaminaLoss(player1.getCurrentPlayerRoom().getMonsterList().size());
		
	}
	public void monsterUnAttack(){
		player1.unAttacked();
	}
	public void checkMonsterAttack(){
		if(commandFrom.equals("player")){
			monsterAttack();
		}
		else if(commandFrom.equals("undo")){
			monsterUnAttack();
		}
	}*/
	
	public boolean gameOver(){
		if(player1.getStamina() < 1) return true;
		return false;
	}
}
