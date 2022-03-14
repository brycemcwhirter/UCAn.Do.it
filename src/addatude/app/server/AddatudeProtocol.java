package addatude.app.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddatudeProtocol {

    private Socket clntSocket;
    private Logger logger;

    public AddatudeProtocol(Socket clntSocket, Logger logger) {
        this.clntSocket = clntSocket;
        this.logger = logger;
    }

    public static void handleAddatudeClient(Socket clntSocket, Logger logger){

        try {
            InputStream in = clntSocket.getInputStream();
            OutputStream out = clntSocket.getOutputStream();


        } catch (IOException e) {
            logger.log(Level.WARNING, "Exception thrown in Addatude Protocol: ", e);
        }


    }
}
