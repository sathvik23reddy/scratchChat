package SSMC;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class serverFromClient extends Thread {

  User currentUser;
  BufferedReader br;
  int portToConnect = -1;

  serverFromClient(BufferedReader br, User currentUser) {
    this.br = br;
    this.currentUser = currentUser;
  }

  @Override
  public void run() {
    String s;
    presentOnlineUsers(currentUser.cliPw);
    while (true) {
      try {
        s = br.readLine();
        if (s != null) {
          System.out.println(currentUser.name + ": " + s);
          if (currentUser.flag == 0) {
            if (s.toLowerCase().equals("q")) {
              deRegisterUser();
              break;
            }
            portToConnect = validateUserRequest(s);
            if (portToConnect == 0 || portToConnect == currentUser.port) {
              //Retry Message
              messageForward(currentUser.port, portToConnect == 0 ? "Try again with a Valid User" : "Can't connect to yourself!", -1);
              presentOnlineUsers(currentUser.cliPw);
            } else {
              messageForward(currentUser.port, "Successfully connected with " + userData.users.get(portToConnect).name + "[q to quit]", -1);
              notifyOtherUser(portToConnect, currentUser.name);
              logNewConnection(currentUser.port, portToConnect);
              currentUser.flag = 1;
            }
          } else if (currentUser.flag == 1) {
            if (s.toLowerCase().equals("q")) {
              tearDownConnection();
            }
            //Normal message forward
            messageForward(userData.liveConnections.get(currentUser.port), s, currentUser.port);
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void deRegisterUser() {
    userData.users.get(currentUser.port).cliPw.println("Session Terminated");
    userData.users.remove(currentUser.port);
  }

  private void tearDownConnection() {
    currentUser.flag = 0;
    int connectedCliPort = userData.liveConnections.get(currentUser.port);
    userData.users.get(connectedCliPort).flag = 0;
    userData.liveConnections.remove(connectedCliPort);
    userData.liveConnections.remove(currentUser.port);
    presentOnlineUsers(userData.users.get(connectedCliPort).cliPw);
    presentOnlineUsers(currentUser.cliPw);
  }

  private void presentOnlineUsers(PrintWriter cliPw) {
    if (userData.users.size() == 1) {
      cliPw.println("No active users at the moment, please try after some time");
      return;
    }
    cliPw.println("List of Online Users[q to quit]: ");
    for (User x : userData.users.values()) {
      if (x.port == currentUser.port) continue;
      cliPw.println(x.name);
    }
  }

  private void logNewConnection(int userAPort, int userBPort) {
    userData.liveConnections.put(userAPort, userBPort);
    userData.liveConnections.put(userBPort, userAPort);
    userData.users.get(userBPort).flag = 1;

  }

  private void notifyOtherUser(int clientPort, String currentUser) {
    messageForward(clientPort, "Successfully connected with " + currentUser + "[q to quit]", -1);
  }

  private int validateUserRequest(String s) {
    for (User x : userData.users.values()) {
      if (x.name.equals(s)) {
        return x.port;
      }
    }
    return 0;
  }

  private void messageForward(int clientPort, String message, int senderPort) {
    String finalMessage = (senderPort == -1 ? "Server" : userData.users.get(senderPort).name) + ": " + message;
    userData.users.get(clientPort).cliPw.println(finalMessage);
  }
}
