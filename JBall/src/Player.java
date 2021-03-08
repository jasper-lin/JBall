import javax.swing.JPanel;

public class Player extends MovableObject
{
	private boolean canJump;

	public Player(JPanel mainPanel, String filename, int x, int y, int width, int height) {
		super(mainPanel, filename, x, y, width, height);
		canJump = true;
		setSpeedXY(25,25);
	}
	public void jump()
	{
		if(canJump) {
			canJump = false;
			setSpeedY(-30);
			setY(getY()-150);
		}
	}
	public void stand(MovableObject m)
	{
		if(this.intersects(m)) {
			setY(m.getY() - getHeight());
			setSpeedY(0);
			canJump = true;
		}
		else {
			setSpeedY(30);
		}

	}
	
	public void inBounds()
	{
		if(getY() > 3000) {
			setXY(1000, 1000);
		}
	}
	public void gravity() {
		setSpeedY(getSpeedY() + 1);
		moveDown();
	}

}
