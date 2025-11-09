/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public sealed abstract class FileBase implements Serializable permits FileWithBytes, FileWithUri {

	private String name;
	private String mimeType;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
