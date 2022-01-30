package serialization;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MessageOutput {

    OutputStream os;

    public MessageOutput(OutputStream os) throws NullPointerException{
        Objects.requireNonNull(os);
        this.os = os;
    }

    void write(byte[] b) throws IOException {
        os.write(b);
    }


}
