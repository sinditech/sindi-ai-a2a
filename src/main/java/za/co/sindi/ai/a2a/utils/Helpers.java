/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskArtifactUpdateEvent;

/**
 * @author Buhake Sindi
 * @since 27 October 2025
 */
public class Helpers {
	private static final Logger LOGGER = Logger.getLogger(Helpers.class.getName());
	
	private Helpers() {
		throw new AssertionError("Private Constructor.");
	}
	
	/**
	 * Helper method for updating a Task object with new artifact data from an event.
	 * 
     * Handles creating the artifacts list if it doesn't exist, adding new artifacts,
     * and appending parts to existing artifacts based on the `append` flag in the event.
    
	 * @param task The {@link Task} object to modify.
	 * @param event The {@link TaskArtifactUpdateEvent} containing the artifact data.
	 */
	public static void appendArtifactToTask(final Task task, final TaskArtifactUpdateEvent event) {
		List<Artifact> artifacts = task.getArtifacts() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(task.getArtifacts()));
		Artifact newArtifact = event.getArtifact();
		String artifactId = newArtifact.artifactId();
		boolean appendParts = event.getAppend() != null ? event.getAppend() : false;
		
		Artifact existingArtifact = null;
		int existingArtifactListIndex = -1;
		
		for (int i = 0; i < artifacts.size(); i++) {
			Artifact artifact = artifacts.get(i);
			if (artifact.artifactId().equals(artifactId)) {
				existingArtifact = artifact;
				existingArtifactListIndex = i;
				break ;
			}
		}
		
		if (!appendParts) {
			if (existingArtifactListIndex > -1) {
				LOGGER.fine(String.format("Replacing artifact at id %s for task %s", artifactId, task.getId()));
				artifacts.set(existingArtifactListIndex, newArtifact);
//				task.getArtifacts()[existingArtifactListIndex] = newArtifact;
			} else {
				LOGGER.fine(String.format("Adding new artifact with id %s for task %s", artifactId, task.getId()));
				artifacts.add(newArtifact);
			}
		} else if (existingArtifact != null) {
			LOGGER.fine(String.format("Appending parts to artifact id %s for task %s", artifactId, task.getId()));
			List<Part> parts = existingArtifact.parts() == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(existingArtifact.parts()));
			parts.addAll(Arrays.asList(newArtifact.parts()));
			Artifact updated = new Artifact.Builder(existingArtifact)
                    .parts(parts.toArray(new Part[parts.size()]))
                    .build();
            artifacts.set(existingArtifactListIndex, updated);
		} else {
			LOGGER.warning(String.format("Received append=True for nonexistent artifact index %s in task %s. Ignoring chunk.", artifactId, task.getId()));
		}
		
		task.setArtifacts(artifacts.toArray(new Artifact[artifacts.size()]));
	}
}
