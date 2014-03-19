import java.util.ArrayList;


public class GameBoard
{
	Square[][] square;
	ArrayList<Tank> tanks;

	public GameBoard()
	{
		
	}

	public void setTankList(ArrayList<Tank> t)
	{
		tanks = t;
	}
	
	public ArrayList<Tank> getTankList()
	{
		return tanks;
	}
}