import javax.swing.*; //JPanel/containers/scrollpane
import java.awt.*; //Dimension/colour/graphics + Point
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class tronGUI extends JPanel
						implements KeyListener
{
/* TODO 
 - keylistener implement arrows and WASD for 2 players
paint each player
add whie lines around the outside
set Banner colours according to playrs

*/

	tronGame game;
	Player player1,player2;
	Frame f;
	int bannerHeight = 30;
	int gameWidth, gameHeight;
	int gridXNum = 50;
	int gridYNum = 50;
	int cellWidth, cellHeight;

	//COLOURS
	Color backgroundColour = Color.BLACK;
	Color gridColour = Color.WHITE;
	Color p1Colour = Color.CYAN;
	Color p2Colour = Color.YELLOW;
	Color headColour = Color.WHITE;

	public tronGUI(tronGame game){
		this.game = game;
		this.player1 = game.player1;
		this.player2 = game.player2;

		f = new Frame(bannerHeight);
		initialiseGUI(f);
	}

	void initialiseGUI (Frame f){
		//some visual constants
		setOpaque(true);
		setBackground(backgroundColour);
		setPreferredSize(new Dimension(f.gameWidth, f.gameHeight) );

		gameWidth = f.gameWidth;
		gameHeight = f.gameHeight;
		//gridXNum = 50;
		//gridYNum = 50;
		cellWidth = gameWidth / gridXNum;
		cellHeight = gameHeight / gridYNum;
		
		//get wet for input
		addKeyListener(this);
		setFocusable(true);
		
		f.initialise(this);
	}

	public void paint(Graphics g){
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));

		paintGrid(g);
		paintBanner(g);
		paintPlayers(g);

	}

	//g.drawLine(x1,y1,x2,y2)
	public void paintGrid(Graphics g){
		g.setColor(gridColour);
		/*
		//draw vertical lines
		for(int i = 0; i<=gridXNum+1; i++){
			g.drawLine(i*cellWidth, bannerHeight, i*cellWidth, gameHeight + bannerHeight + cellHeight);
		}
		//draw right-mostvertical line, cant explain why
		//g.drawLine(gameWidth-1, bannerHeight, gameWidth-1, gameHeight + bannerHeight);

		//draw horizotnal lines
		for(int j=0; j<=gridYNum; j++){
			g.drawLine(0,j*cellHeight + bannerHeight, gameWidth, j*cellHeight + bannerHeight); 
		}
		//the bottom line, compensating for banner
		g.drawLine(0,gameHeight + bannerHeight + cellHeight, gameWidth, gameHeight + bannerHeight + cellHeight);
		*/
		//draw top line
		g.drawLine(0,bannerHeight, gameWidth, bannerHeight);
	}

	//g.drawString(text, x,y) top left corner
	public void paintBanner(Graphics g){
		//Player 1 score
		g.setColor(p1Colour);
		g.drawString("Player 1: " + game.player1.score , 2, 22);

		//Player 2 score
		g.setColor(p2Colour);
		g.drawString("Player 2: " + game.player2.score, gameWidth - 125, 22);

		/*
		if(game.paused == true){
			g.drawString("PAUSED!", 50,15);
		}*/
	}

	//g.fillRect(x,y,width,height)
	public void paintPlayers(Graphics g){

		//paint Player1
		g.setColor(p1Colour);
		for(Point cell : game.player1.trace){
			g.fillRect(cell.x * cellWidth, cell.y * cellHeight + bannerHeight, cellWidth, cellHeight);
		}
		//paint head after so not painted over
		g.setColor(headColour);
		g.fillRect(game.player1.head.x * cellWidth, game.player1.head.y * cellHeight + bannerHeight, cellWidth, cellHeight);
		
		//System.out.println(game.player1.trace);
		
		//paint Player2
		g.setColor(p2Colour);
		for(Point cell : game.player2.trace){
			g.fillRect(cell.x * cellWidth, cell.y * cellHeight + bannerHeight, cellWidth, cellHeight);
		}

		//paint head after so not painted over
		g.setColor(headColour);
		g.fillRect(game.player2.head.x * cellWidth, game.player2.head.y * cellHeight + bannerHeight, cellWidth, cellHeight);
	}
	//INPUT
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		//Player1 movement
			case KeyEvent.VK_W: //direction 0
				//System.out.println("W!");
				if(game.player1.direction != 2) {
					game.player1.direction = 0;
				}
			break;
			case KeyEvent.VK_A: //direction 3
				//System.out.println("A!");
				if(game.player1.direction != 1) {
					game.player1.direction = 3;
				}
			break;
			case KeyEvent.VK_S: //direction 2
				//System.out.println("S!");
				if(game.player1.direction != 0) {
					game.player1.direction = 2;
				}
			break;
			case KeyEvent.VK_D: //direction 1
				//System.out.println("D!");
				if(game.player1.direction != 3) {
					game.player1.direction = 1;
				}
			break;
		//Player 2 movement
			case KeyEvent.VK_UP: //direction 0
				//System.out.println("UP!");
				if(game.player2.direction != 2) {
					game.player2.direction = 0;
				}
			break;
			case KeyEvent.VK_LEFT: //direction 3
				//System.out.println("LEFT!");
				if(game.player2.direction != 1) {
					game.player2.direction = 3;
				}
			break;
			case KeyEvent.VK_DOWN: //direction 2
				//System.out.println("DOWN!");
				if(game.player2.direction != 0) {
					game.player2.direction = 2;
				}
			break;
			case KeyEvent.VK_RIGHT: //direction 1
				//System.out.println("RIGHT!");
				if(game.player2.direction != 3) {
					game.player2.direction = 1;
				}
			break;
			case KeyEvent.VK_ENTER:
				game.gameInPlay = !game.gameInPlay;
			break;
			case KeyEvent.VK_SPACE:
				game.resetGame();
			break;
			case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
	}
	//UNUSED listener functions
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}

}

//Banner displays each players score, wins vs wins
class Frame extends JFrame {
	static int gameWidth = 500; 
	static int gameHeight = 500; 

	public Frame(int bannerHeight){
		setTitle("TRON");
		setSize(gameWidth + 1, gameHeight + 2*bannerHeight + 3); //the 1 allows for rightmost gridline
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initialise(tronGUI guiPanel)
	{
		setLayout(new GridLayout(1,1));
		add(guiPanel);
		//pack();
		//guiPanel.northLimit = getInsets().top;
		setLocationRelativeTo(null);
		setVisible(true);
		System.out.println("gui initialised");
	}
}