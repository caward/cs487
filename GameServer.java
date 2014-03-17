import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket


public class GameServer
{

  final MAX_GAMERS = 8;
  public static void main(String args[]) throws Exception
  {
    ServerSocket servSocket;
    Socket socket;
    int port = 7777;
    Gamer[] gamers = new Gamer[MAX_GAMERS];
    

    servSocket = new ServerSocket(port);
    while(true)
    {
      for(int i=0; i<MAX_GAMERS; i++)
      {
        System.out.println("Waiting for a connection on " + port);

        socket = servSocket.accept();
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        if(gamers[i]==null)
        {
          gamers[i] = new Gamer(gamers,ois,oos)
          Thread.thread = newThread(user[i]);
          thread.start();
          break;  
        }
      }
    }
  }
}