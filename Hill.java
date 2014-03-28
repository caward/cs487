import java.awt.Point;


public class Hill extends Square
{
	String imge = "src/GameHill.png";
	final int SIGHT = 7;
	
	public Hill(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
		setSight(SIGHT);
	}

	public Squares getSquareType()
	{
		return Squares.HILL;
	}
}