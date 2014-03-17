
public class Command
{
	String instruction = null;
	String direction = null;
	public Command(String order)
	{
		instruction = order;
	}

	public Command(String order, String direction)
	{
		instruction = order;
		direction = direction;
	}

	public String getInstruction()
	{
		return instruction;
	}

	public String getDirection()
	{
		return direction;
	}
}