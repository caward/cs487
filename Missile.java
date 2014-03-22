import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	String img = "src/launch16now.png";
	int x, y;
	Tank.Direction dir;
	Image missile = new ImageIcon(img).getImage();
	Image temp = new ImageIcon(img).getImage();
	private boolean live = true;
	
	private TankClient tc;
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
//		Color c = g.getColor();
//		g.setColor(Color.BLACK);
//		g.fillOval(x, y, WIDTH, HEIGHT);
//		g.setColor(c);
		
		//g.drawImage(missile, x, y, null);
		switch(dir)
		{
		case L:
			rotateImage(90);
			break;
		case U:
			rotateImage(180);
			break;
		case R:
			rotateImage(270);
			break;
		case D:
			rotateImage(0);
			break;
		}
		g.drawImage(missile, x, y-9, null);
		move();
	}
	
	public void rotateImage(double degree)
	{
		ImageIcon imgIcon = new ImageIcon(temp);
		BufferedImage blankCanvas = new BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)blankCanvas.getGraphics();
		g2d.rotate(Math.toRadians(degree),imgIcon.getIconWidth()/2,imgIcon.getIconHeight()/2);
		g2d.drawImage(temp,0,0,null);
		missile = blankCanvas;
	}

	private void move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
			tc.missiles.remove(this);
			Tank.missileDestroyed = true;
		}
	}

	public boolean isLive() {
		return live;
	}
	
	
}
