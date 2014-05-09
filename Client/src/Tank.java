import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Tank
{
	int id;
	int level;
	public static int XSPEED = 40; //Distance traveled in one move in the x direction
	public static int YSPEED = 40; //Distance traveled in one move in the y direction
	public static int WIDTH = 20; //Width of tank
	public static int HEIGHT = 20; //Height of tank 
	public static int MP = 190; 
	public static int HP = 50;
	private final int MOVE = 1;
	private final int FIRE = 2;
	private final int END = 3;
	private final int SAVE = 4;
	private final int LEFT = 5;
	private final int RIGHT = 6;
	private final int UP = 7;
	private final int DOWN = 8;
	public final int Gameend = 9;
	long playerStartTime = 60000;
	long playerTime = 0;

	private BufferedImage bimg = null;
	private TankClient tc;
	private Point p;
	private Image tankImage;
	private Image temp;
	private Image blowup;
	private String img = "src/abrams_m1_battle_tank32.png"; //Tank Image pathway
	private String img1 = "src/nuclear32.png"; //Tank explosion Image Pathway
	private Player player;
	
	int x, y;
	public static boolean missileDestroyed = true;
	private double percentage = 1;
	private double loss = 0;
	private boolean bL=false, bU=false, bR=false, bD = false;
	enum Direction {L, U, R, D, STOP};
	private int healthBarWidth = 48; //Length of the health bar
	private int healthBarHeight = 5; //Height of the health bar
	Direction dir = Direction.STOP;
	Direction ptDir = Direction.D;
	private int sight = 3;
	private boolean visible = true; //shows if tank is visible or not on screen
	private int scanCost = 10;
	private int localMP = 10; //10
	private int runningTotalMP=190;
	private int moveCost = 2;
	private int observeCost=5;
	private int fireCost=3;
	private boolean tankDestroyed = false;
	private String type ="Player";
//	private int count=0;
	private int halfScanCost = 5;
	private int resume = 0;
	boolean start = false;
	private long secondTime;
	
	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
		level = 1;
		p = new Point(x,y);
		tankImage = new ImageIcon(img).getImage();
		temp = new ImageIcon(img).getImage();
		blowup = new ImageIcon(img1).getImage();
		runningTotalMP = MP;
		try
		{
			bimg = ImageIO.read(new File(img));
			WIDTH = bimg.getWidth();
			HEIGHT = bimg.getHeight();
		}catch(IOException e)
		{
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
//		if(isVisible())
//		{
//			g.drawImage(tankImage,x,y,null);
//		}
		
		//g.drawString("Total MP: "+runningTotalMP+" Turn MP: "+localMP, 550, 50);
		if(start)//Start clock
		{
			secondTime = (playerTime-System.currentTimeMillis())/1000;
			g.drawString(secondTime/60+":"+secondTime%60, 300, 50);
			if((secondTime/60)<=0&&(secondTime%60)<=0)
			{
				start=false;
				end();
				TankMoveMsg msg = new TankMoveMsg(id, dir,END);
				tc.nc.send(msg);
			}
		}
		// This makes the outline of the healthbar 
		if(!tankDestroyed && isVisible())
		{
			g.drawRect(x-7, y-8, healthBarWidth, healthBarHeight);
			g.drawImage(tankImage,x,y,null);
			//draws remaining health
			g.setColor(Color.GREEN);
			g.fillRect(x-7, y-8, (int)(healthBarWidth*percentage), 5);
			g.drawString("id" + id, x, y - 10);
		}else if(tankDestroyed)
		{
			tankImage = blowup;
			g.drawImage(tankImage,x,y,null);
			g.drawString("YOU LOSE", x, y+7);
			tc.getTanks().remove(this);	
		}
		g.setColor(c);
	}
	
	public int getResume()
	{
		return resume;
	}

	public void setResume(int resume)
	{
		this.resume = resume;
	}
	
	public void setLevel(int lvl)
	{
		level = lvl;
		HP *= level;
	}
	
	public TankClient getTC()
	{
		return tc;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return type;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getID()
	{
		return id;
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
	
	public int getMP()
	{
		return (runningTotalMP+localMP);
	}

	public Direction getPtDir()
	{
		return ptDir;
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
				
				getAreaEffect();  //Sets sight allowed by the square
				setPosition(x,y); //set current position of tank
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
			case KeyEvent.VK_P :
				System.out.println("Sight: "+sight);
				break;
			}
		}
	}
	
	void locateDirection()
	{
		Direction oldDir = this.dir;
		
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
		
		if(dir != oldDir){
//			TankMoveMsg msg = new TankMoveMsg(id, dir);
//			tc.nc.send(msg);
		}
	}

	public void keyReleased(KeyEvent e)
	{
		//Can only go if there is no missile on screen
		if(missileDestroyed && tc.playerTurnID==id)
		{
			TankMoveMsg msg;
			int key = e.getKeyCode();
			switch(key) {
			case KeyEvent.VK_CONTROL: //Control key fires missile
				if(localMP>=fireCost)
				{
					localMP-=fireCost;
					fire();
					msg = new TankMoveMsg(id, dir,FIRE);
					tc.nc.send(msg);
				}
				break;
			case KeyEvent.VK_LEFT :
				if(localMP>=moveCost)
				{
					localMP-=moveCost;
					changeDirection(Direction.L);
					move();
					msg = new TankMoveMsg(id, dir,MOVE);
					tc.nc.send(msg);
					bL = true;
				}
				break;
			case KeyEvent.VK_UP :
				if(localMP>=moveCost)
				{
					localMP-=moveCost;
					changeDirection(Direction.U);
					move();
					msg = new TankMoveMsg(id, dir,MOVE);
					tc.nc.send(msg);
					bU = true;
				}
				break;
			case KeyEvent.VK_RIGHT :
				if(localMP>=moveCost)
				{
					localMP-=moveCost;
					changeDirection(Direction.R);
					move();
					msg = new TankMoveMsg(id, dir,MOVE);
					tc.nc.send(msg);
					bR = true;
				}
				break;
			case KeyEvent.VK_DOWN :
				if(localMP>=moveCost)
				{
					localMP-=moveCost;
					changeDirection(Direction.D);
					move();
					msg = new TankMoveMsg(id, dir,MOVE);
					tc.nc.send(msg);
				}
				break;
			case KeyEvent.VK_W :
				changeDirection(Direction.U);
				rotateImage(180);
				msg = new TankMoveMsg(id, dir,UP);
				tc.nc.send(msg);
				break;
			case KeyEvent.VK_A :
				changeDirection(Direction.L);
				rotateImage(90);
				msg = new TankMoveMsg(id, dir,LEFT);
				tc.nc.send(msg);
				break;
			case KeyEvent.VK_S :
				changeDirection(Direction.D);
				rotateImage(0);
				msg = new TankMoveMsg(id, dir,DOWN);
				tc.nc.send(msg);
				break;
			case KeyEvent.VK_D :
				changeDirection(Direction.R);
				rotateImage(270);
				msg = new TankMoveMsg(id, dir,RIGHT);
				tc.nc.send(msg);
				break;
			case KeyEvent.VK_P :
				System.out.println(player.getName()+" "+player.getPlayerID());
				break;
			case KeyEvent.VK_O :
				observe();
				setPosition(x,y);
				break;
			case KeyEvent.VK_E :
				end();
				msg = new TankMoveMsg(id, dir,END);
				tc.nc.send(msg);
				break;
			case KeyEvent.VK_L :
				scan();
				break;
			case KeyEvent.VK_H :
				halfScan();
				break;
			case KeyEvent.VK_R :
				save();	
				msg = new TankMoveMsg(id, dir,SAVE);
				tc.nc.send(msg);
				break;
			}
			if(this.dir != Direction.STOP)
			{
				this.ptDir = this.dir;
			}
		}
	}
	
	public void fire()
	{
		
			//Positions missile in the middle of tank
			int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2 -9;
			int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;

			Missile m = new Missile(x, y, dir, this.tc, this);
			//Missile m = new Missile(x, y, ptDir, this.tc, this);
			tc.missiles.add(m);
			GameSounds.missileLaunch();
			missileDestroyed = false;
		
	}
	
	//Gets the square that the tank is on
	public Square getSquare()
	{
		Rectangle rect = new Rectangle(x, y, WIDTH, HEIGHT);
		Square[][] squareList =  tc.board.getSquareDblArray();
		Square t = null;
		for(int i=0; i<tc.board.getRow(); i++)
		{
			for(int j=0; j<tc.board.getCol(); j++)
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
	
	public void halfScan()
	{
		if(localMP>=halfScanCost)
		{
			localMP -= halfScanCost;
			ArrayList <Tank> tanks = tc.board.getTankList();
			int count = 0;
			if(tanks != null)
			{
				int num = tanks.size()/2;
				for(int i =0; i< tanks.size(); i++)
				{
					Tank tank = tanks.get(i);
					
					if(tank.getID() != id && count <=num)
					{
						count++;
						tank.setVisibility(true);
					}	
				}
			}
		}
	}
	
	//Makes all tanks visible on screen 
	public void scan()
	{
		if(localMP>=scanCost)
		{
			localMP -= scanCost;
			ArrayList <Tank> tanks = tc.board.getTankList();
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
			setSight(4);
		}
	}
	
	//Rotates image
	public void rotateImage(double degree)
	{
		ImageIcon imgIcon = new ImageIcon(temp);
		BufferedImage blankCanvas = new BufferedImage(imgIcon.getIconWidth(), imgIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)blankCanvas.getGraphics();
		g2d.rotate(Math.toRadians(degree),imgIcon.getIconWidth()/2,imgIcon.getIconHeight()/2);
		if(isVisible())
			g2d.drawImage(temp,0,0,null);
		tankImage = blankCanvas;
	}

	public void setPosition(int px, int py)
	{
		x=px;
		y=py;
		p.setLocation(px, py);
		makeTanksInRangeVisible();
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
		x = player.getXcoor();
		y = player.getYcoor();
		setLevel(player.getLevel());
	}
	
	public Player getPlayer()
	{
		return player;
	}

	public int getHealth()
	{
		return (HP-loss);
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
			GameSounds.tankExplode();
			if(type.equals("Player"))
			{
				//player.increaseLoss();
//				tc.dbc.loss(player.getLoss(), player.getPlayerID());
//				tc.dbc.close();
				//tc.nc.close();
			}
		}	
	}
	
	public void save()
	{
		File file = new File("SaveFile.txt");
		if (!file.exists())
		{
			int[][] map = tc.board.getMap();
			//Write 2D array to file
			PrintStream ps;
			try 
			{
				ps = new PrintStream(new FileOutputStream(file));
				for(int row=0;row < 15;row++)
				{
					for(int col=0; col < 15;col++)
					{
						ps.print(map[row][col]);
					}
					ps.println();
				}
				ps.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Store coordinates in dbc
		//tc.dbc.updateCoordinates(player.getPlayerID(), x, y, id);
		setResume(1);
	}
	
	//Makes tank within sight visible and adjusts fog
	public void makeTanksInRangeVisible()
	{
		ArrayList<Tank> tanks = tc.getTanks();
		Square[][] squareDbl = tc.board.getSquareDblArray();
		int x,y;
		int area = sight;
		for(Tank t: tanks)
		{
			if(!t.equals(this))
			{
				t.setVisibility(false);
			}
		}
		for(int i = 0; i<tc.board.getRow(); i++)
		{
			for(int j = 0; j<tc.board.getCol(); j++)
			{
				squareDbl[i][j].setFog(true);
			}
				
		}
		Square square = getSquare();
		x = (int)square.getSquareIndex().getX();
		y = (int)square.getSquareIndex().getY();
		
		for(int i=0; i<=sight; i++)
		{
			area-=i;
			//Left
			if((x-i)>=0)
			{
				squareDbl[x-i][y].setFog(false);
				if(squareDbl[x-i][y].isUsed())
					squareDbl[x-i][y].getTank().setVisibility(true);
			}
			//Right
			if((x+i)<tc.board.getRow())
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
				for(int j = 1; j<=area; j++)
				{
					
					if((x+j)<tc.board.getRow())
					{
						squareDbl[x+j][y-i].setFog(false);
						if(squareDbl[x+j][y-i].isUsed())
							squareDbl[x+j][y-i].getTank().setVisibility(true);
					}
					if((x-j)>=0)
					{
						squareDbl[x-j][y-i].setFog(false);
						if(squareDbl[x-j][y-i].isUsed())
							squareDbl[x-j][y-i].getTank().setVisibility(true);
					}
				}
			}
			//Down
			if((y+i)<tc.board.getCol())
			{
				squareDbl[x][y+i].setFog(false);
				if(squareDbl[x][y+i].isUsed())
					squareDbl[x][y+i].getTank().setVisibility(true);
				for(int j = 1; j<=area; j++)
				{					
					//make right 
					if((x+j)<tc.board.getRow())
					{
						squareDbl[x+j][y+i].setFog(false);
						if(squareDbl[x+j][y+i].isUsed())
							squareDbl[x+j][y+i].getTank().setVisibility(true);
					}
					if((x-j)>=0)
					{
						squareDbl[x-j][y+i].setFog(false);
						if(squareDbl[x-j][y+i].isUsed())
							squareDbl[x-j][y+i].getTank().setVisibility(true);
					}
				}
			}
		}
	}
	
	public void end()
	{
		int myId = getID();
		int nextID =(myId+1);
		for(Tank t:tc.tanks)
		{
			if(t.getID()==nextID)
			{
				tc.playerTurnID = nextID;
				t.startClock();
				start=false;
				runningTotalMP+=localMP;
				if(runningTotalMP<10)
				{
					localMP=runningTotalMP;
				}
				else{
					localMP=10;
					runningTotalMP-=10;
				}
				return;
			}
		}
		for(Tank t:tc.tanks)
		{
			if(t.getID()==100)
			{
				tc.playerTurnID = 100;
				t.startClock();
				start=false;
				runningTotalMP+=localMP;
				if(runningTotalMP<10)
				{
					localMP=runningTotalMP;
				}
				else{
					localMP=10;
					runningTotalMP-=10;
				}
				return;
			}
		}
		
		
	}
	
	public void startClock()
	{
		// TODO Auto-generated method stub
		if(type.equals("Player"))
		{
			playerTime = System.currentTimeMillis()+playerStartTime;
			start = true;
		}
	}

	public void autoActive()
	{
		
	}
}
