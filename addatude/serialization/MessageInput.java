package serialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MessageInput {

    private InputStream in;

    public MessageInput(InputStream in){
        Objects.requireNonNull(in, "Input Stream Cannot Be Null");
        this.in = in;
    }



    public String readUntilSpace() throws IOException {
        StringBuilder sb = new StringBuilder();

        try {

            int r;
            while ((r = in.read()) != -1) {
                char c = (char) r;

                if (c == ' ')
                    break;

                sb.append(c);
            }

        } catch(IOException e) {
            throw new IOException("Error occurred during reading");
        }

        return sb.toString();
    }

    public byte[] readAllBytes() throws IOException {
        return in.readAllBytes();
    }






}
