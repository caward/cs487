import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class GameBoard
{
	Square[][] square;
	ArrayList<Tank> tanks;
	Point p;
	int row;
	int col;

	public GameBoard(int row, int col)
	{
		this.row = row;
		this.col = col;
		square = new Square[row][col];
		setBoard();
	}

	private void setBoard()
	{
		for(int i=0; i<square.length; i++)
		{          
	        for(int j=0; j<square[i].length; j++)
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
	            }else if (random < .25)
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Hill(p,this);
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
				g.drawImage(square[i][j].getImage(), i * square[i][j].getWidth(), 22+j*square[i][j].getHeight(), null);
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

	public int getImageWidth()
	{
		return square[0][0].getWidth();
	}

	public int getImageHeight()
	{
		return square[0][0].getHeight();
	}

	public int getGameWidth()
	{
		return row*getImageHeight();
	}

	public int getGameHeight()
	{
		return col*getImageWidth()+22;
	}
}