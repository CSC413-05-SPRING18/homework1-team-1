package simpleserver;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


class SimpleServer {

  public static void main(String[] args) throws IOException { //change main to run
    ServerSocket ding;
    Socket dong = null;
    String resource = null;
    String mainRequestLine = null;
    //json
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
          mainRequestLine = line;

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

        //Parsing
        String lineParts[] = mainRequestLine.split(" ");
        String resourceString = lineParts[1];
        String lineParts2[] = resourceString.split("\\?");
        System.out.println(lineParts2.length);
        String resourceString2 = null;
        String lineParts3[] = null;
        int id = -1;
        if (lineParts2.length == 2) {
          resourceString2 = lineParts2[1];
          lineParts3 = resourceString2.split("=");
        }


        // Body of our response
        String param2 = null;

        String param1 = lineParts2[0]; //replace param1 with the actual parsed parameter later
        if (lineParts2.length == 2) {
          param2 = lineParts3[0]; //replace param2 with the actual parsed parameter later
        }
        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.setStatus(ResponseBuilder.StatusCode.OK);


        //if possible change all these codes to switch statements.
        if (param1.equals("/user")) {
          if (param2.equals("userid")) {
            id = Integer.parseInt(lineParts3[1]);
            responseBuilder.setData(User.getUser(id));
          } else {
            responseBuilder.setData(users);
          }
        } else if (param1.equals("/posts")) {
          if (param2.equals("postid")) {
            id = Integer.parseInt(lineParts3[1]);
            responseBuilder.setData(Post.getPost(id));
          } else if (param2.equals("userid")) {
            id = Integer.parseInt(lineParts3[1]);
            responseBuilder.setData(Post.getUser(id));
          } else {
            responseBuilder.setData(posts);
          }
        }
        else
          {
            //maybe make an error case?
          }



          Response response = responseBuilder.build();
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
