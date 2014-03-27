import java.awt.Point;


public class Hill extends Square
{
	String imge = "src/GameHill.png";
	
	public Hill(Point position, GameBoard board)
	{
		super(position, board);
		setImage(imge);
	}

	final int SIGHT = 7;

	public Squares getSquareType()
	{
		return Squares.HILL;
	}
}