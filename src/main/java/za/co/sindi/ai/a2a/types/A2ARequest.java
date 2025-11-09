/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
@JsonbTypeInfo(
		key = "method",
		value = {
		    @JsonbSubtype(alias=SendMessageRequest.DEFAULT_METHOD, type=SendMessageRequest.class),
		    @JsonbSubtype(alias=SendStreamingMessageRequest.DEFAULT_METHOD, type=SendStreamingMessageRequest.class),
		    @JsonbSubtype(alias=GetTaskRequest.DEFAULT_METHOD, type=GetTaskRequest.class),
		    @JsonbSubtype(alias=CancelTaskRequest.DEFAULT_METHOD, type=CancelTaskRequest.class),
		    @JsonbSubtype(alias=ListTasksRequest.DEFAULT_METHOD, type=ListTasksRequest.class),
		    @JsonbSubtype(alias=SetTaskPushNotificationConfigRequest.DEFAULT_METHOD, type=SetTaskPushNotificationConfigRequest.class),
		    @JsonbSubtype(alias=GetTaskPushNotificationConfigRequest.DEFAULT_METHOD, type=GetTaskPushNotificationConfigRequest.class),
		    @JsonbSubtype(alias=TaskResubscriptionRequest.DEFAULT_METHOD, type=TaskResubscriptionRequest.class),
		    @JsonbSubtype(alias=ListTaskPushNotificationConfigRequest.DEFAULT_METHOD, type=ListTaskPushNotificationConfigRequest.class),
		    @JsonbSubtype(alias=DeleteTaskPushNotificationConfigRequest.DEFAULT_METHOD, type=DeleteTaskPushNotificationConfigRequest.class),
		    @JsonbSubtype(alias=GetAuthenticatedExtendedCardRequest.DEFAULT_METHOD, type=GetAuthenticatedExtendedCardRequest.class),
		}
	)
public sealed interface A2ARequest extends Serializable permits SendMessageRequest, SendStreamingMessageRequest, GetTaskRequest, CancelTaskRequest, ListTasksRequest, SetTaskPushNotificationConfigRequest, GetTaskPushNotificationConfigRequest,
																TaskResubscriptionRequest, ListTaskPushNotificationConfigRequest, DeleteTaskPushNotificationConfigRequest, GetAuthenticatedExtendedCardRequest {

}
