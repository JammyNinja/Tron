import javax.swing.*; // timer
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.ArrayList;

public class tronGame implements ActionListener{
/* TODO
Export this motherfucker
implement score limit? have i?
stop it going back on self if going left then quick upright

Unhardcode player start points

*/
	static tronGame game;
	static tronGUI gui;
	Timer t;
	static Player player1, player2;
	static Boolean gameInPlay;

	int scoreLimit = 3;

	public static void main(String args[]){
		gameInPlay = false;
		System.out.println("TRON!");
		game = new tronGame();
		gui = new tronGUI(game);

		newGame();
	}

	static void newGame(){
		player1 = new Player(1, "Lou", gui);
		player2 = new Player(2, "You", gui);
		gameInPlay = true;
		game.t = new Timer(100,game);
		game.t.start();
	}

	//called per timestep
	public void actionPerformed(ActionEvent e) {
		if(gameInPlay){
			//move players
			player1.move();
			player2.move();

			//check if either suicide
			Boolean p1lose = player1.checkSuicide();
			Boolean p2lose = player2.checkSuicide();
			//if one overlaps other, lose
			int overlap = playersOverlapCheck();

			//Deal with gathered win/loss info
			//DRAW (3)
			if((p1lose && p2lose) 		|| 
				p1lose && overlap == 2 	||
				p2lose && overlap == 1 ||
				overlap == 3			 ) 

			{	roundOver(3);	}
			//P1 WIN (1)
			else if ( p2lose || overlap == 2 ) {roundOver(1);}
			//P2 WIN (2)
			else if ( p1lose || overlap == 1 ) {roundOver(2);}
		}

		gui.repaint();
	}
	public void roundOver(int winner){
	//sstop timer?
		gameInPlay = false;

		if(winner == 1) player1.score++;
		if(winner == 2) player2.score++;
		if(winner == 3) System.out.println("DRAW! - go again.");

		//check if score limit reached

		//if not reset game
		//resetGame();
	}

	public void resetGame(){
		player1.resetPosition();
		player2.resetPosition();
		gameInPlay = true;
	}

	//returns 0 if nothing happened, 3 if draw
	//1 if player 1 lost (p2 won) 
	//2 if player 2 lost (p1 won)
	public int playersOverlapCheck(){
		Boolean p1win = false, p2win = false;
		
		//check player 1 doesnt overlap with player 2
		for(Point body2 : player2.trace){
			if(body2.equals(player1.head)) {
				System.out.println("Player 1 hit Player 2 and loses");
				p2win = true;
			}
		}

		//check player 1 doesnt overlap with player 2
		for(Point body1 : player1.trace){
			if(body1.equals(player2.head)) {
				System.out.println("Player 2 hit Player 1 and loses");
				p1win = true;
			}
		}

		//return 
		if (p1win && p2win) return 3;
		else if(p1win) 		return 2;
		else if(p2win) 		return 1;
		else 				return 0;
	}

}

class Player {
	String name;
	int score;
	int playerNum;
	Point head;
	ArrayList<Point> trace; //size(),add(),get(i), remove()
	int direction; //0 north, 1 east, 2 south, 3 west
	tronGUI gui; //for checking out of bounds

	Point newHead;

	public Player(int playerNum, String name, tronGUI gui){
		this.playerNum = playerNum;
		this.name = name;
		this.gui = gui;
		score = 0;

		resetPosition();
		System.out.println("Player " + name + " initialised.");

	}

	public void move(){
		System.out.println("moving " +name);
		newHead = new Point();
		newHead.x = head.x;
		newHead.y = head.y;
		switch(direction){
			case 0: //north
				newHead.y--;
			break;
			case 1: //east
				newHead.x++;
			break;
			case 2: //south
				newHead.y++;
			break;
			case 3: //west
				newHead.x--;
			break;
		}
		trace.add(newHead);
		head = newHead;
		//System.out.println(trace);
	}

	public void resetPosition(){
		trace = new ArrayList<Point>();

		switch(playerNum){
			case 1:
				head = new Point(10,10);
				direction = 2;
			break;
			case 2:
				head = new Point(40,40);
				direction = 0;
			break;
			default:
				System.out.println("ERROR. Who the hell do you think are? ");
			break;
		}
		trace.add(head);
	}
	//returns true if suicide, false if not
	public Boolean checkSuicide(){
		//check not out of bounds
		if(head.x < 0 || head.x > gui.gridXNum-1){
			//System.out.print(head);
			System.out.println("Player " + playerNum + " suicide! out of horiontal bounds");
			return true;
		}
		if(head.y < 0 || head.y > gui.gridYNum){
			//System.out.print(head);
			System.out.println("Player " + playerNum + " suicide! out of vertical bounds");
			return true;
		}
		
		//check not overlap with self
		for(Point bodypart : trace){
			//value comparison true && pointer comparison false
			if(head.equals(bodypart) && bodypart != head) {
				System.out.println("Player " + playerNum + " suicide! overlap");
				return true;
			}
		}
		return false;
	}

}