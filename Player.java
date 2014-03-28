public class Player
{
	String name = null;
	int playerid = -1;
	Tank tank;

	public Player(String name, int pid)
	{
		this.name = name;
		playerid = pid;
		this.tank = null;
	}

	public int getPlayerID()
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