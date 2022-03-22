package addatude.app.server;

import addatude.serialization.MessageInput;
import addatude.serialization.ValidationException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {


    static class User{
        private String userName;
        private String password;

        User(String userName, String password){
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword(){
            return password;
        }
    }






    public static void main(String[] args) throws IOException {

        // Get the Arguments
        if(args.length != 3){
            throw new IllegalArgumentException("Parameters: <Port> <Threads> <Password>");
        }



        // Set Values
        int serverPort = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);

        File passwordFile = new File(args[2]);



        //Reading Users and Passwords
        Map<Long, User> userListMap = readUsers(passwordFile);




        // Create the Socket w/ A timeout of 60 seconds
        final ServerSocket serverSocket = new ServerSocket(serverPort);
        serverSocket.setSoTimeout(60*1000);


        // Create the logger without using Parent Handlers
        final Logger logger = Logger.getLogger("server.log");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.WARNING);


        for(int i = 0; i < threadPoolSize; i++){
            Thread thread = new Thread(() -> {
                while(true){
                    Socket clntSocket = null;
                    try{
                        clntSocket = serverSocket.accept();
                        AddatudeProtocol.handleAddatudeClient(clntSocket, logger, userListMap);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Client Accept Failed", e);
                    }


                }
            });
            thread.start();
            logger.info("Created & Started Thread = " + thread.getName());
        }



    }

    private static HashMap<Long, User> readUsers(File passwordFile) throws IOException {
        HashMap<Long, User> userMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(passwordFile));

        String line;

        while((line = br.readLine()) != null){
            Scanner sc = new Scanner(line);
            sc.useDelimiter(":");
            long id = sc.nextLong();
            String name = sc.next();
            String pw = sc.next();
            userMap.put(id, new User(name, pw));

        }






        return userMap;
    }




}
