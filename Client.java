import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	private Color colour = new Color(0, 0, 0, 100);

	public Client(String host){
		super("Client User");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendData(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
		chatWindow.setEditable(false);
		//chatWindow.setBackground(colour);
	}

	public void startRunningServer(){
		try{
			connectToServer();
			setupStreams();
			duringChat();
		}
		catch(EOFException eofException){
			showMessage("\n Client Terminated the Connection");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			closeStream();
		}
	}

	private void connectToServer() throws IOException{
		showMessage("Attempting to connect. . . ");
		connection = new Socket(InetAddress.getByName(serverIP), 9876);
		showMessage("Connected to Local Host: " + connection.getInetAddress().getHostName());
	}

	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now connected \n");
	}

	private void duringChat() throws IOException{
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException classNotFoundException){
				showMessage("\n Unreadable Object Type");
			}

		}
		while(!message.equals("SERVER - END"));
	}

	private void closeStream(){
		showMessage("\n Closing Connections. . . \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	private void sendData(String message){
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - " + message);
		}
		catch(IOException ioException){
			chatWindow.append("An error occured trying to send this message");
		}
	}

	private void showMessage(final String message){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(message);
				}
			}
		);
	}

	private void ableToType(final boolean ability){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(ability);
				}
			}
		);
	}
}