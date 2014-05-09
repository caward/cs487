import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Square
{
	private Point position;
	private GameBoard board;
	private BufferedImage bimg = null;
	private String imge = "src/Grass1_opt.jpg";
	private int SIGHT = 3;
	private boolean isFoggy = true;

	public Square(Point position, GameBoard board)
	{
		this.position = position;
		this.board = board;
		try
		{
			bimg = ImageIO.read(new File(imge));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Draws Fog on square
	public void draw(Graphics g)
	{
		if(isFoggy)
		{
			Color c = g.getColor();
			Color myColour = new Color(169, 169,169, 128 );
			g.setColor(myColour);
			g.fillRect(position.x * getWidth(),22+position.y*getHeight(),getWidth(),getHeight());
			g.setColor(c);
		}
	}

	//Gets position of square on screen
	public Point getPosition()
	{
		return new Point((int)(position.getX()*getWidth()),(int)(22+position.getY()*getHeight()));
	}
	
	//Returns the index of the Square in the double array
	public Point getSquareIndex()
	{
		return position;
	}
	
	
	//Sets the image of the square
	public void setImage(String image)
	{
		try
		{
			bimg = ImageIO.read(new File(image));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	//Sets the vision of tank
	public void areaEffect(Tank tank)
	{
		tank.setSight(SIGHT);
	}

	public Squares getSquareType()
	{
		return Squares.PLAIN;
	}

	//True if square is occupied by Tank
	public boolean isUsed()
	{
		ArrayList <Tank> tankList =  board.getTankList();
		if(tankList != null )
		{
			Rectangle rect = new Rectangle((int)getPosition().getX(), (int)getPosition().getY(), getWidth(), getHeight());
			for(Tank t : tankList)
			{
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
				if(rect.intersects(rectT))
				{
					return true;
				}
			}
		}
		return false;
	}

	//Gets tank that is on square
	public Tank getTank()
	{
		ArrayList <Tank> tankList =  board.getTankList();
		if(tankList != null)
		{
			Rectangle rect = new Rectangle((int)getPosition().getX(), (int)getPosition().getY(), getWidth(), getHeight());
			for(Tank t : tankList)
			{
				Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
				if(rect.intersects(rectT))
				{
					return t;
				}
			}
		}
		return null;
	}

	public int getWidth()
    {
            return bimg.getWidth();
    }
        
    public int getHeight()
    {
        return bimg.getHeight();
    }
        
    public Image getImage()
    {
        return (Image)bimg;
    }
    
    //Sets field of vision that the Square allow the tank to see
    public void setSight(int s)
    {
    	SIGHT = s;
    }
    
    public void setFog(boolean f)
    {
    	isFoggy  = f;
    }
}