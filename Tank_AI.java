import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Tank_AI extends Tank {

//	private int x, y;
//	private int healthBarWidth = 48; // Length of the health bar
//	private int healthBarHeight = 5; // Height of the health bar
//	private double percentage = 1;
//	private Direction dir = Direction.STOP;
//	private Direction ptDir = Direction.D;
//
//	enum Direction {
//		L, U, R, D, STOP
//	};
//
	private boolean bL = false, bU = false, bR = false, bD = false;

	public Tank_AI(int x, int y) {
		super(x, y);
	}

	public Tank_AI(int x, int y, TankClient tc) {
		super(x, y, tc);
	}

	public void keyPressed(KeyEvent e)
	{
		
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_B)
			autoActive();
	}

//	public void draw(Graphics g) {
//		Color c = g.getColor();
//		// This draws the tank
//		g.drawImage(tankImage, x, y, null);
//		// This makes the outline of the healthbar
//		g.drawRect(x - 7, y - 8, healthBarWidth, healthBarHeight);
//		// draws remaining health
//		g.setColor(Color.GREEN);
//		g.fillRect(x - 7, y - 8, (int) (healthBarWidth * percentage), 5);
//		g.setColor(c);
//
//	}

	public void autoActive()
	{
		if (missileDestroyed)
		{
			int key = (int) (Math.random() * 5);
			switch (key) {
			case 0:
				changeDirection(Direction.L);
				bL = true;
				break;
			case 1:
				changeDirection(Direction.U);
				bU = true;
				break;
			case 2:
				changeDirection(Direction.R);
				bR = true;
				break;
			case 3:
				changeDirection(Direction.D);
				break;
			case 4:
				break;
			}
			move();
			switch (key) {
			case 0:
				changeDirection(Direction.STOP);
				bL = false;
				break;
			case 1:
				changeDirection(Direction.STOP);
				bU = false;
				break;
			case 2:
				changeDirection(Direction.STOP);
				bR = false;
				break;
			case 3:
				changeDirection(Direction.STOP);
				bD = false;
				break;
			case 4:
				fire();
				break;
			}
			if(getDirection() != Direction.STOP)
			{
				setPtDir(getDirection());
			}
			changeDirection(Direction.STOP);
		}
	}

	

	/*
	 * private void rotateImage(double degree) { ImageIcon imgIcon = new
	 * ImageIcon(temp); BufferedImage blankCanvas = new
	 * BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(),
	 * BufferedImage.TYPE_INT_ARGB); Graphics2D g2d = (Graphics2D)
	 * blankCanvas.getGraphics(); g2d.rotate(Math.toRadians(degree),
	 * imgIcon.getIconWidth() / 2, imgIcon.getIconHeight() / 2);
	 * g2d.drawImage(temp, 0, 0, null); tankImage = blankCanvas; }
	 */

}
