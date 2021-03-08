
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;



public class JBall extends JFrame 
{
	// screen constants
	private int screenWidth  = 2400;
	private int screenHeight = 1600;

	private int startMouseDragX = -1;
	private int startMouseDragY = -1;

	private boolean gameOver;

	// for buffering
	private BufferedImage back;

	// keys for movement
	private boolean[] keys;  

	// ***** declaration of JFrame variables *****

	// define a mainPanel for components
	JPanel mainPanel;

	// define JPanels for a BorderLayout
	JPanel     northPanel;   // this is the message panel
	SouthPanel southPanel;   // put your buttons on this panel
	JPanel     westPanel;    // this panel will be empty for now
	boolean    showWestPanel = false;
	JPanel     eastPanel;    // this panel will be empty for now
	boolean    showEastPanel = false;

	DrawPanel  centerPanel;  // this will be the panel with all the drawing of MovableObjects

	// title in northPanel
	JLabel northText;

	// Buttons
	JButton runButton;
	JButton stopButton;
	JButton exitButton;


	//instance variables and objects

	Player playerone;
	Player playertwo;
	MovableObject ball;
	MovableObject ground;
	MovableObject hoop;
	MovableObject backboard;
	MovableObject backboard2;
	MovableObject rim;
	MovableObject hoop2;
	MovableObject rim2; 
	MovableObject threepoint;
	private int score;
	private int score2;
	boolean release;
	boolean release2;
	boolean hold = true;
	boolean hold2 = true;
	private int jumpspot1;
	private int jumpspot2;
	MovableObject background;
	MovableObject scoreboard;
	private double time = 300.0;

	public static Font font;

	// thread for the runButton
	Thread runThread = null;
	int threadDelay = 15;  // the paintComponent method will be called every 25 milliseconds


	public void initMovableObject()
	{
		playerone = new Player(centerPanel, "Player.PNG", 1000, 700, 200, 300);

		playertwo = new Player(centerPanel, "mike.png", screenWidth*2-500, screenHeight-370, 200, 300);

		hoop = new MovableObject(centerPanel, "hoop.png", screenWidth*2-170, screenHeight/2-100, 125, 125);

		hoop2= new MovableObject(centerPanel, "hooper.png", 50, screenHeight/2-100, 125,125);

		ball = new MovableObject(centerPanel, "ball.png", screenWidth-25, screenHeight-300, 50,50);

		ground = new MovableObject(centerPanel, "Green.png", 0, 1200, screenWidth, 200);

		backboard = new MovableObject(centerPanel, "backboard.png", screenWidth*2-50,screenHeight/2-235, 25, 160);

		backboard2 = new MovableObject(centerPanel, "backboard.png", 25, screenHeight/2-235, 25, 160);

		rim = new MovableObject(centerPanel, "rim.png", screenWidth*2-170, screenHeight/2-105,30,30);

		rim2 = new MovableObject(centerPanel, "rim.png", 145, screenHeight/2-105,30,30);

		threepoint = new MovableObject(centerPanel, "backboard.png", screenWidth-10, screenHeight+100, 20, 70);

		background = new MovableObject(centerPanel, "background.png",0, 0, screenWidth*2, screenHeight+150);

		scoreboard = new MovableObject(centerPanel, "scoreboard.png", screenWidth-110, 60, 250, 120);

		font = new Font("Monaco", Font.PLAIN, 30);
	}

	// ***** public void initialize *****  
	public void initialize( )
	{
		back = null;
		gameOver = true;
		screenWidth = 2400;
		screenHeight = 1600;
		keys = new boolean[10];

		// create the buttons
		runButton  = new JButton("Run");
		stopButton  = new JButton("Stop");
		exitButton  = new JButton("Exit");

		// create a mainPanel for components
		// this is where the game will be played
		mainPanel = new JPanel();

		// ***** create JPanels for a BorderLayout *****
		northPanel   = new JPanel();
		southPanel   = new SouthPanel();
		southPanel.setListeners();

		if (showWestPanel)
		{
			westPanel    = new JPanel();
		}
		if (showEastPanel)
		{
			eastPanel    = new JPanel();
		}

		centerPanel  = new DrawPanel();

		mainPanel.setLayout(new BorderLayout());
		southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		setCenterPanelColor(Color.black);
		northPanel.setBackground(new Color(115,205,255));
		southPanel.setBackground(new Color(115,205,255));
		if (showWestPanel)
		{
			westPanel.setBackground(new Color(115,205,255));
		}
		if (showEastPanel)
		{
			eastPanel.setBackground(new Color(115,205,255));
		}


		// add buttons to the southPanel
		southPanel.add(runButton);
		southPanel.add(stopButton);
		southPanel.add(exitButton);

		northText = new JLabel("Game");
		northPanel.add(northText);


		// ***** add the panels to the mainPanel 5 areas *****
		mainPanel.add(northPanel,BorderLayout.NORTH);
		mainPanel.add(southPanel,BorderLayout.SOUTH);
		if (showEastPanel)
		{
			mainPanel.add(eastPanel,BorderLayout.EAST);
		}
		if (showWestPanel)
		{
			mainPanel.add(westPanel,BorderLayout.WEST);
		}
		mainPanel.add(centerPanel,BorderLayout.CENTER);

		southPanel.setFocusable(true);
		southPanel.requestFocus();

		initMovableObject();

		// paint the screen
		centerPanel.repaint();

		// make the mainPanel be the main content area and show it
		setContentPane(mainPanel);

		// now show the screen to the user
		setVisible(true);  // always show the screen last


		// focus the southPanel so that we can receive key strokes
		southPanel.setFocusable(true);
		southPanel.requestFocus();
	}   // end of public void initialize

