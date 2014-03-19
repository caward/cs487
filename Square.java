import java.awt.Point;
import java.util.ArrayList;

public class Square
{
	Point position;
	GameBoard board;
	final int ROW = 5;
	final int COL = 5;
	//enum Squares {PLAIN, HILL, OBSTACLE, PIT }

	public Square(Point position, GameBoard board)
	{
		this.position = position;
		this.board = board;
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
		for(Tank t : tankList)
		{
			if(t.getPosition().equals(getPosition()))
			{
				return true;
			}
		}
		return false;
	}

}