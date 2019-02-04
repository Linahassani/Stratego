package server;

public class StartUp {
	public static void main(String[] args) {
		Controller controller=new Controller();
		new Thread(new Server(controller)).start();
	}

}
