import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Client
{
	private Socket socket=null;
	private DataInputStream input=null;
	private DataOutputStream out=null;
	private DataInputStream in	 = null;

	public Client(String address,int port)
	{

		try
		{
			socket=new Socket(address,port);
			System.out.println("Connected");

			input=new DataInputStream(System.in);

			out=new DataOutputStream(socket.getOutputStream());

		}
		catch(UnknownHostException u)
		{
			System.out.println(u);
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		try
		{
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		Scanner sc=new Scanner(System.in);
		String name,password;
		System.out.println("Name : ");
		name=sc.nextLine();
		System.out.println("Password : ");
		password=sc.nextLine();

		String result="0";

		try
		{
			out.writeUTF(name);
			out.writeUTF(password);

			result=in.readUTF();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		String line="";
		System.out.println("result="+result);
		if(result.equals("1"))
		{
			System.out.println("Logged in successfully");
			System.out.println("Name : "+name);
			String message,receiver;
			System.out.println("Message : ");
			message=sc.readLine();
			System.out.println("Receiver : ");
			receiver=sc.readLine();

			out.writeUTF(message);
			out.writeUTF(receiver);
		}
		else
		{
			System.out.println("Wrong Credentials");
			try
			{
				socket.close();
			}
			catch(IOException e)
			{}
			return;
		}


		Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = sc.nextLine();

                    try {
                        // write on the output stream
                        out.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = in.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
	}



	public static void main(String[] args) {
	 	Client client=new Client("127.0.0.1",8000);
	 }
}
