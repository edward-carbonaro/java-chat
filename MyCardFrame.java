import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;



public class MyCardFrame extends Frame{
	LoginPanel lp;
	ChatPanel cp;

	public MyCardFrame(){
		setLayout(new CardLayout());
		setTitle("Chat Program");
		setSize(500,500);
		lp = new LoginPanel(this);
		cp = new ChatPanel();
		add(lp, "login");
		add(cp, "chat");

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args){

		MyCardFrame mcf = new MyCardFrame();
	}
}

class LoginPanel extends Panel implements ActionListener{
	TextField tf;
	MyCardFrame mcf;
	Label logInLabel;

	public LoginPanel(MyCardFrame mcf){
		logInLabel = new Label("Enter Login Name: ");
		this.mcf = mcf;
		tf = new TextField(20);
		tf.addActionListener(this);
		add(logInLabel);
		add(tf);

	}
	public void actionPerformed(ActionEvent ae){
		mcf.cp.setUserName(tf.getText()); //call setUserName of chatpanel which is a member of mycardframe
		CardLayout cl = (CardLayout)(mcf.getLayout());
		cl.next(mcf);
		tf.setText("");

	}


}

class ChatPanel extends Panel implements ActionListener, Runnable{
	Label label1,label2;
	String userName;
	List myList;	
	TextField tf;
	TextArea ta;
	Button connect, disconnect, findUsers;
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	Thread t;
	String fromServer;


	public ChatPanel(){
		label1 = new Label("Chat Panel: ");
		label2 = new Label("Name will go here");

		add(label1);
		add(label2);

	
		setLayout(new BorderLayout());
		tf = new TextField();
		tf.setEditable(false);
		tf.addActionListener(this);
		add(tf, BorderLayout.SOUTH);
		ta = new TextArea();
		ta.setEditable(false);
		add(ta, BorderLayout.CENTER);
		Panel buttonPanel = new Panel();
		connect = new Button("Connect");
		connect.addActionListener(this);
		buttonPanel.add(connect);
		disconnect = new Button("Disconnect");
		disconnect.setEnabled(false);
		disconnect.addActionListener(this);
		buttonPanel.add(disconnect);
		findUsers = new Button("Find Users");
		findUsers.addActionListener(this);
		buttonPanel.add(findUsers);
		add(buttonPanel, BorderLayout.NORTH);		
		myList = new List(); 
		Panel userPanel = new Panel();
		add(userPanel, BorderLayout.WEST);
		userPanel.add(myList);
	}
	public void setUserName(String userName){
		this.userName = userName;
		label2.setText(getUserName());
	}
	public String getUserName(){
		return userName;
	}

	public List getMyList(){
		return myList;
	}

	public void addUser(String name){
		myList.add(name);
	}

	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == connect){
			try{
				s = new Socket("127.0.0.1", 8189);
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				pw = new PrintWriter(s.getOutputStream(), true);
				String fromTf = ("User Connected: " + userName);
				pw.println(fromTf);

			}catch(UnknownHostException uhe){
				System.out.println(uhe.getMessage());
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
			t = new Thread(this);
			t.start();
			tf.setEditable(true);
			connect.setEnabled(false);
			disconnect.setEnabled(true);
			String fromTf = ("User Connected: " + userName);
			pw.println(fromTf);
			myList.add(userName);
			
		}else if(ae.getSource() == disconnect){
			String fromTf = ("User Disconnected: " + userName);
			pw.println(fromTf);
			ta.append("");
			try{
				pw.close();
				br.close();
				s.close();
			}catch(IOException ioe){
				System.out.println(ioe.getMessage());
			}
			t = null;

			tf.setEditable(false);
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			myList.remove(userName);
		}else if(ae.getSource() == tf){
			String fromTf = (userName + ": " + tf.getText());
			pw.println(fromTf);
			tf.setText("");
		

		}else if(ae.getSource() == findUsers){
			pw.println("");

		}else {

				
		}
	}

	public void run(){
		fromServer = "";

		try{
			while((fromServer = br.readLine()) != null){

					ta.append(fromServer + "\n");
			}

		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
	}
	

}