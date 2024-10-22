package bug;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InvalidCharacterFilterInputStream extends FilterInputStream {

    protected InvalidCharacterFilterInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int ch;
        while ((ch = super.read()) != -1) {
            // Skip invalid XML characters in the range 0x00 to 0x1F (except 0x09, 0x0A, 0x0D)
            if ((ch >= 0x00 && ch <= 0x08) || (ch >= 0x0B && ch <= 0x0C) || (ch >= 0x0E && ch <= 0x1F)) {
                continue; // Skip invalid characters
            }
            return ch;
        }
        return -1; // End of stream
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        for (int i = off; i < off + bytesRead; i++) {
            // Replace invalid characters with a space or skip them
            if ((b[i] >= 0x00 && b[i] <= 0x08) || (b[i] >= 0x0B && b[i] <= 0x0C) || (b[i] >= 0x0E && b[i] <= 0x1F)) {
                b[i] = ' '; // Optionally, replace with a space
            }
        }
        return bytesRead;
    }
}
