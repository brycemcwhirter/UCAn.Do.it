package addatude.app.server;

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


        // Create the Socket & Logger
        final ServerSocket serverSocket = new ServerSocket(serverPort);
        final Logger logger = Logger.getLogger("Server Log");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.WARNING);


        for(int i = 0; i < threadPoolSize; i++){
            Thread thread = new Thread(() -> {
                while(true){
                    try{
                        Socket clntSocket = serverSocket.accept();
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
