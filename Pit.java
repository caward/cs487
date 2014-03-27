import java.awt.Point;

public class Pit extends Square
{
	String imge = "src/Pit.png";
	
	public Pit(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
	}

	final int SIGHT = 1;


	public Squares getSquareType()
	{
		return Squares.PIT;
	}
}