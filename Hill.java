import java.awt.Point;


public class Hill extends Square
{
	public Hill(Point position, GameBoard board)
	{
		super(position, board);
	}

	final int ROW = 7;
	final int COL = 7;

	public Squares getSquareType()
	{
		return Squares.HILL;
	}
}