package addatude.app.server;

import addatude.serialization.ValidationException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {




    public static void main(String[] args) throws IOException {

        // Get the Arguments
        if(args.length != 3){
            throw new IllegalArgumentException("Parameters: <Port> <Threads> <Password>");
        }


        // Set Values
        int serverPort = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);
        String password = args[2];
        // todo you may have to define location response here?


        //Validate the password
        validatePasssword(password);


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
                        AddatudeProtocol.handleAddatudeClient(clntSocket, logger);
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Client Accept Failed", e);
                    }


                }
            });
            thread.start();
            logger.info("Created & Started Thread = " + thread.getName());
        }



    }

    //TODO Test Password Method
    private static void validatePasssword(String password) {

    }

}
