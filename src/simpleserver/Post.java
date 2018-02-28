package simpleserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post extends Data {
    private static Map<Integer, Post> postidDict = new HashMap<>();
    private static Map<Integer, Post> useridDict = new HashMap<>();
    private static ArrayList<Post> allPosts = new ArrayList<>();
    //searching in terms of postid working now. Need to fix searching in terms of userid

    private int postid;
    private int userid;
    private String data;

    public Post(int postid, int userid, String data){
        this.postid = postid;
        this.userid = userid;
        this.data = data;
        postidDict.put(postid, this);
    }

    public static Post getPost(int postid){
        return postidDict.get(postid);
    }

    public static Post getUser(int userid){
        return useridDict.get(userid);
    }

    public Post(){
        allPosts.add(this);
    }

    public void register(){
        postidDict.put(postid, this);
    }

    public void registerUser(){
        useridDict.put(userid, this);
    }


    public static void loadAll(){
        for(int i = 0 ; i < allPosts.size(); i++){
            allPosts.get(i).register();
            allPosts.get(i).registerUser();
        }
    }


}