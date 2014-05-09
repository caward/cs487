import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class TankMoveMsg implements Msg
{
	int msgType = Msg.TANK_MOVE_MSG;
	int id;
	Tank.Direction dir;
	TankClient tc;
	int command;
	
	public TankMoveMsg(int id, Tank.Direction dir, int command)
	{	
		this.id = id;
		this.dir = dir;
		this.command = command;
	}
	public TankMoveMsg(TankClient tc)
	{
		this.tc = tc;
	}
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try
		{
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
			dos.writeInt(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	@Override
	public void parse(DataInputStream dis)
	{		
		int c=-1;// TODO Auto-generated method stub
		try
		{
			int id = dis.readInt();
			if(tc.myTank.id == id)
			{
				return;
			}
			
			Tank.Direction dir = Tank.Direction.values()[dis.readInt()];
			c = dis.readInt();
			//System.out.println("id: " + id + " -x: " + x + " -y: " + y + " -dir: " + dir);
			boolean exist = false;
			for(int i = 0; i < tc.tanks.size(); i ++)
			{
				Tank t = tc.tanks.get(i);
				if(t.id == id)
				{
					t.dir = dir;
					switch(c)
      				{
      					case 1: 
      						t.move();
      						break;
      					case 2:
      						t.fire();
      						break;
   						case 3:
   							t.end();
   							break;
   						case 4:
   							if(t.getType().equals("Player"))
   							{
   								t.save();
   							}else
   							{
   								t.setResume(1);
   							}
   							break;
   						case 5:
   							t.rotateImage(90);
   							break;
   						case 6:
   							t.rotateImage(270);
   							break;
   						case 7:
   							t.rotateImage(180);
   							break;
   						case 8:
   							t.rotateImage(0);
   							break;
   						case 9:
   						
   					}   
					exist = true;
					break;
				}
			}
		}catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
