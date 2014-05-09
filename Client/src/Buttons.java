import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Buttons extends JFrame{
	Button up;
    Button down;
    Button right;
    Button left;
	public Buttons(int x, int y){
		JPanel p = new JPanel();
		this.add(p);
		JPanel south = new JPanel(new GridLayout(x, y, 100, 50));
		up = new Button("Up");
		down = new Button("Down");
		right = new Button("Right");
		left = new Button("Left");
		south.add(up);
		this.add(south, BorderLayout.SOUTH);
		
	}

}
