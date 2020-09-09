import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
/*
    Data structure is as follows
    A Map with key=fileSize and value=list of filenames
*/
public class ChunkHashFileCopy {
  private static Map<MapKey,List<String>> fileMap = new HashMap<MapKey,List<String>>();
  private static List<String> exclusionList = new ArrayList<String>();
  private static List<String> minimumFileSizeList = new ArrayList<String>();
  private static int minimumFileSize = 1000;
  private static List<String> matchFileNamesList = new ArrayList<String>();
  private static boolean matchFileNames = false;
  private static DecimalFormat decimalFormatter = new DecimalFormat("###,###,000");
  private static boolean printMapFileSizes = true;
  /******************************************************************************************/
  public static void main(String[] args) throws Exception {
    String propertyFilePath = locatePropertiesFile();
    System.out.println("REM propertyFilePath="+propertyFilePath);
    Map<String,List<String>> hm = PropertiesLoader.loadToHashMap(propertyFilePath);
    
    String SRC_LOCATION = "d:/torr_src/abc.mp4";
    String DST_LOCATION = "d:/torr_dst/";
    exclusionList = (List<String>)hm.get("EXCLUDE_PATH");
    minimumFileSizeList = (List<String>)hm.get("MINIMUM_FILE_SIZE");
    minimumFileSize = Integer.parseInt(minimumFileSizeList.get(0));
    matchFileNamesList = (List<String>)hm.get("ALSO_MATCH_FILE_NAMES");
    if(matchFileNamesList.get(0).equalsIgnoreCase("Y")) {
        matchFileNames = true;
    }

    List<String> fileList = null;
    if((args != null) && (args.length > 0)) {
        System.out.println("REM folder list ="+args);
        fileList = loadFileLists(args);
    } else {
        System.out.println("REM folder list = .");
        fileList = loadFileLists(new String[]{"."}); // if no arguments are given, process current directory
    }
    System.out.println("REM loading to Map - start");
    System.out.println("REM matchFileNames = "+matchFileNames);
    
    for(int i=0;i<fileList.size();i++) {
      if(i%1000 == 0) {
        System.out.println("REM "+i+",");
      }
      String fileName = fileList.get(i);
      //System.out.println("fileName       = " + fileName);
      File file = new File(fileName);
      if(file.isDirectory()) {
        continue;
      }
      long fileSize = file.length();
      if(fileSize < minimumFileSize) {
        continue;
      }
      String fileNameOnly = (new File(fileName)).getName();
      MapKey mapKey = null;
      
      if(matchFileNames == true) {
    	  mapKey = new MapKey(fileSize, fileNameOnly);
      } else {
    	  mapKey = new MapKey(fileSize, null);
      }
    	  
      //System.out.println("mapKey="+mapKey);
      //System.out.println("mapKey.hash="+mapKey.hashCode());
      //System.out.println("fileMap="+fileMap);
      if(fileMap.containsKey(mapKey)) {
          fileMap.get(mapKey).add(fileName);
      } else {
        List<String> list = new ArrayList<String>();
        list.add(fileName);
        fileMap.put(mapKey, list);
      }
    }
    
    System.out.println("REM fileList.size()="+fileList.size());
    //System.out.println("REM fileMap="+fileMap);
    System.out.println("REM loading to Map - end");

    //--------------- print map sizes - start --------------------------------
    if(printMapFileSizes) {
        System.out.println("REM ------------Map sizes - start");
        Set<MapKey> keys1 = fileMap.keySet();
        Iterator<MapKey> iter1 = keys1.iterator();
        while(iter1.hasNext()) {
          MapKey key1  = iter1.next();

          List<String> sameSizedFileList1 = fileMap.get(key1);
          if(sameSizedFileList1 == null) {
            continue;
          }
          //System.out.println("REM fileSize = "+key1+" ; sameSizedFileList.size()= " + sameSizedFileList1.size());
        }
        System.out.println("REM ------------Map sizes - end");
    }
    //--------------- print map sizes - end ----------------------------------
    

    //display duplicate now
    //System.out.println("REM fileMap = "+fileMap);
    
    synchronized(fileMap) {  
        Set<MapKey> keys = fileMap.keySet();
        List<MapKey> keyList = new ArrayList<MapKey>(); 
        keyList.addAll(keys); 
        Collections.sort(keyList);
        Iterator<MapKey> iter = keyList.iterator();
        while(iter.hasNext()) {
          MapKey mapKey  = iter.next();

          List<String> sameSizedFileList = fileMap.get(mapKey);
          if(sameSizedFileList == null) {
            continue;
          }
          //System.out.println();
          //System.out.println();
          //System.out.println();
          //System.out.println();
          //System.out.println();
          //System.out.println("REM fileSize = "+fileSizeLong+" ; sameSizedFileList.size()= " + sameSizedFileList.size());

          if(sameSizedFileList.size() >= 2) {
			  Map<String, String> sameSizedFileMap = new HashMap<String, String>();
              for(int i=0;i<sameSizedFileList.size();i++) {
				String fileCheckSum = MD6Util.getMD6ChecksumAsHEX(sameSizedFileList.get(i));
				sameSizedFileMap.put(sameSizedFileList.get(i), fileCheckSum);
			  }

              String firstFile  = null;
              //String otherFile  = null;
              String secondFile = null;
              for(int i=0;i<sameSizedFileList.size();i++) {
                firstFile  = sameSizedFileList.get(i);
				String firstFileCheckSum = sameSizedFileMap.get(firstFile);
                List<String> identicalFileList = new ArrayList<String>();
                identicalFileList.add(firstFile);
                for(int j=i+1;j<sameSizedFileList.size();j++) {
                    secondFile = sameSizedFileList.get(j);
					String secondFileCheckSum = sameSizedFileMap.get(secondFile);
                    if(firstFileCheckSum.equals(secondFileCheckSum)) {
                        identicalFileList.add(secondFile);
                        sameSizedFileList.remove(j);
                        j--;  //if we are removing this list item, then no need to increment, the next item is available in the same index j
                    }
                } //end of for j loop
        
                if(identicalFileList.size() >= 2) {
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    //System.out.println("REM : file size : "+decimalFormatter.format(fileSize));
                    //System.out.println("REM : file size : "+decimalFormatter.format(fileSizeLong));
                    System.out.println("REM : file size : "+decimalFormatter.format(mapKey.getFileSize()));
                    System.out.println("REM "+"\""+firstFile+"\"");
                    for(int j=1;j<identicalFileList.size();j++) {
                        //start from second file only
                        System.out.println("DEL "+"\""+identicalFileList.get(j)+"\"");
                    }
                }
              }//end of for i loop
          }
          sameSizedFileList.clear();
          //fileMap.remove(fileCheckSum);
        }//end while
    } //end sync
    System.out.println("\r\n\r\n\r\n\r\n\r\nREM program end");
    
  }
  /******************************************************************************************/
  public static List<String> loadFileLists(String[] args) {
    List<String> returnArray = new ArrayList<String>();
    if(args.length  >= 1) {
        if(args[0].equalsIgnoreCase("-i")) {
            matchFileNames = false;
            if(args.length  == 1) {
                // if that is the only parameter, then add current directory for comparison
                returnArray.addAll(getFileListFromFolder("."));
            }
        } else {
            returnArray.addAll(getFileListFromFolder(args[0]));
        }
    }
    if(args.length  >= 2) {
        returnArray.addAll(getFileListFromFolder(args[1]));
    }
    if(args.length  >= 3) {
        returnArray.addAll(getFileListFromFolder(args[2]));
    }
    if(args.length  >= 4) {
        returnArray.addAll(getFileListFromFolder(args[3]));
    }
    if(args.length  >= 5) {
        returnArray.addAll(getFileListFromFolder(args[4]));
    }
    return returnArray;
  }
  /******************************************************************************************/
    public static List<String> getFileListFromFolder(String sourcePath) {
        //System.out.println("REM "+sourcePath);
        if(sourcePath.endsWith("\\")) {
            sourcePath = sourcePath.substring(0,sourcePath.length()-1);
        }
        if(exclusionList.contains(sourcePath)) {
            System.out.println("REM Skipping folder :"+sourcePath);
            return new ArrayList<String>();
        }
        File dir = new File(sourcePath);
        if(!dir.exists()) {
            return new ArrayList<String>();
        }
        List<String> fileTree = new ArrayList<String>();
        try {
			int filecount = 0;
            for (File entry : dir.listFiles()) {
                if (entry.isFile()) {
					filecount++;
					if (filecount > 700) {
						//return fileTree;
					}
                    String fileName = entry.getAbsolutePath();
                    //int ii = fileName.lastIndexOf(".");
                    //String extn="";
                    //String bareName="";
                    //if(ii > -1) {
                    //    extn = fileName.substring(ii+1);
                    //    bareName = fileName.substring(0,ii);
                    //    //System.out.println("extn="+extn);
                    //    //System.out.println("bareName="+bareName);
                    //}
                    //long fileSize = entry.length();
                    //String fileSizeStr = decimalFormatter.format(fileSize);
                    //String newFileName = bareName + "(fs=" + fileSizeStr + ")." + extn;
                    ////System.out.println("newFileName="+newFileName);
                    fileTree.add(fileName);
                } else {
                    fileTree.addAll(getFileListFromFolder(entry.getAbsolutePath()));
                }
            }
        } catch(Exception e) {
            //
        }
        return fileTree;
    }
  /******************************************************************************************/
  /******************************************************************************************/
  public static ArrayList<String> readFileList(String fileName) throws Exception {
    System.out.println("loading sob.txt - start");
    ArrayList<String> returnArray = new ArrayList<String>();
    BufferedReader input =  new BufferedReader(new FileReader(new File(fileName)));
    try {
        String line = null; //not declared within while loop
        while (( line = input.readLine()) != null){
          File file = new File(line);
          if(file.isDirectory()) {
            continue;
          }
          long fileSize = file.length();
          //if( (fileSize < 10000) || (fileSize > 50000)) {
          if(fileSize < minimumFileSize) {
            continue;
          }
          returnArray.add(line);
        }
      }
      finally {
        input.close();
      }
    System.out.println("loading sob.txt - end");
    return returnArray;
  }
  /******************************************************************************************/
  public static boolean compareFiles(String left, String right) throws Exception {
    //System.out.println("left  File = "+left);
    //System.out.println("right File = "+right);
    File leftFile = new File(left);
    File rightFile = new File(right);
    long leftFileSize = leftFile.length();
    long rightFileSize = rightFile.length();
    
    if(leftFileSize != rightFileSize) {
        return false;
    }
    long sizeToCompare = leftFileSize;
    if(leftFileSize > 1000000) {
        sizeToCompare = 1000000;
    }
    BufferedInputStream left_bis = null;
    BufferedInputStream right_bis = null;
    byte[] leftBA = new byte[(int)sizeToCompare];
    byte[] rightBA = new byte[(int)sizeToCompare];
    try {
        left_bis = new BufferedInputStream(new FileInputStream(leftFile));
        right_bis = new BufferedInputStream(new FileInputStream(rightFile));
        left_bis.read(leftBA, 0, (int)sizeToCompare);
        right_bis.read(rightBA, 0, (int)sizeToCompare);
    }catch(FileNotFoundException fnfe) {
        return false;
    }finally {
    	left_bis.close();
    	right_bis.close();
    }
    boolean result = blockCompare(leftBA, rightBA);
    //System.out.println("comparison = "+result);
    return result;
  }
  /******************************************************************************************/
  public static boolean blockCompare(byte[] left, byte[] right) throws Exception {
    if(left.length != right.length) {
        return false;
    }
    for(int i=0;i<left.length;i++) {
        if(left[i] != right[i]) {
            return false;
        }
    }
    return true;
  }
  /******************************************************************************************/
  /******************************************************************************************/
  public static String locatePropertiesFile() {
      String filePath="";
      filePath = "C:\\DUPLICATE_FINDER_PROPERTIES.TXT";
      if((new File(filePath)).exists()) return filePath;
      
      filePath = "D:\\DUPLICATE_FINDER_PROPERTIES.TXT";
      if((new File(filePath)).exists()) return filePath;
      
      filePath = "D:\\Programs_Portable_GIT\\Java_Utils\\DUPLICATE_FINDER_PROPERTIES.TXT";
      if((new File(filePath)).exists()) return filePath;

      String classPath = System.getProperty("java.class.path"); //assuming only one path in CLASSPATH
      File file = new File(classPath);
      filePath = file.getParent() + "\\DUPLICATE_FINDER_PROPERTIES.TXT";
      if((new File(filePath)).exists()) return filePath;

      return "";
  }

  /******************************************************************************************/

  /******************************************************************************************/

  /******************************************************************************************/

  /******************************************************************************************/
}


