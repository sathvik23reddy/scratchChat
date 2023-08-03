package SSMC;

import java.io.PrintWriter;

public class User {
  String name;
  int port;
  PrintWriter cliPw;
  int flag;
//  -1->Torn down, ready to dispose
//  0->Registered [Present online users and validate connection req]
//  1->Connected with valid user [Connected, MessageFwd]

  User(String name, int port, PrintWriter cliPw) {
    this.name = name;
    this.port = port;
    this.cliPw = cliPw;
    this.flag = 0;
  }
}
