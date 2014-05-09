import java.awt.event.KeyEvent;



public class Tank_AI extends Tank
{

	public Tank_AI(int x, int y)
	{
		super(x, y);
		setType("AI");
		setVisibility(false);
	}

	public Tank_AI(int x, int y, TankClient tc)
	{
		super(x, y, tc);
		setType("AI");
		setVisibility(false);
	}

	public void keyPressed(KeyEvent e)
	{
		
	}

	public void keyReleased(KeyEvent e)
	{
//		int key = e.getKeyCode();
//		if (key == KeyEvent.VK_B)
//			autoActive();
	}

	//Simple AI
	public void autoActive()
	{
		if (missileDestroyed)
		{
			int key = (int) (Math.random() * 5);
			switch (key) {
			case 0:
				changeDirection(Direction.L);
				break;
			case 1:
				changeDirection(Direction.U);
				break;
			case 2:
				changeDirection(Direction.R);
				break;
			case 3:
				changeDirection(Direction.D);
				break;
			case 4:
				break;
			}
			move();
			switch (key) {
			case 0:
				changeDirection(Direction.STOP);
				break;
			case 1:
				changeDirection(Direction.STOP);
				break;
			case 2:
				changeDirection(Direction.STOP);
				break;
			case 3:
				changeDirection(Direction.STOP);
				break;
			case 4:
				fire();
				break;
			}
			if(getDirection() != Direction.STOP)
			{
				setPtDir(getDirection());
			}
			changeDirection(Direction.STOP);
		}
	}

	public void makeTanksInRangeVisible()
	{
		for(Tank t: getTC().tanks)
		{
			if(t.getType().equals("Player"))
				t.makeTanksInRangeVisible();
		}
	}
}
