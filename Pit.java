import java.awt.Point;

public class Pit extends Square
{
	String imge = "src/Pit.png";
	final int SIGHT = 1;
	
	public Pit(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
		setSight(SIGHT);
	}

	public Squares getSquareType()
	{
		return Squares.PIT;
	}
}