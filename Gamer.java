import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Gamer implements Runnable
{
	Gamer[] gamers;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectOutputStream objectOut;

	public Gamer(Gamer[] gamers, DataInputStream dis, DataOutputStream dos, ObjectOutputStream objectOut, int[][] map)
	{
		this.gamers = gamers;
		this.dis = dis;
		this.dos = dos;
		try {
			objectOut.writeObject(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{
		int one = -1;
		int two = -1;
		int three = -1;
		int four = -1;
		int five = -1;

		while(true)
		{
			try
			{
				one = dis.readInt();
				two = dis.readInt();
				three = dis.readInt();
				four = dis.readInt();
				five = dis.readInt();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0; i<gamers.length; i++)
			{
				if(gamers[i]!=null && !gamers[i].equals(this))
				{
					try
					{
						gamers[i].dos.writeInt(one);
						gamers[i].dos.writeInt(two);
						gamers[i].dos.writeInt(three);
						gamers[i].dos.writeInt(four);
						gamers[i].dos.writeInt(five);
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
				}
			}
		}
	}
}