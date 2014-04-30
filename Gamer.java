import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Gamer implements Runnable
{
	Gamer[] gamers;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectOutputStream objectOut;

	public Gamer(Gamer[] gamers, DataInputStream dis, DataOutputStream dos, ObjectOutputStream objectOut, GameBoard board)
	{
		this.gamers = gamers;
		this.dis = dis;
		this.dos = dos;
		try {
			objectOut.writeObject(board);
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