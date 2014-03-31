import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TankClient extends Frame
{
	public static int GAME_WIDTH = 680;
	public static int GAME_HEIGHT = 700;
//	private String img = "src/TankCombat.png";
//	private Image tankIcon = new ImageIcon(img).getImage();
	Tank myTank = new Tank(7, 30, this);
	Tank aiTank = new Tank_AI(7,30,this);
	List<Missile> missiles = new ArrayList<Missile>();
	ArrayList<Tank> tanks = new ArrayList<Tank>();
	BufferedImage bimg = null;
	Image offScreenImage = null;
	GameBoard board;
	
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		board.draw(g);
		for(int i=0; i<missiles.size(); i++)
		{
			Missile m = missiles.get(i);
			m.draw(g);
		}
		for(Tank t:tanks)
		{
			t.draw(g);
		}	
	}
	
	public void update(Graphics g)
	{
		if(offScreenImage == null)
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

	public void lauchFrame()
	{
		// GAMEBOARD SETUP
		board = new GameBoard(17,17, this);
		Tank.XSPEED = board.getImageWidth();
		Tank.YSPEED = board.getImageHeight();
		GAME_WIDTH = board.getGameWidth();
		GAME_HEIGHT = board.getGameHeight();
		tanks.add(aiTank);
		tanks.add(myTank);
		randomPosition();
		//this.setIconImage(tankIcon);
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
	}
	
	public ArrayList <Tank> getTanks()
	{
		return tanks;
	}
	
	//Places tanks on random part of field
	public void randomPosition()
	{
		Square[][] squareDbl = board.getSquareDblArray();
		int width = board.getImageWidth();
		int height = board.getImageHeight();
		int randomRow;
		int randomCol;

		for(Tank t: tanks)
		{
			randomRow = (int) (Math.random() * squareDbl.length);
			randomCol = (int) (Math.random() * squareDbl[0].length);
			if(squareDbl[randomRow][randomCol].getSquareType()!=Squares.OBSTACLE||!squareDbl[randomRow][randomCol].isUsed())
			{
				t.setPosition(randomCol*width+7,randomRow*height+30);
			}
		}
	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
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
			aiTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
			
		}
		
	}
}













