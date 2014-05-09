import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;



public class NetClient {
	TankClient tc;
	private static int UDP_PORT_START = 2226 + (int)(Math.random() * (3001 - 2226)); //2228;
	
	private int udpPort;
	DataInputStream dis;
	DatagramSocket ds = null;
	private int[][] board;
	private Socket s;
	long future = 0;
	
	public NetClient(TankClient tc)
	{
		udpPort = UDP_PORT_START ++;
		this.tc = tc;
		try
		{
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}	
	}
	
	public void close()
	{
		ds.close();
	}
	
	public void connect(String IP, int port)
	{
		try {
			s = new Socket(IP, port);
			ObjectInputStream objectInputStr = new ObjectInputStream(s.getInputStream());
			board = (int[][])objectInputStr.readObject();
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
	}
	
	public long getTime()
	{
		return future;
	}
	
	public void setup()
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			future = dis.readLong();
			System.out.println("Connected to server. The ID gived by server is: " + id);
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} finally{
			if(s != null)
			{
				try
				{
					s.close();
					s = null;
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}		
		}
		
		TankNewMsg msg = new TankNewMsg(tc.myTank);
		send(msg);
		new Thread(new UDPRecvThread()).start();
	}
	
	public void send(Msg msg){
		msg.send(ds, "10.0.0.2", TankServer.UDP_PORT);
		//msg.send(ds, "127.0.0.1", TankServer.TCP_PORT);
	}
	
	private class UDPRecvThread implements Runnable{
		byte[] buf = new byte[1024];
		
		@Override
		public void run()
		{	
			
			while(ds != null)
			{
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try
				{
					ds.receive(dp);
					parse(dp);
					System.out.println("A packet received from server.");
//					try {
//						future = dis.readLong();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp)
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
			DataInputStream dis1 = new DataInputStream(bais);
			int msgType = 0;
			try
			{
				msgType = dis1.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg = null;
			switch (msgType)
			{
			case Msg.TANK_NEW_MSG:	
				msg = new TankNewMsg(NetClient.this.tc);
				msg.parse(dis1);
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis1);
				break;
			}		
		}
	}

	public int[][] getMap()
	{
		return board;
	}
}
