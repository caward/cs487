public class Player
{
	String name = null;
	int playerid = -1;
	Tank tank;
	private int level;
	private int win;
	private int loss;
	
	public Player(String name, int pid)
	{
		this.name = name;
		playerid = pid;
		this.tank = null;
		level = 1;
		win = 0;
		loss = 0;
	}
	
	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getWin()
	{
		return win;
	}

	public void setWin(int win)
	{
		this.win = win;
	}

	public int getLoss()
	{
		return loss;
	}

	public void setLoss(int loss)
	{
		this.loss = loss;
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