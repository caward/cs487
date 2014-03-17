import java.io.IOException;
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
			try {
				PlayerCommand command = (PlayerCommand)objectIn.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}