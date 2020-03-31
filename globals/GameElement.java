package globals;

import java.awt.Graphics;

public interface GameElement {

	public void draw(Graphics g);
	
	public void update(long millis);
}
