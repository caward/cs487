import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Square
{
	Point position;
	GameBoard board;
	BufferedImage bimg = null;
	String imge = "src/Grass1_opt.jpg";
	String imge1 = "src/green_hill_icon_opt.png";
	Image img;
	Image grass = new ImageIcon(imge).getImage();
	int SIGHT = 3;
	//enum Squares {PLAIN, HILL, OBSTACLE, PIT }

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

	public void areaEffect(Tank tank)
	{
		tank.setSight(SIGHT);
	}

	public Squares getSquareType()
	{
		return Squares.PLAIN;
	}

	public boolean isUsed()
	{
		ArrayList <Tank> tankList =  board.getTankList();
		Rectangle rect = new Rectangle((int)position.getX(), (int)position.getY(), getWidth(), getHeight());
		for(Tank t : tankList)
		{
			Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
			if(rect.intersects(rectT))
			{
				return true;
			}
		}
		return false;
	}

	public Tank getTank()
	{
		ArrayList <Tank> tankList =  board.getTankList();
		Rectangle rect = new Rectangle((int)position.getX(), (int)position.getY(), getWidth(), getHeight());
		for(Tank t : tankList)
		{
			Rectangle rectT = new Rectangle((int)t.getPosition().getX(),(int)t.getPosition().getY(), Tank.WIDTH, Tank.HEIGHT);
			if(rect.intersects(rectT))
			{
				return t;
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
    
    public void setSight(int s)
    {
    	SIGHT = s;
    }

}