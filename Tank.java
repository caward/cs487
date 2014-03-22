
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Tank {
	public static int XSPEED = 40;
	public static int YSPEED = 40;
	
	public static int WIDTH= 20;
	public static int HEIGHT= 20;
	BufferedImage bimg = null;
	TankClient tc;
	Point p;
	Image tankImage;
	Image temp;
	String img = "src/abrams_m1_battle_tank32.png";
				
	private int x, y;
	public static boolean missileDestroyed = true;
	private boolean bL=false, bU=false, bR=false, bD = false;
	enum Direction {L, U, R, D, STOP};
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;

	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
		p = new Point(x,y);
		tankImage = new ImageIcon(img).getImage();
		temp = new ImageIcon(img).getImage();
		try
		{
			bimg = ImageIO.read(new File(img));
			WIDTH = bimg.getWidth();
			HEIGHT = bimg.getHeight();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Tank(int x, int y, TankClient tc)
	{
		this(x, y);
		this.tc = tc;
		p = new Point(x,y);
		tankImage = new ImageIcon(img).getImage();
		temp = new ImageIcon(img).getImage();
		try
		{
			bimg = ImageIO.read(new File(img));
			WIDTH = bimg.getWidth();
			HEIGHT = bimg.getHeight();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void draw(Graphics g)
	{
		//Color c = g.getColor();
		//g.setColor(Color.RED);
		g.drawImage(tankImage,x,y,null);
		
		//g.fillRect(x, y, WIDTH, HEIGHT);
		//g.setColor(c);
		
//		switch(ptDir)
//		{
//		case L:
//			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x - 4, y + Tank.HEIGHT/2);
//			break;
//		case U:
//			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y - 4);
//			break;
//		case R:
//			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH + 4, y + Tank.HEIGHT/2);
//			break;
//		case D:
//			g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT + 4);
//			break;
//		}
		
		move();
	}
	
	void move()
	{
		switch(dir) {
		case L:
			x -= XSPEED;
			rotateImage(90);
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			break;
		case U:
			y -= YSPEED;
			rotateImage(180);
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			break;
		case R:
			x += XSPEED;
			rotateImage(270);
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			break;
		case D:
			y += YSPEED;
			rotateImage(0);
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		break;
		case STOP:
			break;
		}
		
		if(this.dir != Direction.STOP)
		{
			this.ptDir = this.dir;
		}
		
		if(x < 0)
		{
			x = 6;//10;
			setPosition(x,y);
		}
		if(y < 30)
		{
			y = 30;//30
			setPosition(x,y);
		}

		if(x + Tank.WIDTH > TankClient.GAME_WIDTH)
		{
			//x = TankClient.GAME_WIDTH - Tank.WIDTH - 10;
			x=TankClient.GAME_WIDTH-42;//630;
			setPosition(x,y);
		}
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
		{
			//y = TankClient.GAME_HEIGHT - Tank.HEIGHT - 10;
			y=TankClient.GAME_HEIGHT-40;//654;
			setPosition(x,y);
		}
		
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	
	void locateDirection()
	{
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}

	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_CONTROL:
			if(missileDestroyed)
				fire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		case KeyEvent.VK_D :
			System.out.println(TankClient.GAME_WIDTH+" "+TankClient.GAME_HEIGHT+"     774 798");
			break;
		}
		locateDirection();		
	}
	
	public Missile fire()
	{
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 -9;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir, this.tc);
		tc.missiles.add(m);
		missileDestroyed = false;
		return m;
	}
	
	private void rotateImage(double degree)
	{
		ImageIcon imgIcon = new ImageIcon(temp);
		BufferedImage blankCanvas = new BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)blankCanvas.getGraphics();
		g2d.rotate(Math.toRadians(degree),imgIcon.getIconWidth()/2,imgIcon.getIconHeight()/2);
		g2d.drawImage(temp,0,0,null);
		tankImage = blankCanvas;
	}

	public void setPosition(int px, int py)
	{
		p.setLocation(px, py);
	}
	
	public Point getPosition()
	{
		return p;
	}

	public void setVisibility(int row, int col) {
		// TODO Auto-generated method stub
		
	}
}
