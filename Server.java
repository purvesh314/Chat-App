// A Java program for a Server
import java.net.*;
import java.io.*;
import java.sql.*;

class MultiServer extends Thread
{
	static String url="jdbc:mysql://10.10.13.175:3306/";
	static String dbName="te3157db";
	static String driver="com.mysql.jdbc.Driver";
	static String username="te3157";
	static String password="te3157";
	static Connection conn=null;
	static Statement st=null;
	static int port=0;
	//initialize socket and input stream
	private Socket client = null;
	private ServerSocket server = null;
	private DataInputStream in	 = null;

	private DataInputStream input=null;
	private DataOutputStream out=null;

	// constructor with port
	public MultiServer(Socket client,ServerSocket server)throws IOException, SQLException
	{
		// starts server and waits for a connection
		try
		{
			this.server=server;
			this.client=client;
			//server = new ServerSocket(port);
			//System.out.println("Server started");

			//System.out.println("Waiting for a client ...");

			Class.forName(driver);
			System.out.println("Connection : "+conn);
			conn=DriverManager.getConnection(url+dbName,username,password);
			System.out.println("Connection : "+conn);
			st=conn.createStatement();




			// String line = "";
			//
			// // reads message from client until "Over" is sent
			// while (!line.equals("Over"))
			// {
			// 	try
			// 	{
			// 		line = in.readUTF();
			// 		System.out.println("Client : "+line);
			//
			// 	}
			// 	catch(IOException i)
			// 	{
			// 		System.out.println(i);
			// 	}
			//
			// 	try
			// 	{
			// 		line=input.readLine();
			// 		out.writeUTF(line);
			// 	}
			// 	catch(IOException i)
			// 	{
			// 		System.out.println(i);
			// 	}
			// }
			// System.out.println("Closing connection");

			// close connection
			//socket.close();
			//in.close();
		}
		catch(Exception i)
		{
			System.out.println(i);
		}
	}

	public void run()
	{
		try
		{

			System.out.println("Client accepted");

			input=new DataInputStream(System.in);

			out=new DataOutputStream(client.getOutputStream());


			// takes input from the client socket
			in = new DataInputStream(
				new BufferedInputStream(client.getInputStream()));

			String name,password;
			name=in.readUTF();
			password=in.readUTF();
			System.out.println("Here");
			try {
				PreparedStatement ps=conn.prepareStatement("select * from users where name=? and password=?");
				ps.setString(1, name);
				ps.setString(2, password);

				ResultSet rs=ps.executeQuery();
				System.out.println("tHere");
				//ps.close();
				if(rs.next())
				{
					this.setName(name);
					System.out.println(this.getName());
					out.writeUTF("1");

					String message,receiver;
					message=in.readUTF();
					receiver=in.readUTF();
				}
				else
				{
					out.writeUTF("0");
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}

			client.close();
			in.close();
		}

	catch(Exception e)
	{}
	}
}

class Server
{
	public static void main(String[] args) {
	try
	{
			ServerSocket server = new ServerSocket(8000);
			while(true)
			{
				Socket client=server.accept();
				MultiServer thread=new MultiServer(client,server);
				thread.start();
			}
	}
	catch(Exception e)
	{}
	}

}
