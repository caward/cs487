import java.io.ObjectInputStream;


public class GameClientInput implements Runnable
{
	ObjectInputStream objectIn;

	public GameClientInput(ObjectInputStream objectIn)
	{
		this.objectIn = objectIn;
	}

	public void run()
	{
		while(true)
		{
			PlayerCommand command =(PlayerCommand)objectIn.readObject();
		}
	}
}