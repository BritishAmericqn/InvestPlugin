package arcadering.invest;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class PersistentConfig {
    private MappedByteBuffer out;

    void initialize() throws IOException {
        File file = new File("globalBalance.dat");
        boolean exists = file.exists();

        out = new RandomAccessFile("globalBalance.dat", "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 16);
        if (!exists) {
            out.putLong(0, 0);
            out.putInt(8, -1);
        }
     }

    public long adjustGlobalBalance(long amount) {
        long current = out.getLong(0);
        long newBalance = current + amount;
        out.putLong(0, newBalance);
        return newBalance;
    }

    public long getGlobalBalance() {
        return out.getLong(0);
    }

    public void close() {
       if (out != null) {
           out.force();
       }
    }

    public void setLastClaimed(int index) {
        out.putInt(8, index);
    }

    public int getLastClaimed() {
        return out.getInt(8);
    }
}
