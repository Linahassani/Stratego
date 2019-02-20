package help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Help Class with topics for help Screen used by MenuSingleton
 * @author kuras
 *
 */
public class HelpTopics {
	private final Map<String,String> topics=new HashMap<String,String>();
	private final String goals = "\n \n GOALS OF THE GAME:\n \n"
			+ "The goal of the game is to capture the flag of your opponent or all the opponent's pieces,"
			+ "while defending your own flag.";
	private final String place = "\n \n TO PLACE A PIECE:\n \n"
			+ "Select a piece in the panel to the right. \n \n"
			+ "Click on a square on the board where you would like to put the piece. \n \n"
			+ "Repeat until all you have a full setup.";
	private final String move = "\n \n MOVE A PIECE:\n \n"
			+ "Select a piece and possible move options will appear.\n \n "
			+ "Pieces cannot jump over another piece." ;
	private final String attack = "\n \n ATTACK YOUR OPPONENT:\n \n"
			+ "If your opponent has a piece within reach from one of yours, "
			+ "attack it by selecting the piece and clicking 'OK' when the popup message says 'Show attack'. \n \n"
			+ "If the attacking piece has equal rank as the attacked one, they are both removed. \n \n"
			+ "Otherwise, the piece with the lowest rank is removed. ";
	private final String online = "\n \n HOW TO PLAY ONLINE:\n \n"
			+ "Start the server and click on 'Multiplayer'. \n \n"
			+ "Choose one of the available players from the list. \n \n"
			+ "Send a game request, and wait until your opponent has accepted the invite. \n \n"
			+ "The game starts automatically as soon as the invitation is accepted.";
	private final String remove = "\n \n REMOVE A MISPLACED PIECE :\n \n"
			+ "If one of the pieces are misplaced during setup and you want to move it elsewhere, " 
			+ "right-click on the piece.";
	private final String piece = "\n \n PIECES AND SPECIAL SKILLS:\n \n"
			+ " Pieces move 1 square per turn, horizontally or vertically. \n"
			+ "Only the scout can move over multiple empty squares per turn. \n"
			+ "The pieces have different rank, marked with a number between 1-10, except the flag and the bombs. \n"
			+ "The pieces have the following rank and special skill: \n"
			+ "1. Spy(1x) When attacking the marshal, the spy defeats the higher ranked marshal. However, when the marshal attacks the spy, the spy loses. \n"
			+ "2. Scout (8x)\n" 
			+ "3. Miner (5x) The only piece that can defeat a bomb. \n" 
			+ "4. Sergeant (4x)\n" 
			+ "5. Lieutenant (4x)\n"  
			+ "6. Captain (4x)\n"  
			+ "7. Major (3x)\n" 
			+ "8. Colonel (2x)\n"  
			+ "9. General (1x)\n"  
			+ "10. Marshall (1x)\n"
			+ "Bomb (6x). Defeats every piece that tries to attack it, except the miner \n "
			+ "Flag (1x). Loses from every other piece.";
	
	
	/**
	 * adds topics to the help list
	 */
	public HelpTopics() {
		topics.put("Goals", goals);
		topics.put("Place", place);
		topics.put("Move", move);
		topics.put("Remove", remove);
		topics.put("Attack", attack);
		topics.put("Online", online);
		topics.put("Piece", piece);
	}
	
	/**
	 * 
	 * @param topic
	 * @return
	 */
	public String getTopic(String topic) {
		return topics.get(topic);
	}
	
	
}
