import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class GameBoard
{
	private Square[][] square;
	private TankClient tc;
	private ArrayList<Square> obstacles = new ArrayList<Square>();
	private Point p;
	private int row=0;
	private int col=0;

	public GameBoard(int row, int col,TankClient tc)
	{
		this.row = row;
		this.col = col;
		this.tc = tc;
		square = new Square[row][col];
		setBoard();
		setObstacles();
		
	}
	
	//Randomly creates Square field
	private void setBoard()
	{
		for(int i=0; i<row; i++)
		{          
	        for(int j=0; j<col; j++)
	        {
	            double random = Math.random();
	            if (random <.05) //Chance of square becoming an obstacle
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Obstacle(p,this);
	            }else if (random < .15) //Chance of square becoming a Pit
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Pit(p,this);
	            }else if (random < .25) //Chance of square becoming a hill
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Hill(p,this);
	            }else					//Chance of square becoming an plain square
	            {
	            	p = new Point(i,j);
	                square[i][j]= new Square(p,this);
	            }
	        }
	    }
	}
	
	//Draws Squares to screen
	public void draw(Graphics g)
	{
		for(int i = 0; i < row; i ++)
		{
			for(int j = 0; j<col; j++)
			{				
				g.drawImage(square[i][j].getImage(), i * square[i][j].getWidth(), 22+j*square[i][j].getHeight(), null);
				square[i][j].draw(g); //Draws fog
			}
		}
	}
	
	//Makes of list of the obstacles on the screen
	public void setObstacles()
	{
		for(int i=0; i<row; i++)
		{          
	        for(int j=0; j<col; j++)
	        {
	        	if(square[i][j].getSquareType()==Squares.OBSTACLE)
	        		obstacles.add(square[i][j]);
	        }
		}
	}
		

	public Square[][] getSquareDblArray()
	{
		return square;
	}
	
	public ArrayList<Tank> getTankList()
	{
		return tc.getTanks();
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
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}

	public ArrayList<Square> getObstacleList()
	{
		return obstacles;
	}
}