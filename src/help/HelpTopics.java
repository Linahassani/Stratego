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
	private final String objectives = "\n \n OBJECTIVES:\n \n"
			+ "Objectives of the game is to find a opponents flag, and or defeating all of the opponents pawns.\n \n"
			+ "Opponents make a move turn by turn, attacking other players pawns.";
	private final String place = "\n \n TO PLACE A PAWN:\n \n"
			+ "Select a pawn on the right hand panel and a position on the board, then click PLACE button.";
	private final String move = "\n \n TO MOVE A PAWN:\n \n"
			+ "Select a pawn, possible move options will appear.\n \n"
			+ "Select one of the possibilities." ;
	private final String attack = "\n \n TO ATTACK THE OPPONENT:\n \n"
			+ "Select one of your pawns, then select the opponents pawn that is in range.";
	private final String online = "\n \n TO PLAY ONLINE:\n \n"
			+ "Click on the Network button.\n \n"
			+ "Choose a player from the avaliable players on the list.\n \n"
			+ "Send a game request.\n \n"
			+ "Game starts automaticly If the opponent accepts your game invetation.";
	private final String remove = "\n \n TO REMOVE A PAWN:\n \n"
			+ "Select a pawn on the board, and click on the REMOVE button.";
	private final String pawns = "\n \n PAWNS AND SPECIAL SKILLS:\n \n"
			+ "Pawns have different range which can be seen after clicking on a pawn. The value shown on every pawn, "
			+ "shows its strenght.\n \n"
			+ "Some panws have special skills:\n \n"
			+ "Pawn with value 3 - destroys bombs.\n \n"
			+ "Panw with value 1 - destroys pawnm with value 10 (only when attacking).\n \n"
			+ "Bomb - destroys all other pawns except pawn with value 3";
	
	
	/**
	 * adds topics to the help list
	 */
	public HelpTopics() {
		topics.put("Objectives", objectives);
		topics.put("Place", place);
		topics.put("Move", move);
		topics.put("Remove", remove);
		topics.put("Attack", attack);
		topics.put("Online", online); 
		topics.put("Pawns", pawns);
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
