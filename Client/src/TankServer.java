import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class TankServer
{	
	private static int ID = 100;
	public static final int TCP_PORT = 7777;
	public static final int UDP_PORT = 6666;
	List<Client> clients = new ArrayList<Client>();
	private ObjectOutputStream objectOut;
	static int intervalMillisecs = 1261000;
	long futureTime = 0;
	int[][] map = new int[15][15];
	
	
	public void start()
	{
		GameBoard board = new GameBoard(15,15);
		File file = new File("SaveFile.txt");

		if (file.exists())
		{
			Scanner scan = null;
			try {
				scan = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Read each row.
			for (int i = 0; i < 15; i++)
			{
				for (int j = 0; j < 15; j++)
				{
					// For each number in the column, read a number
					// and put it in the array
					map[i][j] = scan.nextInt();
				}
				// advance the scanner to the next line (row).
				scan.nextLine();
			}
		}else{map = board.getMap();}
		new Thread(new UDPThread()).start();	
		ServerSocket ss = null;
		
		try
		{
			ss = new ServerSocket(TCP_PORT);	
		} catch (IOException e) {
			e.printStackTrace();
		}
		futureTime = System.currentTimeMillis()+intervalMillisecs;
		while(true)
		{
			Socket s = null;
			try
			{
				s = ss.accept();
				objectOut = new ObjectOutputStream(s.getOutputStream());
				objectOut.flush();
				objectOut.writeObject(map);
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String IP = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID ++);
				dos.writeLong(futureTime);
				Client c = new Client(IP, udpPort);
				clients.add(c);
				System.out.println("A Client Connect! Address- " + s.getInetAddress() + ":" + s.getPort() + "----UDP Port: " + udpPort);
			} catch(IOException e){
				e.printStackTrace();
			} finally {
				if (s != null){
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args)
	{	
		new TankServer().start();
	}

	private class Client
	{
		String IP;
		int udpPort;
		DataOutputStream dos;
		
		public Client(String IP, int udpPort)
		{
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}
	
	private class UDPThread implements Runnable
	{	
		byte[] buf = new byte[1024];
		
		@Override
		public void run()
		{
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("UDP thread started at port: " + UDP_PORT);
			while(ds != null)
			{
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try
				{
					ds.receive(dp);
					for(int i = 0; i < clients.size(); i ++)
					{
						Client c = clients.get(i);	
						dp.setSocketAddress(new InetSocketAddress(c.IP, c.udpPort));
						ds.send(dp);
					}
					System.out.println("A packet received.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}	
}
