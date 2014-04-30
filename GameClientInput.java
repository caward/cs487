import java.io.DataInputStream;
import java.io.IOException;


public class GameClientInput implements Runnable
{
	DataInputStream dis;
	TankClient tc;

	public GameClientInput(DataInputStream dis, TankClient tc)
	{
		this.dis = dis;
		this.tc = tc;
	}

	public void run()
	{
		int choice = -1;
		int id = -1;
		int level = -1;
		int x = -1;
		int y = -1;
		int dir = -1;
		int command = -1;
		
		while(true)
		{
			try {
				choice = dis.readInt();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		if(choice == 0)
    		{
      			try {
					id = dis.readInt();
					level = dis.readInt();
					x = dis.readInt();
					y = dis.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      			
      			if(tc.myTank.getID() != id)
      			{	
      				Tank ai = new Tank_AI(x,y,tc);
      				ai.setLevel(level);
      				tc.tanks.add(ai);
      			}
    		}else if(choice==1)
    		{
      			try {
					id = dis.readInt();
					dir = dis.readInt();
					command = dis.readInt();
					dis.readInt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      			if(tc.myTank.getID() != id)
      			{
      				for(Tank t:tc.tanks)
      				{
      					if(t.getID()==id)
      					{
      						t.changeDirection(Tank.Direction.values()[dir]);
      						switch(command)
      						{
      						case 1: 
      							t.move();
      							break;
      						case 2:
      							t.fire();
      							break;
      						case 3:
      							t.end();
      						}
      					}
      				}
      			}
    		}
		}
	}
}