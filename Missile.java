import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static int WIDTH = 10;

	public static int HEIGHT = 10;
	public static int damage = 10;
	BufferedImage bimg = null;
	String img = "src/launch16now.png";
	String img1 = "src/nuclear.png";
	int x, y;
	Tank.Direction dir;
	Image missile = new ImageIcon(img).getImage();
	Image nuclear = new ImageIcon(img1).getImage();
	Image temp = new ImageIcon(img).getImage();
	private boolean live = true;
	
	private TankClient tc;
	
	public Missile(int x, int y, Tank.Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		try
		{
			bimg = ImageIO.read(new File(img));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
	}
	
	public void draw(Graphics g)
	{
//		Color c = g.getColor();
//		g.setColor(Color.BLACK);
//		g.fillOval(x, y, WIDTH, HEIGHT);
			//WIDTH = bimg.getWidth();
			//HEIGHT = bimg.getHeight();
//		}catch(IOException e) {
//		g.setColor(c);
		
		//g.drawImage(missile, x, y, null);
		
		//This changes the orientation of the missile
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
		
		// If an obstacle is hit an explosion is sent to the screen and and the missile is removed
		if(hitObstacle() || hitTank())
		{
			missile =nuclear;
			g.drawImage(missile, x, y-9, null);		
			live = false;
			tc.missiles.remove(this);		
			Tank.missileDestroyed = true;	
		}
		g.drawImage(missile, x, y-9, null);
		move();

	}
	
	//Returns true if missile hits an obstacle 
	private boolean hitObstacle()
	{
		ArrayList <Square> obstacleList =  tc.board.getObstacleList();
		if(obstacleList != null)
		{
			Rectangle rect = new Rectangle(x, y, Missile.WIDTH, Missile.HEIGHT);
			for(Square t : obstacleList)
			{
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), t.getWidth(), t.getHeight());
				if(rect.intersects(rectT))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//Returns true if missile hits tank and adds damage to that tank
	private boolean hitTank()
	{
		ArrayList <Tank> tankList =  tc.board.getTankList();
		if(tankList != null)
		{
			Rectangle rect = new Rectangle(x, y, Missile.WIDTH, Missile.HEIGHT);
			for(Tank t : tankList)
			{
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
				if(rect.intersects(rectT))
				{
					t.takeHit(damage);
					return true;
				}
			}
		}
		return false;
	}

	//Rotates missile image
	public void rotateImage(double degree)
	{
		ImageIcon imgIcon = new ImageIcon(temp);
		BufferedImage blankCanvas = new BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)blankCanvas.getGraphics();
		g2d.rotate(Math.toRadians(degree),imgIcon.getIconWidth()/2,imgIcon.getIconHeight()/2);
		g2d.drawImage(temp,0,0,null);
		missile = blankCanvas;
	}

	private void move()
	{
		switch(dir) {
		case L:
			x -= XSPEED; //Moves missile to the left
			break;
		case U:
			y -= YSPEED; //Moves missile to up
			break;
		case R:
			x += XSPEED; //Moves missile to the right
			break;
		case D:
			y += YSPEED; //Moves missile to down
			break;
		case STOP:
			break;
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT)
		{
			live = false;
			tc.missiles.remove(this); //removes missile once gone off screen
			Tank.missileDestroyed = true;
		}
	}

	public boolean isLive() {
		return live;
	}
	
}
