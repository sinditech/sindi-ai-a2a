/**
 * 
 */
package za.co.sindi.ai.a2a.types;

import java.io.Serializable;

/**
 * @author Buhake Sindi
 * @since 22 October 2025
 */
public sealed interface A2AError extends Serializable permits JSONParseError, InvalidRequestError, MethodNotFoundError, InvalidParamsError, InternalError, TaskNotFoundError, 
															TaskNotCancelableError, PushNotificationNotSupportedError, UnsupportedOperationError, ContentTypeNotSupportedError, 
															InvalidAgentResponseError, AuthenticatedExtendedCardNotConfiguredError {

}
