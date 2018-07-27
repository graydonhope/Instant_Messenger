import javax.swing.JFrame;

public class ServerTest{
	public static void main(String[] args){
		Server ghope = new Server();
		ghope.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ghope.startRunningServer();
	}
}