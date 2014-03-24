import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class GameBoard
{
	Square[][] square;
	ArrayList<Tank> tanks;
	Point p;

	public GameBoard(int row, int col)
	{
		square = new Square[row][col];
	}

	public void setBoard()
	{
		for(int i=0; i<square.length; i++)
		{          
	        for(int j=0; j<square.length; j++)
	        {
	            double random = Math.random();
	            if (random <.05)
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Obstacle(p,this);
	            }else if (random < .15)
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Pit(p,this);
	            }else
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Square(p,this);
	            }
	        }
	    }
	}
	
	public void draw(Graphics g)
	{
		for(int i = 0; i < square.length; i ++)
		{
			for(int j = 0; j<square.length; j++)
			{				
				g.drawImage(square., i * bimg.getWidth(), 22+j*bimg.getHeight(), null);//g.drawLine(0, 20 + i * 40, 680, 20 + i * 40);
				//g.drawImage(img, 0 * bimg.getWidth(), 20, null);//g.drawLine(i * 40, 0, i * 40, 700);
			}
		}
	}
	public void setTankList(ArrayList<Tank> t)
	{
		tanks = t;
	}
	
	public ArrayList<Tank> getTankList()
	{
		return tanks;
	}
}