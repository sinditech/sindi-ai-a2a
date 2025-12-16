/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public sealed abstract class PartBase implements Part permits TextPart, FilePart, DataPart {

	private Map<String, Object> metadata;

	/**
	 * @return the metadata
	 */
	public Map<String, Object> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
}
