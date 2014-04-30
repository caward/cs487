import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient
{

  DataInputStream dataInputStr;
  DataOutputStream dos;
  GameBoard board;

  public void connect(TankClient tc)
  {
    Socket socket1;
    int portNumber = 7777;

    try {
		socket1 = new Socket(InetAddress.getLocalHost(), portNumber);
		ObjectInputStream objectInputStr = new ObjectInputStream(socket1.getInputStream());
		board = (GameBoard)objectInputStr.readObject();
		dataInputStr = new DataInputStream(socket1.getInputStream());
		dos = new DataOutputStream(socket1.getOutputStream());
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    GameClientInput gcInput = new GameClientInput(dataInputStr,tc);
    Thread thread = new Thread(gcInput);
    thread.start();
  }

  public GameBoard getBoard()
  {
    return board;
  }

  public void sendTank(Tank tank)
  {
    try
    {
    	dos.writeInt(0);
		dos.writeInt(tank.getID());
		dos.writeInt(tank.getLevel());
		dos.writeInt(tank.x);
		dos.writeInt(tank.y);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
  }

  public void sendInfo(int id, Tank.Direction dir, int command)
  {
    try
    {
		dos.writeInt(1);
		dos.writeInt(id);
		dos.writeInt(dir.ordinal());
		dos.writeInt(command);
		dos.writeInt(5);
	} catch (IOException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
  }
}