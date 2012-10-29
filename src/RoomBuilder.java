import java.util.*;


public class RoomBuilder {
	private List<RoomInformation> rooms;
	
	public RoomBuilder()
	{
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
}
