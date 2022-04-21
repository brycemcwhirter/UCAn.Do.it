/*

 Author: Bryce McWhirter
 Assignment: Program 6
 Class: Data Communications

 */

package addatude.app.server;


import addatude.serialization.AddatudeValidator;
import addatude.serialization.ValidationException;
import notifi.app.server.NoTiFiServer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The Addatude Server
 */
public class Server {


    /**
     * User
     *
     * This class identifies a specific
     * user. A User has a username and password
     */
    static class User{
        private final String userName;
        private final String password;

        User(String userName, String password){
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }
    }



    // The Logger for Logging Information
    public static Logger logger = Logger.getLogger("AddatudeAndNotifiServerLog");


    // The maximum size for a UDP packet
    private static final int MAX_READ_SIZE_UDP = 65507;



    /** The Main Method for the Server to Execute
     * @param args Port, Number of Threads, Password File
     * @throws IOException if an I/O error occurs
     * @throws ValidationException if a validation exception occurred
     */
    public static void main(String[] args) throws IOException, ValidationException {

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




        // Create the TCP Socket w/ A timeout of 60 seconds
        ServerSocket serverSocket = new ServerSocket(serverPort);
        serverSocket.setSoTimeout(60*1000);

        // Create the UDP socket
        DatagramSocket datagramSocket = new DatagramSocket(serverPort);



        // Create the logger without using Parent Handlers
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.WARNING);



        for(int i = 0; i < threadPoolSize; i++) {
            new Thread(() -> executeAddatudeProtocol(serverSocket, logger, userListMap)).start();
            new Thread(() -> executeNoTiFiProtocol(datagramSocket, logger)).start();
        }



    }






    /**
     * Executes the Addatude Protocol
     * @param serverSocket the socket from the TCP connection
     * @param logger logs important information
     * @param userListMap a map of valid users.
     */
    public static void executeAddatudeProtocol(ServerSocket serverSocket, Logger logger,Map<Long, Server.User> userListMap){

            while(true) {
                Socket clntSocket;
                try {
                    clntSocket = serverSocket.accept();
                    AddatudeProtocol.handleClient(clntSocket, logger, userListMap);
                } catch (IOException e) {
                    logger.log(Level.WARNING, "TCP Client Accept Failed", e);
                } catch (ValidationException e) {
                    logger.log(Level.WARNING, "Validation Exception in Client", e);
                }
            }
    }






    /**
     * Executes the NoTiFi Protocol
     * @param datagramSocket the socket from the UDP connection
     * @param logger logs important messages
     */
    public static void executeNoTiFiProtocol(DatagramSocket datagramSocket, Logger logger) {

        while (true) {

            byte[] inBuffer = new byte[MAX_READ_SIZE_UDP];

            try {

                // Receive the packet from the client
                DatagramPacket datagramPacket = new DatagramPacket(inBuffer, inBuffer.length);
                datagramSocket.receive(datagramPacket);
                byte[] encodedMsg = Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength());

                new NoTiFiServer(datagramSocket, encodedMsg).handleClientConnection(logger, datagramPacket);
            }catch (IOException e){
                logger.log(Level.WARNING, "UDP Client Accept Failed", e);

            }

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
