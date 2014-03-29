import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class TankClient extends Frame {
	public static int GAME_WIDTH = 680;
	public static int GAME_HEIGHT = 700;
		
	Tank myTank = new Tank(7, 30, this);
	Tank aiTank = new Tank_AI(7,30,this);
	List<Missile> missiles = new ArrayList<Missile>();
	BufferedImage bimg = null;
	Image offScreenImage = null;
	String imge = "src/Grass1_opt.jpg";
	String imge1 = "src/green_hill_icon_opt.png";
	Image img;
	Image grass = new ImageIcon(imge).getImage();
	Image hill = new ImageIcon(imge1).getImage();
	GameBoard board;
	
	public void paint(Graphics g)
	{
//		try
//		{
//			bimg = ImageIO.read(new File(imge));
//			Tank.XSPEED = bimg.getWidth();
//			Tank.YSPEED = bimg.getHeight();
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
		
		//Color c = g.getColor();
		g.setColor(Color.WHITE);
//		for(int i = 0; i < 17; i ++)
//		{
//			for(int j = 0; j<17; j++)
//			{
//				img = ((i%2==0&&j%2==0) ? grass:hill);
//				g.drawImage(img, i * bimg.getWidth(), 22+j*bimg.getHeight(), null);
//			}
//		}
		board.draw(g);
		//g.setColor(c);
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.draw(g);
		}
		myTank.draw(g);
		aiTank.draw(g);
		
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
		board = new GameBoard(17,17);
		Tank.XSPEED = board.getImageWidth();
		Tank.YSPEED = board.getImageHeight();
		GAME_WIDTH = board.getGameWidth();
		GAME_HEIGHT = board.getGameHeight();
		
//		try
//		{
//			bimg = ImageIO.read(new File(imge));
//			Tank.XSPEED = bimg.getWidth();
//			Tank.YSPEED = bimg.getHeight();
//			GAME_WIDTH = 17*Tank.XSPEED;
//			GAME_HEIGHT = 17*Tank.YSPEED+22;
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
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













