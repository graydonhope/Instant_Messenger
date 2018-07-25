import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;  // Output stream that flows from my computer
	private ObjectInputStream input; 	// Input stream """
	private ServerSocket server; 		// Server portion (comeback to)
	private Socket connection;			// Sockets are connections between my computer and someone elses

										// Constructor / GUI 
	public Server(){
		super("Instant Messenger");
		userText = new JTextField();	// Single line of text (editable)
		userText.setEditable(false); 	// Before you are connected to anyone, you cannot type
		userText.addActionListener(
			new ActionListener(){		// When someone adds something into the textbox and hits enter (Action), then actionPerformed method will be called.
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();	// JTextArea is a component that displays multiple lines of text
		add(new JScrollPane(chatWindow));
		setSize(300, 150);				// JScrollPane provides scrollable view of a component (JTextArea)
		setVisible(true);
	}

	public void startRunningServer(){	// Setting up and running server
		try{							// First argument is port number, second is backlog (how many people can wait to access)
			server = new ServerSocket(9876, 100);
			while(true){
				try{					// Connecting and having conversation
					waitForConnection();
					setupStreams();		// Setting up input / output streams
					duringChat();
				}						// end of stream has been reached unexpectedly during input
				catch(EOFException eofException){
					showMessage("\n Server ended the connection.");
				}
				finally{
					closeStream();		// End of Stream, close all sockets
				}
			}
		}
		catch(IOException ioException{	// Tracing code to try to identify where error occured (or in what method)
			ioException.printStackTrace();
		}
	}

										// Waiting for connection then gives prompt to signal connection and information (Ip address)
	public void waitForConnection() throws IOException{	
		showMessage("Waiting for someone to connect. . . \n");
		connection = server.accept();	// Waiting for socket to get connected
		showMessage("Now connected to " + connection.getInetAddress().getHostName());
	}

										// Get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();					// For limne above - Creating the pathway that allows us to connect to another computer (the computer that connection made)
										// Flush pushes through (to output user) any leftover data bytes that may not have been fully sent
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup. \n");
	}

	private void duringChat() throws IOException{
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}
			catch(ClassNotFoundException classNotFound){
				showMessage("\n Unreadable information type from user");
			}
		}
		while (!message.equals("CLIENT - END"))
	}
}