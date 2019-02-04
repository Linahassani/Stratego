package highscore;

public class Test {
	
	
    public static void main(String[] args) {
        HighScoreList list = new HighScoreList();
        list.addScore("Lukasz",2);
        list.addScore("André",2);
        list.addScore("Anders",1);
        list.addScore("Henrik",1);
        list.addScore("Henrik",2);
        System.out.print(list.getHighscore());
        
    }
}