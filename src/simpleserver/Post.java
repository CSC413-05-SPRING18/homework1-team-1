package simpleserver;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private final static Map<Integer, Post> postidDict = new HashMap<>();

    private final int postid;
    private final int userid;
    private final String data;

    public Post(int postid, int userid, String data){
        this.postid = postid;
        this.userid = userid;
        this.data = data;
        postidDict.put(postid, this);
    }

    public static Post getPost(int postid){
        return postidDict.get(postid);
    }
}