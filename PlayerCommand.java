import java.io.Serializable;


//Command that the player does and the id of the player
public class PlayerCommand implements Serializable
{
	long playerid =-1;
	Command command;

	public PlayerCommand(long playerid, Command c)
	{
		this.playerid = playerid;
		command = c;
	}

	public long getPlayerID()
	{
		return playerid;
	}

	public Command getCommand()
	{
		return command;
	}
}