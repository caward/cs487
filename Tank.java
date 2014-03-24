
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
	int healthBarWidth = 48;
	int healthBarHeight = 5;
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
		Color c = g.getColor();
		// This draws the tank
		g.drawImage(tankImage,x,y,null);
		// This makes the outline of the healthbar 
		g.drawRect(x-7, y-8, healthBarWidth, healthBarHeight);
		//draws remaining health
		g.setColor(Color.GREEN);
		g.fillRect(x-7, y-8, 20, 5);
		g.setColor(c);
		
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
		if(isClear(x,y,dir))
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
// MIGHT NOT NEED COMMENTED OUT SECTION BELOW
			// Edge Cases to make sure that tank does not go off screen
//			if(x < 0)
//			{
//				x = 7;//10;
//				setPosition(x,y);
//			}
//			if(y < 30)
//			{
//				y = 30;//30
//				setPosition(x,y);
//			}
//
//			if(x + Tank.WIDTH > TankClient.GAME_WIDTH)
//			{
//				//x = TankClient.GAME_WIDTH - Tank.WIDTH - 10;
//				x=TankClient.GAME_WIDTH-42;//630;
//				setPosition(x,y);
//			}
//			if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT)
//			{
//				//y = TankClient.GAME_HEIGHT - Tank.HEIGHT - 10;
//				y=TankClient.GAME_HEIGHT-40;//654;
//				setPosition(x,y);
//			}
		}
		
	}
	
	private boolean isClear(int x, int y, Direction direction)
	{
		int tmpx,tmpy;
		tmpx=x;
		tmpy=y;
		switch(direction) {
		case L:
			tmpx -= XSPEED;
			break;
		case U:
			tmpy -= YSPEED;
			break;
		case R:
			tmpx += XSPEED;
			break;
		case D:
			tmpy += YSPEED;
			break;
		case STOP:
			break;
		}
		if(tmpx<0||tmpy<0||tmpx>TankClient.GAME_WIDTH||tmpy>TankClient.GAME_HEIGHT) return false;
		//Rectangle rect = new Rectangle(tmpx, tmpy, WIDTH, HEIGHT);
		// TODO NEED TO ITERATE THROUGH ARRAY OF TANKS AND OBSTACLEs to see if rectangles intersect
		return true;
	}

	public void keyPressed(KeyEvent e)
	{
		if(missileDestroyed)
		{
			int key = e.getKeyCode();
			switch(key) {
			case KeyEvent.VK_LEFT :
				dir = Direction.L;
				bL = true;
				break;
			case KeyEvent.VK_UP :
				dir = Direction.U;
				bU = true;
				break;
			case KeyEvent.VK_RIGHT :
				dir = Direction.R;
				bR = true;
				break;
			case KeyEvent.VK_DOWN :
				dir = Direction.D;
				break;
			}
			//locateDirection();
		}
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
		if(missileDestroyed)
		{
			int key = e.getKeyCode();
			switch(key) {
			case KeyEvent.VK_CONTROL:
				if(missileDestroyed)
					fire();
				break;
			case KeyEvent.VK_LEFT :
				dir = Direction.STOP;
				bL = false;
				break;
			case KeyEvent.VK_UP :
				dir = Direction.STOP;
				bU = false;
				break;
			case KeyEvent.VK_RIGHT :
				dir = Direction.STOP;
				bR = false;
				break;
			case KeyEvent.VK_DOWN :
				dir = Direction.STOP;
				bD = false;
				break;
			case KeyEvent.VK_D :
				System.out.println(TankClient.GAME_WIDTH+" "+TankClient.GAME_HEIGHT+"     774 798");// test; delete when done
				break;
			}
			//locateDirection();
		}
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
