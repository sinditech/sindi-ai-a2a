/**
 * 
 */
package za.co.sindi.ai.a2a.types;

/**
 * @author Buhake Sindi
 * @since 15 October 2025
 */
public enum TaskState {
	/** The task has been submitted and is awaiting execution. */
	SUBMITTED("submitted"),
	
	/** The agent is actively working on the task. */
	WORKING("working"),
	
	/** The task is paused and waiting for input from the user. */
	INPUT_REQUIRED("input-required"),
	
	/** The task has been successfully completed. */
	COMPLETED("completed"),
	
	/** The task has been canceled by the user. */
	CANCELED("canceled"),
	
	/** The task failed due to an error during execution. */
	FAILED("failed"),
	
	/** The task was rejected by the agent and was not started. */
	REJECTED("rejected"),
	
	/** The task requires authentication to proceed. */
	AUTH_REQUIRED("auth-required"),
	
	/** The task is in an unknown or indeterminate state. */
	UNKNOWN("unknown")
	;
	private final String state;

	/**
	 * @param state
	 */
	private TaskState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return state;
	}
	
	public static TaskState of(final String value) {
		for (TaskState state : values()) {
			if (state.state.equals(value)) return state;
		}
		
		throw new IllegalArgumentException("Invalid Task state value '" + value + "'.");
	}
}
