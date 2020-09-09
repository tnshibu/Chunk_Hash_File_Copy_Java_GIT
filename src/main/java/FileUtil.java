
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class FileUtil {

    //=========================================================================================
    public static List<String> getFileList(String sourcePath) {
        System.out.println("REM -- "+sourcePath);
        File dir = new File(sourcePath);
        List<String> fileTree = new ArrayList<String>();
        for (File entry : dir.listFiles()) {
            if (entry.isFile())
                fileTree.add(entry.getAbsolutePath());
            else
                fileTree.addAll(getFileList(entry.getAbsolutePath()));
        }
        return fileTree;
    }
    //=========================================================================================
    public static void writeListToFile(String fileListLocation, List<String> fileList) throws Exception {
        FileOutputStream fos1 = new FileOutputStream(new File(fileListLocation));
        for (int i = 0; i < fileList.size(); i++) {
            fos1.write((fileList.get(i) + "\n").getBytes());
        }
        fos1.close();
    }
    //=========================================================================================
    public static ArrayList<String> readListFromFile(String fileName) throws Exception {
        ArrayList<String> returnArray = new ArrayList<String>();
        BufferedReader input = new BufferedReader(new FileReader(new File(fileName)));
        try {
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                returnArray.add(line);
            }
        } finally {
            input.close();
        }
        return returnArray;
    }
    //=========================================================================================
    public static ByteBuffer readFullFileAsBlocks(String filePath) throws IOException {
    	int BLOCK_SIZE = 1_000_000;
        File binaryFile = new File(filePath);
        long fileSize = binaryFile.length();
        
        FileChannel binaryFileChannel = new RandomAccessFile(binaryFile, "r").getChannel();

        return binaryFileChannel.map(FileChannel.MapMode.READ_ONLY, start, size);
    }
    //=========================================================================================
    public static ByteBuffer readBlockFromFile(String filePath, int start, int size) throws IOException {
        File binaryFile = new File(filePath);
        FileChannel binaryFileChannel = new RandomAccessFile(binaryFile, "r").getChannel();

        return binaryFileChannel.map(FileChannel.MapMode.READ_ONLY, start, size);
    }
    //=========================================================================================
}
