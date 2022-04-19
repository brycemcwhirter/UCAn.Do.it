package addatude.app.server;

import addatude.serialization.AddatudeValidator;
import addatude.serialization.ValidationException;
import notifi.app.client.NoTiFiClient;
import notifi.app.server.NoTiFiServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCopy {


    /**
     * User
     *
     * This class identifies a specific
     * user. A User has a username and password
     */
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
    }


    /** The Main Method for the Server to Execute
     * @param args Port, Number of Threads, Password File
     * @throws IOException if an I/O error occurs
     * @throws ValidationException if a validation exception occurred
     */
    public static void main(String[] args) throws IOException, ValidationException, InterruptedException {

        // Get the Arguments
        if(args.length != 3){
            throw new IllegalArgumentException("Parameters: <Port> <Threads> <Password>");
        }


        // Set Values
        int serverPort = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);
        File passwordFile = new File(args[2]);





        //Reading Users and Passwords
        Map<Long, Server.User> userListMap = readUsers(passwordFile);




        // Create the Socket w/ A timeout of 60 seconds
        final ServerSocket serverSocket = new ServerSocket(serverPort);
        serverSocket.setSoTimeout(60*1000);


        // Create the logger without using Parent Handlers
        final Logger logger = Logger.getLogger("server.log");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.WARNING);


        for(int i = 0; i < threadPoolSize; i++) {
            Thread mainThread = new Thread(() -> {
                while (true) {
                    Socket clntSocket;

                    try {
                        clntSocket = serverSocket.accept();
                        AddatudeProtocol.handleClient(clntSocket, logger, userListMap);

                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Client Accept Failed", e);
                    } catch (ValidationException e) {
                        logger.log(Level.WARNING, "Validation Exception in Client", e);
                    }


                }
            });

            Thread threadNoTiFi = new Thread(() -> {
                while (true) {
                    DatagramSocket datagramSocket;

                    try {
                        datagramSocket = new DatagramSocket(serverSocket.getLocalSocketAddress());
                        NoTiFiServer.handleClient(datagramSocket, logger, serverPort);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Client Accept Failed", e);
                    }
                }
            });

            mainThread.start();
            threadNoTiFi.start();
            logger.info("Created & Started Thread = " + mainThread.getName());
            logger.info("Created & Started Thread = " + threadNoTiFi.getName());
        }





    }




    /**
     * This method is used to read users from a password File
     * @param passwordFile The password file containing the users
     * @return The map of UserIDs to Users
     * @throws IOException If an I/O error occurs
     * @throws ValidationException If the UserID, username, or password is invalid
     */
    private static HashMap<Long, Server.User> readUsers(File passwordFile) throws IOException, ValidationException {
        HashMap<Long, Server.User> userMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(passwordFile));

        String line;

        while((line = br.readLine()) != null){
            Scanner sc = new Scanner(line);
            sc.useDelimiter(":");
            long id = sc.nextLong();
            String name = sc.next();
            String pw = sc.next();
            AddatudeValidator.validUnsignedInteger("User ID", Long.toString(id));
            AddatudeValidator.validString("User Name", name);
            AddatudeValidator.validPassword(pw);
            userMap.put(id, new Server.User(name, pw));
        }

        return userMap;
    }



}