	public JBall()
	{
		setSize(screenWidth,screenHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Ball");

		// initialize variables
		initialize( );
	}

	public void setMessage(String message)
	{
		northText.setText(message);
	}


	public String getMessage()
	{
		return northText.getText();
	}


	public void setThreadDelay(int threadDelay)
	{
		this.threadDelay = threadDelay;
	}


	public void setNorthPanelColor(Color color)
	{
		northPanel.setBackground(color);
	}


	public void setSouthPanelColor(Color color)
	{
		southPanel.setBackground(color);
	}


	public void setCenterPanelColor(Color color)
	{
		centerPanel.setBackground(color);
	}


	public void setWestPanelColor(Color color)
	{
		if (showWestPanel)
		{
			westPanel.setBackground(color);
		}
	}

	public void setEastPanelColor(Color color)
	{
		if (showEastPanel)
		{
			eastPanel.setBackground(color);
		}
	}

	// ***** main method *****
	public static void main(String[] arguments)
	{
		JBall theGame = new JBall();
	}


	class SouthPanel extends JPanel implements KeyListener,ActionListener, Runnable
	{
		// start of actionPerformed (ActionListener interface)
		// handle button clicks here
		public SouthPanel()
		{
			// allow buttons to listen for clicks
			super();
		}

		public void setListeners()
		{
			runButton.addActionListener(this);
			stopButton.addActionListener(this);
			exitButton.addActionListener(this);
			addKeyListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			if (source==exitButton)
			{
				gameOver = true;
				if (runThread!=null)
				{
					runThread.stop();
					runThread = null;
				}
				System.exit(0);
			}
			else if (source==runButton)
			{
				if (runThread != null)
					if (runThread.isAlive())
						return;  // if the game is active, ignore the click
				if (runThread==null)
				{
					runThread = new Thread(this); // the game is inactive, so let's play
				}
				// init the screen
				initialize( );

				// start the game
				if (!runThread.isAlive())
					runThread.start();
				gameOver = false;

			}
			else if (source==stopButton)  // they want to stop the game
			{
				gameOver = true;
				if (runThread!=null)
				{
					runThread.stop();
					runThread = null;
				}
			}
		}  // end of actionPerformed



		// thread to delay for the runButton
		// we do it all here so we have control of the buttons
		public void run()
		{
			try
			{
				while(true)
				{   		   


					if (!gameOver)
					{
						centerPanel.repaint();
					}

					setFocusable(true);
					requestFocus();


					Thread.currentThread().sleep(threadDelay);

				}
			} 
			catch(Exception e) {
			}
		}

		// start of keyTyped (KeyListener interface)
		public void keyTyped(KeyEvent e)
		{
		}  // end of keyTyped(KeyEvent e)

		// start of keyPressed (KeyListener interface)
		// this method handles the pressing of the 
		// arrow keys and the space bar
		public void keyPressed(KeyEvent e)
		{
			if (gameOver)
				return;

			int key = e.getKeyCode();
			String keyString = e.getKeyText(key);
			keyString = keyString.toUpperCase();

			if (keyString.equals("UP"))
			{
				keys[0]=true;
			}
			else if (keyString.equals("DOWN"))
			{
				keys[1]=true;
			}
			else if (keyString.equals("LEFT"))
			{
				keys[2]=true;
			}
			else if (keyString.equals("RIGHT"))
			{
				keys[3]=true;
			}
			else if (keyString.equals("SPACE"))
			{
				keys[4]=true;
			}
			else if(keyString.equals("W"))
			{
				keys[5]=true;
			}
			else if(keyString.equals("A"))
			{
				keys[6]=true;
			}
			else if(keyString.equals("S"))
			{
				keys[7]=true;
			}
			else if(keyString.equals("D"))
			{
				keys[8]=true;
			}
			else if(keyString.equals("Q"))
			{
				keys[9] = true;
			}

		}  // end of keyPressed(KeyEvent e)

		// start of keyReleased (KeyListener interface)
		// this method handles the releasing of the 
		// arrow keys and the space bar
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();
			String keyString = KeyEvent.getKeyText(key);
			keyString = keyString.toUpperCase();

			if (keyString.equals("UP"))
			{
				keys[0]=false;
			}
			else if (keyString.equals("DOWN"))
			{
				keys[1]=false;
			}
			else if (keyString.equals("LEFT"))
			{
				keys[2]=false;
			}
			else if (keyString.equals("RIGHT"))
			{
				keys[3]=false;
			}
			else if (keyString.equals("SPACE"))
			{
				keys[4]=false;
			}
			else if(keyString.equals("W"))
			{
				keys[5]=false;
			}
			else if(keyString.equals("A"))
			{
				keys[6]=false;
			}
			else if(keyString.equals("S"))
			{
				keys[7]=false;
			}
			else if(keyString.equals("D"))
			{
				keys[8]=false;
			}
			else if(keyString.equals("Q"))
			{
				keys[9] = false;
			}
		}  // end of keyReleased(KeyEvent e)



	} // end of centerPanel class



	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************
	// this is the panel for the game  (this is an inner class)
	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************
	class DrawPanel extends JPanel implements  MouseListener, MouseMotionListener
	{
		String testXY="X= Y=";
		boolean dragging = false;

