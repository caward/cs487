import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;

public class Gamer implements Runnable
{
	Gamer[] gamers;
	ObjectInputStream objectIn;
	ObjectOutputStream objectOut;

	public Gamer(Gamer[] gamers, ObjectInputStream objectIn, ObjectOutputStream objectOut)
	{
		this.gamers = gamers;
		this.objectIn = objectIn;
		this.objectOut = objectOut;
	}

	public void run()
	{
		while(true)
		{
			for(int i=0; i<gamers.length; i++)
			{
				
			}
		}
	}
}