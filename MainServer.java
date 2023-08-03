package SSMC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {
  //Port that all clients connect to initially
  static int initialPort = 5050;
  //Port that is assigned upon registration
  static int runningPort = 1000;

  public static void main(String args[]) throws IOException {

    ServerSocket parentSocket = new ServerSocket(initialPort);
    while (true) {

      //Initial handshake
      System.out.println("Awaiting connection...");
      Socket tempSocket = parentSocket.accept();
      System.out.println("Initial handshake with new client");

      //Temp communication for registration
      PrintWriter cliPw = new PrintWriter(tempSocket.getOutputStream(), true);
      BufferedReader cliBr = new BufferedReader(new InputStreamReader(tempSocket.getInputStream()));
      String userName = cliBr.readLine();
      System.out.println("New user " + userName + " just registered");

      //Informing client of port assigned to them
      cliPw.println(runningPort);

      //Actual connection
      ServerSocket socket = new ServerSocket(runningPort);
      Socket clientSocket = socket.accept();
      System.out.println("Client " + userName + " connected successfully");

      cliPw = new PrintWriter(clientSocket.getOutputStream(), true);
      cliBr = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      User currentUser = registerUser(userName, cliBr, runningPort, cliPw);

      //Thread to handle particular client
      Thread y = new serverFromClient(cliBr, currentUser);
      y.start();
      runningPort++;
    }
  }

  private static User registerUser(String userName, BufferedReader cliBr, int runningPort, PrintWriter cliPw) throws IOException {
    User newUser = new User(userName, runningPort, cliPw);
    userData.users.put(runningPort, newUser);
    return newUser;
  }
}
