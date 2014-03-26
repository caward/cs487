import java.awt.Image;
import java.awt.Point;
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
	public String imge = "src/Grass1_opt.jpg";
	String imge1 = "src/green_hill_icon_opt.png";
	Image img;
	Image grass = new ImageIcon(imge).getImage();
	final int ROW = 5;
	final int COL = 5;
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

	//Gets position in array/table
	public Point getPosition()
	{
		return position;
	}

	public void areaEffect(Tank tank)
	{
		tank.setVisibility(ROW,COL);
	}

	public Squares getSquareType()
	{
		return Squares.PLAIN;
	}

	public boolean isUsed()
	{
		ArrayList <Tank> tankList =  board.getTankList();
		Rectangle rect = new Rectangle(p.getX(), p.getY(), getWidth(), getHeight());
		for(Tank t : tankList)
		{
			Rectangle rectT = new Rectangle(t.getPosition().getX(),t.getPosition.getY(), Tank.WIDTH, Tank.HEIGHT)
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
		Rectangle rect = new Rectangle(p.getX(), p.getY(), getWidth(), getHeight());
		for(Tank t : tankList)
		{
			Rectangle rectT = new Rectangle(t.getPosition().getX(),t.getPosition.getY(), Tank.WIDTH, Tank.HEIGHT)
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
        return img1;
    }

}