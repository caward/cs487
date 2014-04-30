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
		while(true)
		{
			for(int i=0; i<gamers.length; i++)
			{
				if(gamers[i]!=null)
				{
					try
					{
						dos.writeInt(dis.readInt());
						dos.writeInt(dis.readInt());
						dos.writeInt(dis.readInt());
						dos.writeInt(dis.readInt());
						dos.writeInt(dis.readInt());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
				}
			}
		}
	}
}