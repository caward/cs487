import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class TankClient extends Frame {
	public static int GAME_WIDTH = 680;
	public static int GAME_HEIGHT = 700;
	// private String img = "src/TankCombat.png";
	// private Image tankIcon = new ImageIcon(img).getImage();
	Tank myTank = new Tank(7, 30, this);
	List<Missile> missiles = new ArrayList<Missile>();
	ArrayList<Tank> tanks = new ArrayList<Tank>();
	BufferedImage bimg = null;
	Image offScreenImage = null;
	GameBoard board;
	Player p;
	DBConnection dbc = null;
	boolean isNewState = true;

	NetClient nc = new NetClient(this);
	private boolean sorted = false;
	public int playerTurnID = 100;
	private long futureTime;
	private long secondTime;
	boolean timeGiven = true;
	boolean won = true;
	
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		board.draw(g);
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.draw(g);
		}
		for (Tank t : tanks) {
			t.draw(g);
		}
		if(!tanks.contains(myTank))
		{
			System.exit(0);
		}
		
		//Draws timer
		//g.drawString(countDown, 50, 50);
		futureTime = nc.getTime();
		secondTime = (futureTime-System.currentTimeMillis())/1000;
		g.drawString(secondTime/60+":"+secondTime%60, 50, 50);//Draw GameClock
		if(tanks.size()==1 && !timeGiven)
		{
			g.drawString("YOU WIN", myTank.x, myTank.y+8);
			if(won==true)
			{
				playerTurnID= -1;//stop turns
				Player playerWon;
				playerWon = myTank.getPlayer();
				playerWon.increaseWin();
//				dbc.win(playerWon.getWin(), playerWon.getPlayerID());
//				dbc.close();
				won = false;
			}
		}
		
		if((secondTime/60) <= 20 && (secondTime%60)<=0 && timeGiven && myTank.getID() == 100)
		{
			myTank.startClock();
			timeGiven = false;
		}
		if((secondTime/60) <= 0 && (secondTime%60)<=0)
		{
			

			//implement game ending scenario
			playerTurnID= -1;//stop turns
			tabulate(g);
			// TankMoveMsg msg = new TankMoveMsg(myTank.id, myTank.dir, myTank.Gameend);
			// tc.nc.send(msg);
		}
	}

	public void tabulate(Graphics g)
	{
		ArrayList<Tank> winner = new ArrayList<Tank>();
		sortTanks();
		winner.add(tanks.get(0));
		for(Tank t:tanks)
		{
			if(!winner.contains(t))
			{
				if(t.getMP()==winner.get(1).getMP())
					winner.add(t);
			}
		}
		if(winner.size()==1)
		{
			if(winner.get(1).equals(myTank))
			{
				g.drawString("YOU WIN", myTank.x, myTank.y+8);
				Player playerWon;
				playerWon = myTank.getPlayer();
				playerWon.increaseWin();
//				dbc.win(playerWon.getWin(), playerWon.getPlayerID());
//				dbc.close();
			}
			else
			{
				g.drawString("YOU Lose", myTank.x, myTank.y+8);
				Player playerLost;
				playerLost = myTank.getPlayer();
				playerLost.increaseLoss();
//				dbc.win(playerLost.getLoss(), playerLost.getPlayerID());
//				dbc.close();
			}
		}else
		{
			ArrayList<Tank> healthWinner = new ArrayList<Tank>();
			//sort by health
			sortTanksHealth(winner);
			if(healthWinner.size()==1)
			{
				if(healthWinner.get(1).equals(myTank))
				{
					g.drawString("YOU WIN", myTank.x, myTank.y+8);
					Player playerWon;
					playerWon = myTank.getPlayer();
					playerWon.increaseWin();
//					dbc.win(playerWon.getWin(), playerWon.getPlayerID());
//					dbc.close();
				}else
				{
					g.drawString("YOU Lose", myTank.x, myTank.y+8);
					Player playerLost;
					playerLost = myTank.getPlayer();
					playerLost.increaseLoss();
//					dbc.win(playerLost.getLoss(), playerLost.getPlayerID());
//					dbc.close();
				}
			}else
			{
				if(healthWinner.contains(myTank))
				{
					g.drawString("Tie Game", myTank.x, myTank.y+8);
				}else
				{
					g.drawString("YOU Lose", myTank.x, myTank.y+8);
					Player playerLost;
					playerLost = myTank.getPlayer();
					playerLost.increaseLoss();
//					dbc.win(playerLost.getLoss(), playerLost.getPlayerID());
//					dbc.close();
				}
			}
		}
	}

	public void update(Graphics g)
	{
		if (offScreenImage == null)
		{
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.LIGHT_GRAY);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void setSorted(boolean sorted)
	{
		this.sorted = sorted;
	}
	
	public boolean isSorted()
	{
		return sorted;
	}


	public void sortTanksHealth(ArrayList<Tank> tank)
	{
		Tank[] array;
		//Cover ArrayList to array 
		array = tank.toArray(new Tank[tank.size()]);
		//Sort array
		for (int i=0; i<array.length-1; i++)
		{
	        for (int j=i+1; j<array.length; j++)
	        {
	            if (array[i].getHealth() < array[j].getHealth())
	            {
	                Tank temp = array[i];
	                array[i] = array[j];
	                array[j] = temp;
	            }
	        }
	    }
		//Convert array to ArrayList 
		tank = new ArrayList<Tank>(Arrays.asList(array));
		setSorted(true);
	}

	
	public void sortTanks()
	{
		Tank[] array;
		//Cover ArrayList to array 
		array = tanks.toArray(new Tank[tanks.size()]);
		//Sort array
		for (int i=0; i<array.length-1; i++)
		{
	        for (int j=i+1; j<array.length; j++)
	        {
	            if (array[i].getMP() < array[j].getMP())
	            {
	                Tank temp = array[i];
	                array[i] = array[j];
	                array[j] = temp;
	            }
	        }
	    }
		//Convert array to ArrayList 
		tanks = new ArrayList<Tank>(Arrays.asList(array));
		setSorted(true);
	}

	public void lauchFrame()
	{
		// dbc = new DBConnection();
		// p = dbc.login();
		// p.setTank(myTank);
		// myTank.setPlayer(p);
		
		// GAMEBOARD SETUP
		
		nc.connect("10.0.0.2", TankServer.TCP_PORT);
		board = new GameBoard(15, 15, this);
		File file = new File("SaveFile.txt");
		if (file.exists())
		{
			isNewState = false;
		}
		board.setMap(nc.getMap()); 
		Tank.XSPEED = board.getImageWidth();
		Tank.YSPEED = board.getImageHeight();
		GAME_WIDTH = board.getGameWidth();
		GAME_HEIGHT = board.getGameHeight();
		tanks.add(myTank);
		if(isNewState)
		{
			randomPosition();
		}
		
		// this.setIconImage(tankIcon);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);

		this.addKeyListener(new KeyMonitor());

		setVisible(true);

		new Thread(new PaintThread()).start();
		
	//	nc.connect("127.0.0.1", TankServer.TCP_PORT);
		//nc.connect("10.0.0.2", TankServer.TCP_PORT);
		nc.setup();
	}

	public ArrayList<Tank> getTanks() {
		return tanks;
	}

	// Places tanks on random part of field
	public void randomPosition()
	{
		Square[][] squareDbl = board.getSquareDblArray();
		int width = board.getImageWidth();
		int height = board.getImageHeight();
		int randomRow;
		int randomCol;

		for (Tank t : tanks)
		{
			randomRow = (int) (Math.random() * board.getRow());
			randomCol = (int) (Math.random() * board.getCol());
			if (squareDbl[randomRow][randomCol].getSquareType() != Squares.OBSTACLE
					&& !squareDbl[randomRow][randomCol].isUsed()) {
				t.setPosition(randomRow * width + 7, randomCol * height + 30);
			}
		}
	} 

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}

	private class PaintThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
			//aiTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
	}
}
