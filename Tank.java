
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Tank {
	public static int XSPEED = 40; //Distance traveled in one move in the x direction
	public static int YSPEED = 40; //Distance traveled in one move in the y direction
	public static int WIDTH = 20; //Width of tank
	public static int HEIGHT = 20; //Height of tank 
	public static int MP = 200; 
	public static int HP = 200;

	BufferedImage bimg = null;
	TankClient tc;
	Point p;
	Image tankImage;
	Image temp;
	String img = "src/abrams_m1_battle_tank32.png"; //Tank Image pathway
	Player player;
	
	private int x, y;
	public static boolean missileDestroyed = true;
	private double percentage = 1;
	private double loss = 0;
	private boolean bL=false, bU=false, bR=false, bD = false;
	enum Direction {L, U, R, D, STOP};
	private int healthBarWidth = 48; //Length of the health bar
	private int healthBarHeight = 5; //Height of the health bar
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	private int sight = 3;
	private boolean visibile = true; //shows if tank is visibile or not on screen

	
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
		g.fillRect(x-7, y-8, (int)(healthBarWidth*percentage), 5);
		g.setColor(c);
		
		
		//move();
	}
	
	void move()
	{
		if(isClear(x,y,dir))
		{
			switch(dir) {
			case L:
				x -= XSPEED;
				rotateImage(90);//Turn tank to the left
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				break;
			case U:
				y -= YSPEED;
				rotateImage(180); //Turn tank to up
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				break;
			case R:
				x += XSPEED;
				rotateImage(270);//Turn tank to the right
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				break;
			case D:
				y += YSPEED;//Turn tank to down
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

			setPosition(x,y);

			if(this.dir != Direction.STOP)
			{
				this.ptDir = this.dir;
			}
		}
		
	}
	
	//Checks if the position is clear of obstacles and tanks
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
		Rectangle rect = new Rectangle(tmpx, tmpy, WIDTH, HEIGHT);
		ArrayList <Square> obstacleList =  tc.board.obstacles;
		for(Square t : obstacleList)
		{
			Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), t.getWidth(), t.getHeight());
			if(rect.intersects(rectT))
			{
				return false;
			}
		}
//		ArrayList <Tank> tankList =  tc.board.getTankList();
//		for(Tank t : tankList)
//		{
//			Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
//			if(rect.intersects(rectT))
//			{
//				return false;
//			}
//		}
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
			move();
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
		//Can only go if there is no missile on screen
		if(missileDestroyed)
		{
			int key = e.getKeyCode();
			switch(key) {
			case KeyEvent.VK_CONTROL: //Control key fires missile
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
			case KeyEvent.VK_W :
				dir = Direction.U;
				rotateImage(180);
				break;
			case KeyEvent.VK_A :
				dir = Direction.L;
				rotateImage(90);
				break;
			case KeyEvent.VK_S :
				dir = Direction.D;
				rotateImage(0);
				break;
			case KeyEvent.VK_D :
				dir = Direction.R;
				rotateImage(270);
				break;
			}
			if(this.dir != Direction.STOP)
			{
				this.ptDir = this.dir;
			}
			dir = Direction.STOP;
		}
	}
	
	public Missile fire()
	{
		//Positions missile in the middle of tank
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 -9;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		
		Missile m = new Missile(x, y, ptDir, this.tc);
		tc.missiles.add(m);
		missileDestroyed = false;
		return m;
	}
	
	//Rotates image
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

	public void setVisibility(boolean see)
	{
		visibile = see;	
	}

	public boolean isVisibile()
	{
		return visibile ;
	}

	public void setSight(int num)
	{
		sight = num;
	}

	public void setPlayer(Player p)
	{
		player = p;
	}

	public void takeHit(int damage)
	{
		loss+=damage;
		if(HP-loss>0)
		{
			percentage = (double)(HP-loss)/(double)HP;
		}else
		{
			
		}	
	}
}
