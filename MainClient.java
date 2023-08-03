package SSMC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainClient {
  static final String serverAddress = "localhost";
  static final int port = 5050;

  public static BufferedReader cliBr = new BufferedReader(new InputStreamReader(System.in));

  public static void main(String[] args) throws IOException {
    System.out.println("-x-x-x-x-x-x-x-x-x-x-x-x-x------Welcome to ScratchChat------x-x-x-x-x-x-x-x-x-x-x-x-x-");
    //Temp connection for signup
    Socket socket = new Socket(serverAddress, port);
    System.out.println("Successful handshake with server");
    PrintWriter servPw = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader servBr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    getUserDetails(servPw, cliBr);

    //Get and setup actual connection
    String actualPort = servBr.readLine();
    if (actualPort != null) {
      System.out.println("Server assigned port number: " + actualPort);
      socket.close();
      socket = new Socket(serverAddress, Integer.parseInt(actualPort));
      servBr.close();
      servBr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      servPw = new PrintWriter(socket.getOutputStream(), true);
    }

    //Thread to post messages
    Thread x = new clientToServer(cliBr, servPw);
    x.start();

    //Thread to read messages from server
    String s;
    while ((s = servBr.readLine()) != null) {
      System.out.println(s);
    }
  }

  private static void getUserDetails(PrintWriter servPw, BufferedReader cliBr) throws IOException {
    String userName;
    while (true) {
      System.out.print("Enter your name: ");
      userName = cliBr.readLine();
      if (userName != null) break;
    }
    servPw.println(userName);
  }


}
