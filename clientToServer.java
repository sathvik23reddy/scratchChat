package SSMC;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class clientToServer extends Thread {
  BufferedReader br;
  PrintWriter pw;

  clientToServer(BufferedReader br, PrintWriter pw) {
    this.br = br;
    this.pw = pw;
  }

  @Override
  public void run() {
    String s;
    try {
      while (true) {
        s = br.readLine();
        if (s != null)
          pw.println(s);
        else
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
