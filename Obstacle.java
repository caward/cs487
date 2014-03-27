import java.awt.Point;

public class Obstacle extends Square
{
	String imge = "src/obstacle.png";
	
	public Obstacle(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
	}

	final int SIGHT = 0;
	

	public Squares getSquareType()
	{
		return Squares.OBSTACLE;
	}
}