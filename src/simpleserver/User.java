package simpleserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends Data{

    private  static Map<Integer, User> useridDict = new HashMap<>();
    private static ArrayList<User> allUsers = new ArrayList<>();

    private  String username;

    private  int userid;

    public User(){
        allUsers.add(this);
    }

    public User(String username, int userid){
        this.username = username;
        this.userid = userid;
        useridDict.put(userid, this);
    }

    public static User getUser(int userid){
        return useridDict.get(userid);
    }

    public void register(){
        useridDict.put(userid, this);
    }

    public static void loadAll(){
        for(int i = 0 ; i < allUsers.size(); i++){
            allUsers.get(i).register();
        }
    }
}





