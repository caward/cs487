import java.awt.Point;

public class Obstacle extends Square
{
	public Obstacle(Point position, GameBoard board)
	{
		super(position, board);
	}

	final int ROW = 0;
	final int COL = 0;

	public Squares getSquareType()
	{
		return Squares.OBSTACLE;
	}
}