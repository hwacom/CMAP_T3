package com.cmap.extension.openid.connect.sdk;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.Response;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public abstract class ConfigurationResponse implements Response {
	
	
	/**
	 * Casts this response to a UserInfo success response.
	 *
	 * @return The UserInfo success response.
	 */
	public ConfigurationSuccessResponse toSuccessResponse() {
		return (ConfigurationSuccessResponse) this;
	}
	
	
	/**
	 * Casts this response to a UserInfo error response.
	 *
	 * @return The UserInfo error response.
	 */
	public ConfigurationErrorResponse toErrorResponse() {
		return (ConfigurationErrorResponse) this;
	}


	/**
	 * Parses a UserInfo response from the specified HTTP response.
	 *
	 * @param httpResponse The HTTP response. Must not be {@code null}.
	 *
	 * @return The UserInfo success or error response.
	 *
	 * @throws ParseException If the HTTP response couldn't be parsed to a 
	 *                        UserInfo response.
	 */
	public static ConfigurationResponse parse(final HTTPResponse httpResponse)
		throws ParseException {
		
		if (httpResponse.getStatusCode() == HTTPResponse.SC_OK)
			return ConfigurationSuccessResponse.parse(httpResponse);
		else
			return ConfigurationErrorResponse.parse(httpResponse);
	}
}