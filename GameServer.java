import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class GameServer
{

  final static int MAX_GAMERS = 8;
  public static void main(String args[]) throws Exception
  {
    ServerSocket servSocket;
    Socket socket;
    int port = 7777;
    Gamer[] gamers = new Gamer[MAX_GAMERS]; 
    GameBoard board = new GameBoard(15,15);
    ObjectOutputStream oos;
    DataInputStream dis;
    DataOutputStream dos;
    

    servSocket = new ServerSocket(port,MAX_GAMERS);
//    while(true)
//    {
      for(int i=0; i<MAX_GAMERS; i++)
      {
        //if not done do this
        System.out.println("Waiting for a connection on " + port);

        socket = servSocket.accept();

        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        dos.flush();

        if(gamers[i]==null)
        {
          gamers[i] = new Gamer(gamers,dis,dos,oos,board.getMap());
          Thread thread = new Thread(gamers[i]);
          thread.start();
          break;  
        }
      }
//    }
  }
}