		public DrawPanel()
		{
			super();
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void update(Graphics window)
		{
			paintComponent(window);
		}



		public void paintComponent(Graphics g)
		{
			super.paintComponent((Graphics2D)g);
			Graphics2D g2 = (Graphics2D) g;
			if(!gameOver)
				time = time - 0.05;
			//take a snap shop of the current screen and same it as an image
			//that is the exact same width and height as the current screen
			if(back==null)
				back = (BufferedImage)(createImage(getWidth(),getHeight()));

			//create a graphics reference to the back ground image
			//we will draw all changes on the background image
			Graphics gMemory = back.createGraphics();

			// clear the screen
			gMemory.setColor(Color.LIGHT_GRAY);
			gMemory.fillRect(0,0,getWidth(),getHeight());
			gMemory.setFont(font);
			// this will draw the x,y coordinate of the mouse
			// you may want to remove these 2 lines of code
			background.draw(gMemory);
			scoreboard.draw(gMemory);
			gMemory.setColor(Color.RED);
			gMemory.drawString(testXY,10,50);
			gMemory.setColor(Color.GREEN);





			/*
		ball.gravity();
		ball.moveDown();
		ball.moveRight();		
		ball.bounce(ground);
		playerone.gravity();
		playertwo.gravity();

			 */
			playerone.gravity();
			playerone.stand(ground);	
			if(keys[0])
			{
				playerone.jump();
				playerone.moveUp();
			}
			//else if(!keys[0])

			if(keys[1])
			{
				playerone.moveDown();
			}
			if(keys[2])
			{					
				playerone.moveLeft();
				playerone.setDirection(180);
			}
			if(keys[3])
			{		
				playerone.moveRight();
				playerone.setDirection(0);
			}/*
		if(keys[4]&&hold)
		{	

			ball.shoot(playerone, rim);
			//ball.setSpeedX((rim.getX()-playerone.getX())/45);
			playerone.setCurrentFilename("leshot.png");

		}
		else if (!keys[4])
		{
			release = true;
			playerone.setCurrentFilename("lebron.png");
		}

		if(keys[5]&&!playertwo.getVertical())
		{	
			jumpspot2=playertwo.getX();
			playertwo.jump();
		}
		//else if(!keys[5])
		playertwo.stand(ground);
		if(keys[6])
		{
			playertwo.moveLeft();
			playertwo.setDirection(180);
		}
		if(keys[8])
		{
			playertwo.moveRight();
			playertwo.setDirection(0);
		}
		if(keys[9]&&hold2)
		{

			ball.shoot(playertwo,rim2);
			playertwo.setCurrentFilename("theshot.png");

		}
		else if(!keys[9])
		{
			release2 = true;
			playertwo.setCurrentFilename("mike.png");
		}

		hold=true;
		hold2 = true;
		playerone.setRelease(release);
		playertwo.setRelease(release2);

		if(ball.intersects(backboard))
		{	
			ball.setSpeedX(-ball.getSpeedX());
			ball.setX(backboard.getX()-ball.getWidth()-1);
		}
		if(ball.intersects(backboard2))
		{
			ball.setSpeedX(-ball.getSpeedX());
			ball.setX(backboard2.getX()+ball.getWidth()+1);
		}

		if(ball.intersects(rim)&&ball.getSpeedX()>0)
		{
			ball.setX(rim.getX()-ball.getWidth());
			ball.setSpeedX(-ball.getSpeedX());

			ball.setSpeedY(-ball.getSpeedY());

		}
		if(ball.intersects(rim)&&ball.getSpeedX()<0)
		{
			ball.setX(rim.getX()+rim.getWidth());
			ball.setSpeedX(-ball.getSpeedX());

			ball.setSpeedY(-ball.getSpeedY());

		}
		if(ball.intersects(rim2)&&ball.getSpeedX()<0)
		{
			ball.setX(rim2.getX()+ball.getWidth());
			ball.setSpeedX(-ball.getSpeedX());

			ball.setSpeedY(-ball.getSpeedY());
		}
		if(ball.intersects(rim2)&&ball.getSpeedX()>0)
		{
			ball.setX(rim2.getX()-ball.getWidth());
			ball.setSpeedX(-ball.getSpeedX());

			ball.setSpeedY(-ball.getSpeedY());
		}

		ball.dribble(playerone);
		ball.dribble(playertwo);
		//made shot
			 */
			if(ball.intersects(hoop)&&ball.getY()<=hoop.getY()&&ball.getSpeedY()>0&&ball.getSpeedX()!=0&&ball.getX()>rim.getX()&&ball.getX()+ball.getWidth()<backboard.getX()&&playertwo.getX()+playertwo.getWidth()<backboard.getX())
			{
				//right rim
				release = true;
				hold = false;			


				ball.setSpeedXY(0,0);
				if(jumpspot1<threepoint.getX())
					score = score + 3;
				else
					score=score + 2;
				ball.setX(screenWidth-25);

			}
			playerone.inBounds();
			ground.draw(gMemory);
			threepoint.draw(gMemory);
			playerone.draw(gMemory);
			playertwo.draw(gMemory);   		

			rim.draw(gMemory);
			rim2.draw(gMemory);


			hoop.draw(gMemory);  		
			backboard.draw(gMemory);
			hoop2.draw(gMemory);

			backboard2.draw(gMemory);
			ball.draw(gMemory);


			// *** show the screen by copying the image to the graphics display ********
			g2.drawImage(back, null, 0, 0);	
		}  // end of public void paintComponent(Graphics g)


		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		// *** if a key is pressed, handle the movement here ***********************
		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		public void keyPressed(String keyString)
		{

		} // end of method public void keyPressed(String keyString)


		// ***** MouseListener interface methods *****


		// start of mouseClicked(MouseEvent e) (MouseListener interface)
		public void mouseClicked(MouseEvent e) 
		{
			int xPos = e.getX();
			int yPos = e.getY();
		}  // end of public void mouseClicked(MouseEvent e) 


		// start of mousePressed(MouseEvent e) (MouseListener interface)
		public void mousePressed(MouseEvent e) 
		{
			//if (ship==null)
			//return;
			//if (ship.getRect().contains(e.getX(),e.getY()))
			//{
			// startMouseDragX = e.getX();
			//startMouseDragY = e.getY();
			//	dragging = true;
			//}
		}  // end of public void mousePressed(MouseEvent e)



		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		// *** if the mouse is released, fire a bullet  ****************************
		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		// *************************************************************************
		public void mouseReleased(MouseEvent e) 
		{
			dragging = false;
			startMouseDragX = -1;
			startMouseDragY = -1;
			/*if (ship==null)
  		return;
    int xPos = e.getX();
    int yPos = e.getY();

    if (ship.getRect().contains(xPos,yPos))
    {

    } 
			 */
		}  // end of public void mouseReleased(MouseEvent e)


		public void mouseEntered(MouseEvent e) 
		{

		}  // end of public void mouseEntered(MouseEvent e)


		public void mouseExited(MouseEvent e) 
		{

		}  // end of public void mouseExited(MouseEvent e)


		// ***** MouseMotionListener interface methods *****


		public void mouseMoved(MouseEvent e) 
		{
			int xPos = e.getX();
			int yPos = e.getY();
			testXY = "X=" + xPos + "  Y=" + yPos;
		}  // end of public void mouseMoved(MouseEvent e) 


		public void mouseDragged(MouseEvent e) 
		{
			int xPos = e.getX();
			int yPos = e.getY();
			testXY = "X=" + xPos + "  Y=" + yPos;
			//if (ship==null)
			//return;

			boolean allowDragging = true;

			if (xPos < 0)
				allowDragging = false;
			else if (xPos >= this.getWidth())
				allowDragging = false;
			if (yPos < 0)
				allowDragging = false;
			else if (yPos >= this.getHeight())
				allowDragging = false;


			//} // end of if (dragging)

		}  // end of public void mouseDragged(MouseEvent e)

	} // end of class DrawPanel






} // end of class Project