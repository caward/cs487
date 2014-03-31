
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
	Image blowup;
	String img = "src/abrams_m1_battle_tank32.png"; //Tank Image pathway
	String img1 = "src/nuclear32.png"; //Tank explosion Image Pathway
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
	private boolean visible = true; //shows if tank is visible or not on screen
	private int scanCost = 0;
	private int localMP = 0;
	private int moveCost= 0;
	private int observeCost=0;
	private int fireCost=0;
	private boolean tankDestroyed = false;

	
	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
		p = new Point(x,y);
		tankImage = new ImageIcon(img).getImage();
		temp = new ImageIcon(img).getImage();
		blowup = new ImageIcon(img1).getImage();
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
		//autoActive();
		// This draws the tank
		if(isVisible())
		{
			g.drawImage(tankImage,x,y,null);
		}
		// This makes the outline of the healthbar 
		if(!tankDestroyed && isVisible())
		{
		g.drawRect(x-7, y-8, healthBarWidth, healthBarHeight);
		//draws remaining health
		g.setColor(Color.GREEN);
		g.fillRect(x-7, y-8, (int)(healthBarWidth*percentage), 5);
		}else if(tankDestroyed)
		{
			tankImage = blowup;
			g.drawImage(tankImage,x,y,null);
			tc.getTanks().remove(this);
		}
		g.setColor(c);
		
	}
	
	public void changeDirection(Direction d)
	{
		dir = d;
	}
	
	public Direction getDirection()
	{
		return dir;
	}
	
	public void setPtDir(Direction pt)
	{
		ptDir = pt;
	}
	
	public Direction getPtDir()
	{
		return ptDir;
	}
	
	void move()
	{
		if(isClear(x,y,dir))
		{
			if(localMP>=moveCost)
			{
				localMP-=moveCost;
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

				setPosition(x,y); //set current position of tank
				getAreaEffect();  //Sets sight allowed by the square
				makeTanksInRangeVisible();

				if(this.dir != Direction.STOP)
				{
					this.ptDir = this.dir;
				}
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
		ArrayList <Square> obstacleList =  tc.board.getObstacleList();
		if(obstacleList != null)
		{
			for(Square t : obstacleList)
			{
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), t.getWidth(), t.getHeight());
				if(rect.intersects(rectT))
				{
					return false;
				}
			}
		}
		ArrayList <Tank> tankList =  tc.board.getTankList();
		if(tankList != null)
		{
			for(Tank t : tankList)
			{
				if(!t.equals(this))
				{
					Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
					if(rect.intersects(rectT))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

	public void keyPressed(KeyEvent e)
	{
		if(missileDestroyed)
		{
			int key = e.getKeyCode();
			switch(key) {
			case KeyEvent.VK_LEFT :
				 changeDirection(Direction.L);
				bL = true;
				break;
			case KeyEvent.VK_UP :
				changeDirection(Direction.U);
				bU = true;
				break;
			case KeyEvent.VK_RIGHT :
				changeDirection(Direction.R);
				bR = true;
				break;
			case KeyEvent.VK_DOWN :
				changeDirection(Direction.D);
				break;
			case KeyEvent.VK_P :
				System.out.println("Sight: "+sight);
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
				changeDirection(Direction.STOP);
				bL = false;
				break;
			case KeyEvent.VK_UP :
				changeDirection(Direction.STOP);
				bU = false;
				break;
			case KeyEvent.VK_RIGHT :
				changeDirection(Direction.STOP);
				bR = false;
				break;
			case KeyEvent.VK_DOWN :
				changeDirection(Direction.STOP);
				bD = false;
				break;
			case KeyEvent.VK_W :
				changeDirection(Direction.U);
				rotateImage(180);
				break;
			case KeyEvent.VK_A :
				changeDirection(Direction.L);
				rotateImage(90);
				break;
			case KeyEvent.VK_S :
				changeDirection(Direction.D);
				rotateImage(0);
				break;
			case KeyEvent.VK_D :
				changeDirection(Direction.R);
				rotateImage(270);
				break;
			}
			if(this.dir != Direction.STOP)
			{
				this.ptDir = this.dir;
			}
			changeDirection(Direction.STOP);
		}
	}
	
	public void fire()
	{
		if(localMP>=fireCost)
		{
			localMP-=fireCost;
			//Positions missile in the middle of tank
			int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 -9;
			int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;

			Missile m = new Missile(x, y, ptDir, this.tc, this);
			tc.missiles.add(m);
			missileDestroyed = false;
		}
	}
	
	//Gets the square that the tank is on
	public Square getSquare()
	{
		Rectangle rect = new Rectangle(x, y, WIDTH, HEIGHT);
		Square[][] squareList =  tc.board.getSquareDblArray();
		Square t = null;
		for(int i=0; i<squareList.length; i++)
		{
			for(int j=0; j<squareList[i].length; j++)
			{
				t = squareList[i][j];
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), t.getWidth(), t.getHeight());
				if(rect.intersects(rectT))
				{
					return t;
				}
			}
		}
		return null;
	}
	
	//Gets the Effect that happens on the square
	public void getAreaEffect()
	{
		Square s = getSquare();
		s.areaEffect(this);
	}
	
	//Makes all tanks visible on screen 
	public void scan()
	{
		if(localMP>=scanCost)
		{
			localMP -= scanCost;
			ArrayList <Tank> tanks = tc.board.getTankList();;
			if(tanks != null)
			{
				for(Tank tank: tanks)
				{
					tank.setVisibility(true);
				}
			}
		}
	}
	
	//Increases sight
	public void observe()
	{
		if(localMP>=observeCost)
		{
			localMP-=observeCost;
			setSight(5);
		}
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
		x=px;
		y=py;
		p.setLocation(px, py);
	}
	
	public Point getPosition()
	{
		return p;
	}

	public void setVisibility(boolean see)
	{
		visible = see;	
	}

	public boolean isVisible()
	{
		return visible ;
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
			tankDestroyed = true;
		}	
	}
	
	//Makes tank within sight visible and adjusts fog
	public void makeTanksInRangeVisible()
	{
		ArrayList<Tank> tanks = tc.getTanks();
		Square[][] squareDbl = tc.board.getSquareDblArray();
		int x,y;
		for(Tank t: tanks)
		{
			if(!t.equals(this))
			{
				t.setVisibility(false);
			}
		}
		for(int i = 0; i<squareDbl.length; i++)
		{
			for(int j = 0; j<squareDbl[0].length; j++)
			{
				squareDbl[i][j].setFog(true);
			}
				
		}
		Square square = getSquare();
		x = (int)square.getSquareIndex().getX();
		y = (int)square.getSquareIndex().getY();
		
		for(int i=0; i<=sight; i++)
		{
			//Left
			if((x-i)>=0)
			{
				squareDbl[x-i][y].setFog(false);
				if(squareDbl[x-i][y].isUsed())
					squareDbl[x-i][y].getTank().setVisibility(true);
			}
			//Right
			if((x+i)<squareDbl[0].length)
			{
				squareDbl[x+i][y].setFog(false);
				if(squareDbl[x+i][y].isUsed())
					squareDbl[x+i][y].getTank().setVisibility(true);
			}
			//Up
			if((y-i)>=0)
			{
				squareDbl[x][y-i].setFog(false);
				if(squareDbl[x][y-i].isUsed())
					squareDbl[x][y-i].getTank().setVisibility(true);
			}
			//Down
			if((y+i)<squareDbl.length)
			{
				squareDbl[x][y+i].setFog(false);
				if(squareDbl[x][y+i].isUsed())
					squareDbl[x][y+i].getTank().setVisibility(true);
			}
		}
	}
	
	public void autoActive()
	{
		
	}
}
