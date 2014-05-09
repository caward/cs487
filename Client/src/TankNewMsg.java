import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class TankNewMsg implements Msg{
	int msgType = Msg.TANK_NEW_MSG;
	
	Tank tank;
	TankClient tc;
	enum Direction {L, U, R, D, STOP};
	public TankNewMsg(Tank tank){
		this.tank = tank;
	}
	
	public TankNewMsg(TankClient tc){
		this.tc = tc;
	}
	
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
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

	public void parse(DataInputStream dis)
	{
		try {
			int id = dis.readInt();
			if(tc.myTank.id == id){
				return;
			}
			
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			//System.out.println("id: " + id + " -x: " + x + " -y: " + y + " -dir: " + dir);
			boolean exist = false;
			for(int i = 0; i < tc.tanks.size(); i ++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					exist = true;
					break;
				}
				
			}
			if(!exist)
			{	
				TankNewMsg tnMsg = new TankNewMsg(tc.myTank);
				tc.nc.send(tnMsg);
				
				Tank t = new Tank_AI(x, y, tc);
				t.id = id;
				tc.tanks.add(t);
			}
//			Tank t = new Tank_AI(x, y,tc);
//			t.id = id;
//			tc.tanks.add(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
