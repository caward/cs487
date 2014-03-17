import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameClient
{

  public static void main(String args[]) throws Exception
  {
    Socket socket1;
    int portNumber = 7777;
    String str = "";

    socket1 = new Socket(InetAddress.getLocalHost(), portNumber);

    ObjectInputStream objectInputStr = new ObjectInputStream(socket1.getInputStream());
    ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());
    GameClientInput gcInput = new GameClientInput(objectInputStr);
    Thread thread = new Thread(gcInput);
    thread.start();


  }

}