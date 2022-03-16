package addatude.app.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {



    static class ServerThread extends Thread{

        ServerSocket serverSocket;
        Logger logger;

        ServerThread(ServerSocket serverSocket, Logger logger){
            this.serverSocket = serverSocket;
            this.logger = logger;
        }

        @Override
        public void run() {
            while(true){
                try{
                    Socket clntSocket = serverSocket.accept();
                    AddatudeProtocol.handleAddatudeClient(clntSocket, logger);
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Client Accept Failed", e);
                }
            }
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
        String password = args[2];


        // Create the Socket & Logger
        final ServerSocket serverSocket = new ServerSocket(serverPort);
        final Logger logger = Logger.getLogger("Server Log");
        logger.setLevel(Level.WARNING);


        for(int i = 0; i < threadPoolSize; i++){
            ServerThread serverThread = new ServerThread(serverSocket, logger);
            serverThread.start();
            logger.info("Created & Started Thread = " + serverThread.getName());
        }



    }

}
