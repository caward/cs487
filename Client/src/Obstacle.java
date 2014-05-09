import java.awt.Point;

public class Obstacle extends Square
{
	String imge = "src/obstacle.png";
	final int SIGHT = 0;
	
	public Obstacle(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
	}

	
	public Squares getSquareType()
	{
		return Squares.OBSTACLE;
	}
}