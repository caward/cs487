import java.awt.Point;

public class Pit extends Square
{
	public Pit(Point position, GameBoard board)
	{
		super(position, board);
	}

	final int ROW = 1;
	final int COL = 1;

	public Squares getSquareType()
	{
		return Squares.PIT;
	}
}