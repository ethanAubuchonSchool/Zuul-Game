import java.util.*;


public class RoomBuilder {
	private List<RoomInformation> rooms;
	private Game game;
	
	public RoomBuilder(Game game)
	{
		this.game = game;
		this.rooms = new ArrayList<RoomInformation>();
	}
	
	public void addRoom(RoomInformation r)
	{
		this.rooms.add(r);
	}
	
	public void removeRoom(RoomInformation r)
	{
		this.rooms.remove(r);
	}
	
	public void makeRoom()
	{
		for(int i = 0; i < rooms.size(); i++)
		{
			RoomViewPanel room = new RoomViewPanel(rooms.get(i).getName());
			
			for(int j = 0;j < rooms.get(i).getItems().size(); j++)
			{
				
				room.addItem(rooms.get(i).getItems().get(j));
			}
			
			game.getRooms().put(rooms.get(i).getName(), room);
			
		}
	}
}
