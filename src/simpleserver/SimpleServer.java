package simpleserver;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


class SimpleServer {

  public static void main(String[] args) throws IOException {
    ServerSocket ding;
    Socket dong = null;
    String resource = null;
    Gson gson = new Gson();
    BufferedReader br;
    User[] users = null;
    Post[] posts = null;

    try {
      br = new BufferedReader(new FileReader("src/data.json"));
      JsonParser jsonParser = new JsonParser();
      JsonObject obj = jsonParser.parse(br).getAsJsonObject();
      //make a new usr class

      users = gson.fromJson(obj.get("users"), User[].class);
      User.loadAll();

      ///****************
      posts = gson.fromJson(obj.get("posts"), Post[].class);
      Post.loadAll();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      ding = new ServerSocket(1299);
      System.out.println("Opened socket " + 1299);
      while (true) {

        // keeps listening for new clients, one at a time
        try {
          dong = ding.accept(); // waits for client here
        } catch (IOException e) {
          System.out.println("Error opening socket");
          System.exit(1);
        }

        InputStream stream = dong.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        try {

          // read the first line to get the request method, URI and HTTP version
          String line = in.readLine();
          System.out.println("----------REQUEST START---------");
          System.out.println(line); //parse and return method and parameters
          //take the methods and returns and find out which one of those to call.
          //last person does the builder.
          // read only headers
          line = in.readLine();
          while (line != null && line.trim().length() > 0) {
            int index = line.indexOf(": ");
            if (index > 0) {
              System.out.println(line);
            } else {
              break;
            }
            line = in.readLine();
          }
          System.out.println("----------REQUEST END---------\n\n");
        } catch (IOException e) {
          System.out.println("Error reading");
          System.exit(1);
        }

        BufferedOutputStream out = new BufferedOutputStream(dong.getOutputStream());
        PrintWriter writer = new PrintWriter(out, true);  // char output to the client

        // every response will always have the status-line, date, and server name
        writer.println("HTTP/1.1 200 OK");
        writer.println("Server: TEST");
        writer.println("Connection: close");
        writer.println("Content-type: application/json");
        writer.println("");


        // Body of our response

        //Commented out to test POST
        //String param1 = "users"; //replace param1 with the actual parsed parameter later
        //String param2 = "userid"; //replace param2 with the actual parsed parameter later


        //**********TESTING POST**************
        String param3 = "posts"; //replace param1 with the actual parsed parameter later
        String param4 = "postid";


          ResponseBuilder responseBuilder = new ResponseBuilder();
          responseBuilder.setStatus(ResponseBuilder.StatusCode.OK);
          //Commented out so that I could test post below
        /*  if (param1 == "users") {
            if(param2 == "userid") {
              int id = 234;
              responseBuilder.setData(User.getUser(id));
          }
            else
            {
              responseBuilder.setData(users);
            }
          }
          Response response = responseBuilder.build();
          */

          //***************TESTING POST********************
        if (param3 == "posts") {
          if(param4 == "postid") {
            int id = 0;
            responseBuilder.setData(Post.getPost(id));
          }
          else
          {
            responseBuilder.setData(users);
          }
        }
        Response response = responseBuilder.build();


          //Response response = new Response("Ok", users.length, users);
        //writer.println(gson.toJson(users[1]));
        //writer.println(gson.toJson(User.getUser(234)));
        writer.print("\"status\": ");
        writer.println(gson.toJson(response.getStatusString()));
        writer.print("\"entries\": ");
        writer.println(gson.toJson(response.getEntries()));
        writer.print("\"data\": ");
        writer.println(gson.toJson(response.getData()));


        dong.close();
      }
    } catch (IOException e) {
      System.out.println("Error opening socket");
      System.exit(1);
    }
  }
}
