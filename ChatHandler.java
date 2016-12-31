import java.io.*;
import java.net.*;
import java.util.*;

public class ChatHandler extends Thread{
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	String temp;
	ArrayList <ChatHandler>handlers;
	String userName;
	//ArrayList userList = new ArrayList<String>();
	List userList = new ArrayList<String>();





	//ChatHandler(){}
	public ChatHandler(Socket s, ArrayList <ChatHandler>handlers){
		this.s = s;
		this.handlers = handlers;
	}

	public String getUserName(){
		return userName;
	}


	public List getUserList(){
		List newUserList = new ArrayList<Object>();
		for (ChatHandler ch : handlers)
		{
			newUserList.add(ch.getUserName());
		}
		return newUserList;
	}

	public void run(){
		try{
			handlers.add(this);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream(), true);
			temp = "";
			userName = br.readLine().substring(15);


			//System.out.println(handlers+ " these are the handlers");

			//System.out.println(userName+ " this is the user name");

			for (ChatHandler ch : handlers){
				userList.add(ch.getUserName());
				//System.out.println(ch.getUserName()+" this is getUserName method on ch");

			}

			for (Object names : userList){
				System.out.println(names + " this is iterating through userList and printing their names");

			}


			//pw.println(getUserList() + " Are in lobby");



			System.out.println(userList + "  this is the list");

			while((temp = br.readLine()) != null){
				for (ChatHandler ch : handlers){
					ch.pw.println(temp);
					//ch.pw.println(userList + " this is printing userList variable");
					//ch.pw.println(getUserList() + " Are in lobby");
				}
				for (ChatHandler ch : handlers){
					if (temp.length() <= 1){
						ch.pw.println(getUserList() + " Are in lobby");
					}
				}

			System.out.println(temp);
			}

			while((temp = br.readLine()) != null){
							for (ChatHandler ch : handlers){
								//ch.pw.println(temp);
								//ch.pw.println(userList + " this is printing userList variable");
								ch.pw.println(getUserList() + " Are in lobby");

							}
						System.out.println(temp);
			}





		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}finally{
			handlers.remove(this);
		}
	}
}