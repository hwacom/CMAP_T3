package com.cmap.extension.openid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.token.BearerTokenError;
import com.nimbusds.oauth2.sdk.util.StringUtils;

public class EduInfoErrorResponse extends EduInfoResponse implements ErrorResponse {


	/**
	 * Gets the standard errors for a UserInfo error response.
	 *
	 * @return The standard errors, as a read-only set.
	 */
	public static Set<BearerTokenError> getStandardErrors() {
		
		Set<BearerTokenError> stdErrors = new HashSet<>();
		stdErrors.add(BearerTokenError.MISSING_TOKEN);
		stdErrors.add(BearerTokenError.INVALID_REQUEST);
		stdErrors.add(BearerTokenError.INVALID_TOKEN);
		stdErrors.add(BearerTokenError.INSUFFICIENT_SCOPE);

		return Collections.unmodifiableSet(stdErrors);
	}


	/**
	 * The underlying error.
	 */
	private final ErrorObject error;


	/**
	 * Creates a new UserInfo error response. No OAuth 2.0 bearer token
	 * error / general error object is specified.
	 */
	private EduInfoErrorResponse() {

		error = null;
	}
	

	/**
	 * Creates a new UserInfo error response indicating a bearer token
	 * error.
	 *
	 * @param error The OAuth 2.0 bearer token error. Should match one of 
	 *              the {@link #getStandardErrors standard errors} for a 
	 *              UserInfo error response. Must not be {@code null}.
	 */
	public EduInfoErrorResponse(final BearerTokenError error) {

		this((ErrorObject) error);
	}
	
	
	/**
	 * Creates a new UserInfo error response indicating a general error.
	 *
	 * @param error The error. Must not be {@code null}.
	 */
	public EduInfoErrorResponse(final ErrorObject error) {
		
		if (error == null)
			throw new IllegalArgumentException("The error must not be null");
		
		this.error = error;
	}


	@Override
	public boolean indicatesSuccess() {

		return false;
	}


	@Override
	public ErrorObject getErrorObject() {

		return error;
	}


	/**
	 * Returns the HTTP response for this UserInfo error response.
	 *
	 * <p>Example HTTP response:
	 *
	 * <pre>
	 * HTTP/1.1 401 Unauthorized
	 * WWW-Authenticate: Bearer realm="example.com",
	 *                   error="invalid_token",
	 *                   error_description="The access token expired"
	 * </pre>
	 *
	 * @return The HTTP response matching this UserInfo error response.
	 */
	@Override
	public HTTPResponse toHTTPResponse() {

		HTTPResponse httpResponse;

		if (error != null && error.getHTTPStatusCode() > 0) {
			httpResponse = new HTTPResponse(error.getHTTPStatusCode());
		} else {
			httpResponse = new HTTPResponse(HTTPResponse.SC_BAD_REQUEST);
		}

		// Add the WWW-Authenticate header
		if (error instanceof BearerTokenError) {
			httpResponse.setWWWAuthenticate(((BearerTokenError) error).toWWWAuthenticateHeader());
		} else if (error != null){
			httpResponse.setContentType(CommonContentTypes.APPLICATION_JSON);
			httpResponse.setContent(error.toJSONObject().toJSONString());
		}

		return httpResponse;
	}


	/**
	 * Parses a UserInfo error response from the specified HTTP response
	 * {@code WWW-Authenticate} header.
	 *
	 * @param wwwAuth The {@code WWW-Authenticate} header value to parse. 
	 *                Must not be {@code null}.
	 *
	 * @return The UserInfo error response.
	 *
	 * @throws ParseException If the {@code WWW-Authenticate} header value 
	 *                        couldn't be parsed to a UserInfo error 
	 *                        response.
	 */
	public static EduInfoErrorResponse parse(final String wwwAuth)
		throws ParseException {

		BearerTokenError error = BearerTokenError.parse(wwwAuth);

		return new EduInfoErrorResponse(error);
	}
	
	
	/**
	 * Parses a UserInfo error response from the specified HTTP response.
	 *
	 * <p>Note: The HTTP status code is not checked for matching the error
	 * code semantics.
	 *
	 * @param httpResponse The HTTP response to parse. Its status code must
	 *                     not be 200 (OK). Must not be {@code null}.
	 *
	 * @return The UserInfo error response.
	 *
	 * @throws ParseException If the HTTP response couldn't be parsed to a 
	 *                        UserInfo error response.
	 */
	public static EduInfoErrorResponse parse(final HTTPResponse httpResponse)
		throws ParseException {
		
		httpResponse.ensureStatusCodeNotOK();

		String wwwAuth = httpResponse.getWWWAuthenticate();
		
		if (StringUtils.isNotBlank(wwwAuth)) {
			// Bearer token error?
			return parse(wwwAuth);
		}
		
		// Other error?
		return new EduInfoErrorResponse(ErrorObject.parse(httpResponse));
	}
}