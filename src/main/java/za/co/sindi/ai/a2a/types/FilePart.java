/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.util.Map;
import java.util.Objects;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public final class FilePart extends PartBase {

	private FileBase file;

	/**
	 * @param file
	 */
	public FilePart(FileBase file) {
		super();
		this.file = Objects.requireNonNull(file, "File is required.");
	}
	
	/**
	 * @param file
	 * @param metadata
	 */
	@JsonbCreator
	public FilePart(@JsonbProperty("file") FileBase file, @JsonbProperty("metadata") Map<String, Object> metadata) {
		this(file);
		setMetadata(metadata);
	}

	/**
	 * @return the file
	 */
	public FileBase getFile() {
		return file;
	}
}
