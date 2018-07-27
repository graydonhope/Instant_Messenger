import javax.swing.JFrame;

public class ClientTest{
	public static void main(String[] args){
		Client user;
		user = new Client("127.0.0.1"); 		// Local Host (Instead of using a server)
		user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		user.startRunningServer();
	}
}