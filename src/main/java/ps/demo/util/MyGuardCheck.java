package ps.demo.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class MyGuardCheck {

    private File file;
    private RandomAccessFile randomAccessFile = null;
    private FileChannel fileChannel;
    private FileLock fileLock;

    public MyGuardCheck(File file) {
        this.file = file;
    }

    public boolean tryLock() {
        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
            fileLock = fileChannel.tryLock();
            return fileLock != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean lockedByMe() {
        return fileLock != null;
    }

    public boolean unlock() {
        if (!lockedByMe()) {
            return false;
        }
        try {
            fileLock.release();
            fileLock = null;
            fileChannel.close();
            randomAccessFile.close();
            file.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


}
