public class Player
{
	String name = null;
	long playerid = -1;
	Tank tank;

	public Player(String name, long pid)
	{
		this.name = name;
		playerid = pid;
		this.tank = null;
	}

	public long getPlayerID()
	{
		return playerid;
	}

	public String getName()
	{
		return name;
	}

	public void setTank(Tank t)
	{
		tank = t;
	}
	
	public Tank getTank()
	{
		return tank;
	}

	public void endTurn()
	{
		Command command = new Command("end");
		PlayerCommand playerCommand = new PlayerCommand(playerid,command);
	}
}