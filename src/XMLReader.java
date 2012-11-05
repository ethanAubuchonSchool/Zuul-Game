import java.io.*;


public class XMLReader {
	/*
	 * Constants
	 */
	
	//XML tag ends
	private final static String START = "<";
	private final static String END = "</";
	private final static String CLOSE = ">";
	
	//XML tags components
	private final static String ROOM = "Room";
	private final static String MONSTER = "Monster";
	private final static String ITEM = "Item";
	private final static String EXIT = "Exit";
	private final static String NAME = "Name";
	
	//Filename
	private final static String FILE_NAME = "rooms.xml";
	
	/*
	 * Instance Variables
	 */
	
	//Input Data
	private FileInputStream fstream;
	private BufferedReader br;
	
	//Room builder
	private RoomBuilder builder;
	
	/*
	 * Methods
	 */
	
	/*
	 * Constructor for XMLReader Class
	 * Initialises buffer and input stream
	 */
	public XMLReader()
	{
		initReader();
	}
	
	/*
	 * Inialises buffer and input stream
	 */
	public void initReader()
	{
		try{
			fstream = new FileInputStream(FILE_NAME);
			br = new BufferedReader(new InputStreamReader(fstream));
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * Reads a line of XML from file and passes it to the decoder
	 */
	public void readXML()
	{
		String s;
		while(true)
		{
			try{
				s = br.readLine();
				if(s == null)
				{
					break;
				}
				interpretXML(s);
			}catch(Exception e){
				System.out.println(e);
			}
		}
	}
	
	/*
	 * Interprets the XML data and stores it in the proper location
	 */
	public void interpretXML(String s)
	{
		//Starts new room info collection
		if(s.equals(START+ROOM+CLOSE))
		{
			RoomInformation room = new RoomInformation();
			while(!s.equals(END+ROOM+CLOSE))
			{
				if(s.equals(START+NAME+CLOSE))
				{
					String temp = new String();
					while(!s.equals(END+ROOM+CLOSE))
					{
						temp += s;
					}
					room.setName(temp);
				}
				else if(s.equals(START+ITEM+CLOSE))
				{
					while(!s.equals(END+ITEM+CLOSE))
					{
						room.getItems().add(s);
					}
				}
				else if(s.equals(START+MONSTER+CLOSE))
				{
					while(!s.equals(END+MONSTER+CLOSE))
					{
						room.getMonsters().add(s);
					}
				}
				else if(s.equals(START+EXIT+CLOSE))
				{
					while(!s.equals(END+EXIT+CLOSE))
					{
						room.getExits().add(s);
					}
				}
			}
			builder.addRoom(room);
		}
	}
}
