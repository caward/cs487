public class Player
{
	String name = null;
	long playerid = -1;
	Tank tank;

	public Player(String name, long pid, Tank t)
	{
		this.name = name;
		playerid = pid;
		this.tank = tank;
	}

	public long getPlayerID()
	{
		return playerid;
	}

	public String getName()
	{
		return name;
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