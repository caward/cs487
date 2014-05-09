public class Player
{
	String name = null;
	int playerid = -1;
	Tank tank;
	private int level;
	private int win;
	private int loss;
	private int resume;
	
	public Player(String name, int pid)
	{
		this.name = name;
		playerid = pid;
		this.tank = null;
		level = 1;
		win = 0;
		loss = 0;
		setResume(0);
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void increaseLoss()
	{
		loss++;
	}
	
	public void increaseWin()
	{
		win++;
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

	public int getResume()
	{
		return resume;
	}

	public void setResume(int resume)
	{
		this.resume = resume;
	}
}