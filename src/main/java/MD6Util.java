
import java.io.*;
import java.security.MessageDigest;

public class MD6Util {
   static int bytesToRead = 1024 * 1024;

  /******************************************************************************************/
    public static String getMD5ChecksumAsHEX(String filename) throws Exception {
        byte[] b = createChecksumAsBinary(filename);
        String result = convertByteArrayToHEX(b);
        return result;
    }
  /******************************************************************************************/
    public static String getMD6ChecksumAsHEX(String filename) throws Exception {
        String result = "";
        File file = new File(filename);
        if(file.length() < 2 * 1024 * 1024) { // if small file, size <= 2MB
            byte[] b = createChecksumAsBinary(filename);
            result = convertByteArrayToHEX(b);
        } else {
            byte[] b1 = createChecksumAsBinaryFor1024BytesFromBegining(filename);
            String result1 = convertByteArrayToHEX(b1);
            byte[] b2 = createChecksumAsBinaryFor1024BytesFromEnd(filename);
            String result2 = convertByteArrayToHEX(b2);
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b3 = md.digest((result1+result2).getBytes("UTF-8"));
            result = convertByteArrayToHEX(b3);
        }
        return result;
    }
  /******************************************************************************************/
  //sample code from http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
    public static byte[] createChecksumAsBinary(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }
      
  /******************************************************************************************/
    public static byte[] createChecksumAsBinaryFor1024BytesFromBegining(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[bytesToRead];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;

        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }

        fis.close();
        return complete.digest();
    }
    /******************************************************************************************/
    public static byte[] createChecksumAsBinaryFor1024BytesFromEnd(String filename) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[bytesToRead];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        
        File file = new File(filename);
        RandomAccessFile raf = new RandomAccessFile(filename, "r");

        // Seek to the end of file
        raf.seek(file.length() - bytesToRead);
        // Read it out.
        raf.read(buffer, 0, bytesToRead);
        
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }

        fis.close();
        return complete.digest();
    }
 /******************************************************************************************/
    public static String convertByteArrayToHEX(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ).toUpperCase();
        }
        return result;
    }
  /******************************************************************************************/

  /******************************************************************************************/
  /******************************************************************************************/
  /******************************************************************************************/
}
