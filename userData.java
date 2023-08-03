package SSMC;

import java.util.concurrent.ConcurrentHashMap;

public class userData {
  public static final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
  public static final ConcurrentHashMap<Integer, Integer> liveConnections = new ConcurrentHashMap<>();
}