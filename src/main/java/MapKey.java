public class MapKey implements Comparable {
	private long fileSize = 0;
	private String fileNameOnly = null;

	/******************************************************************************************/
	public MapKey(long fileSize, String fileNameOnly) {
		super();
		this.fileSize = fileSize;
		this.fileNameOnly = fileNameOnly;
	}

	/******************************************************************************************/
	public int compareTo(Object mapKey ) {

		MapKey obj = (MapKey) mapKey ;
	    Long int1 = new Long(obj.getFileSize());
	    Long int2 = new Long(this.getFileSize());
	    
		//if(this.getFileNameOnly() != null && obj.getFileNameOnly() != null) {
        //    int retval =  this.getFileNameOnly().compareTo(obj.getFileNameOnly());
        //    if(retval == 0 ) {
        //        retval =  int1.compareTo(int2);
        //        return retval;
        //    }
        //    return retval;
		//}
		//if(this.getFileNameOnly() == null || obj.getFileNameOnly() == null) {
            int retval =  int1.compareTo(int2);
            return retval;
		//}
		//return 0;
	}

	/******************************************************************************************/
	@Override
	public String toString() {
		if(fileNameOnly == null) {
			return fileSize+"";
		}
		return fileSize + "_" + fileNameOnly;
	}

	/******************************************************************************************/
	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileNameOnly() {
		return fileNameOnly;
	}

	public void setFileNameOnly(String fileNameOnly) {
		this.fileNameOnly = fileNameOnly;
	}

	/******************************************************************************************/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileNameOnly == null) ? 0 : fileNameOnly.hashCode());
		result = prime * result + (int) (fileSize ^ (fileSize >>> 32));
		return result;
	}

	/******************************************************************************************/
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapKey other = (MapKey) obj;
		if (fileNameOnly == null) {
			if (other.fileNameOnly != null)
				return false;
		} else if (!fileNameOnly.equals(other.fileNameOnly))
			return false;
		if (fileSize != other.fileSize)
			return false;
		return true;
		
		//return false;
	}

	/******************************************************************************************/

	/******************************************************************************************/

	/******************************************************************************************/

	/******************************************************************************************/
}

