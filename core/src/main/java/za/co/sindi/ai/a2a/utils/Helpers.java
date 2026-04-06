/**
 * 
 */
package za.co.sindi.ai.a2a.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import za.co.sindi.ai.a2a.types.AgentCard;
import za.co.sindi.ai.a2a.types.Artifact;
import za.co.sindi.ai.a2a.types.MessageSendParams;
import za.co.sindi.ai.a2a.types.Part;
import za.co.sindi.ai.a2a.types.Task;
import za.co.sindi.ai.a2a.types.TaskArtifactUpdateEvent;
import za.co.sindi.ai.a2a.types.TaskState;
import za.co.sindi.ai.a2a.types.TaskStatus;
import za.co.sindi.ai.a2a.types.TextPart;
import za.co.sindi.ai.a2a.utils.jcs.JsonCanonicalizer;
import za.co.sindi.commons.utils.Strings;

/**
 * @author Buhake Sindi
 * @since 27 October 2025
 */
public final class Helpers {
	private static final Logger LOGGER = Logger.getLogger(Helpers.class.getName());
	
	private Helpers() {
		throw new AssertionError("Private Constructor.");
	}
	
	/**
	 * Create a new task object from message send params.
	 * <p />Generates UUIDs for task and context IDs if they are not already present in the message.
	 * 
	 * @param messageSendParams The {@link MessageSendParams} object containing the initial message.
	 * @return A new {@link Task} object initialized with 'submitted' status and the input message in history.
	 */
	public static Task createTaskObject(final MessageSendParams messageSendParams) {
		
		if (Strings.isNullOrEmpty(messageSendParams.message().getContextId())) {
			messageSendParams.message().setContextId(UUID.randomUUID().toString());
		}
		
		return new Task.Builder()
					   .id(UUID.randomUUID().toString())
					   .contextId(messageSendParams.message().getContextId())
					   .status(new TaskStatus(TaskState.SUBMITTED))
					   .history(Arrays.asList(messageSendParams.message()))
					   .build();
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
	
	/**
	 * Helper to create a text artifact.
	 * 
	 * @param text The text content for the artifact.
	 * @param artifactId The ID for the artifact.
	 * @return An {@link Artifact} object containing a single {@link TextPart}.
	 */
	public static Artifact buildTextArtifact(final String text, final String artifactId) {
		
		final TextPart textPart = new TextPart(text);
		return new Artifact.Builder()
				.artifactId(artifactId)
				.parts(Arrays.asList(textPart))
				.build();
		
	}
	
	public static String canonicalizeAgentCard(final AgentCard agentCard) {
		Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(agentCard);

        // Remove "signatures" field before canonicalization
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject obj = reader.readObject();
            JsonObjectBuilder builder = Json.createObjectBuilder();
            obj.entrySet().stream()
                    .filter(entry -> !"signatures".equals(entry.getKey()))
                    .forEach(entry -> builder.add(entry.getKey(), entry.getValue()));
            JsonObject withoutSignatures = builder.build();

            // Feed to JCS (the library guarantees RFC 8785 compliance)
            try {
				JsonCanonicalizer canonicalizer = new JsonCanonicalizer(withoutSignatures.toString());
				return canonicalizer.getEncodedString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new UncheckedIOException(e);
			} 
        }
	}
}
