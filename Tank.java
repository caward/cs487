
public class Tank
{
	Player player;
	int numMissile;
	double hp;
	int movePoint;

	public Tank(Player player,int numMissile, double hp, int mp)
	{
		this.player = player;
		this.numMissile = numMissile;
		this.hp = hp;
		movePoint = mp;
	}

	public PlayerCommand scan()
	{
		Command command = new Command("scan");
		PlayerCommand playerCommand = new PlayerCommand(player.getPlayerID,command);
		return playerCommand;
	}

	public PlayerCommand observe()
	{
		Command command = new Command("observe");
		PlayerCommand playerCommand = new PlayerCommand(player.getPlayerID,command);
		return playerCommand;
	}

	public PlayerCommand turn(String direction)
	{
		Command command = new Command(direction);
		PlayerCommand playerCommand = new PlayerCommand(player.getPlayerID,command);
		return playerCommand;
	}

	public PlayerCommand fire()
	{
		Command command = new Command("fire");
		PlayerCommand playerCommand = new PlayerCommand(player.getPlayerID,command);
		return playerCommand;
	}

	public PlayerCommand move(String direction)
	{
		Command command = new Command(direction);
		PlayerCommand playerCommand = new PlayerCommand(player.getPlayerID,command);
		return playerCommand;
	}

}