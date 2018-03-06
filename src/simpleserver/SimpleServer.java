package simpleserver;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


class SimpleServer {

  private static SimpleServer instance = null;

  private SimpleServer(){

  }

  public static SimpleServer getInstance() {
    if (instance == null) {
      instance = new SimpleServer();
    }
    return instance;
  }

  public static void run() { //change main to run
    ServerSocket ding;
    Socket dong = null;
    String resource = null;
    String mainRequestLine = null;
    Gson gson = new Gson();
    BufferedReader br;
    User[] users = null;
    Post[] posts = null;

    try {
      br = new BufferedReader(new FileReader("src/data.json"));
      JsonParser jsonParser = new JsonParser();
      JsonObject obj = jsonParser.parse(br).getAsJsonObject();
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


        Parse parseUrl = new Parse(mainRequestLine);

        int id = parseUrl.getId();
        boolean doWeHaveSecondParameter = parseUrl.doWeHaveSecondParameter();
        boolean isOurIntValid = parseUrl.isOurIntValid();

        ResponseBuilder responseBuilder = new ResponseBuilder();
        responseBuilder.setStatus(ResponseBuilder.StatusCode.OK);




            if( parseUrl.getFirstParameter().equals("/user")) {
              if (doWeHaveSecondParameter) {
                firstParameterOptions(doWeHaveSecondParameter, parseUrl, isOurIntValid, responseBuilder, users, id);

              } else {
                responseBuilder.setData(users);

              }

            }
          if (parseUrl.getFirstParameter().equals("/posts"))
          {
                  if(doWeHaveSecondParameter)
                  {
                    if(parseUrl.getSecondParameter().equals("userid"))
                    {
                      if (isOurIntValid) {
                        responseBuilder.setData(Post.getUser(id));
                      }
                      if (!isOurIntValid) {
                        responseBuilder.setStatus(ResponseBuilder.StatusCode.ERROR_GENERAL);
                      }
                    }
                    else {
                      firstParameterOptions(doWeHaveSecondParameter, parseUrl, isOurIntValid, responseBuilder, users, id);
                    }
                  }
                    else{
                      responseBuilder.setData(posts);

                   }
           }



          Response response = responseBuilder.build();
        writer.print("{\"status\":");
        writer.print(gson.toJson(response.getStatusString()));
        writer.print(",\"entries\":");
        writer.print(gson.toJson(response.getEntries()));
        writer.print(",\"data\":");
        writer.print(gson.toJson(response.getData()));
        writer.println("}");

        dong.close();
      }
    } catch (IOException e) {
      System.out.println("Error opening socket");
      System.exit(1);
    }
  }

  public static void firstParameterOptions(boolean doWeHaveSecondParameter, Parse parseUrl, boolean isOurIntValid, ResponseBuilder responseBuilder, User[] users, int id)
  {
    if(doWeHaveSecondParameter) {
      if (parseUrl.getSecondParameter().equals("userid")) {
        if (isOurIntValid) {
          responseBuilder.setData(User.getUser(id));
        }
        if (!isOurIntValid) {
          responseBuilder.setStatus(ResponseBuilder.StatusCode.ERROR_GENERAL);
        }


      }

      if (parseUrl.getSecondParameter().equals("postid")) {
        if (isOurIntValid) {
          responseBuilder.setData(Post.getPost(id));
        }
        if (!isOurIntValid) {
          responseBuilder.setStatus(ResponseBuilder.StatusCode.ERROR_GENERAL);
        }
      }



    }


  }




  public static void main(String[] args){

    SimpleServer instance=SimpleServer.getInstance();

    instance.run();
  }
}
