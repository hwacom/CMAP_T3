package com.cmap.extension.openid.connect.sdk;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ProtectedResourceRequest;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;

import net.jcip.annotations.Immutable;

@Immutable
public class EduInfoRequest extends ProtectedResourceRequest {

	/**
	 * The HTTP method.
	 */
	private final HTTPRequest.Method httpMethod;
	
	/**
	 * Creates a new UserInfo HTTP GET request.
	 *
	 * @param uri         The URI of the UserInfo endpoint. May be
	 *                    {@code null} if the {@link #toHTTPRequest} method
	 *                    will not be used.
	 * @param accessToken An OAuth 2.0 Bearer access token for the request.
	 *                    Must not be {@code null}.
	 */
	public EduInfoRequest(final URI uri, final BearerAccessToken accessToken) {
	
		this(uri, HTTPRequest.Method.GET, accessToken);
	}
	
	
	/**
	 * Creates a new UserInfo request.
	 *
	 * @param uri         The URI of the UserInfo endpoint. May be
	 *                    {@code null} if the {@link #toHTTPRequest} method
	 *                    will not be used.
	 * @param httpMethod  The HTTP method. Must be HTTP GET or POST and not 
	 *                    {@code null}.
	 * @param accessToken An OAuth 2.0 Bearer access token for the request.
	 *                    Must not be {@code null}.
	 */
	public EduInfoRequest(final URI uri, final HTTPRequest.Method httpMethod, final BearerAccessToken accessToken) {
	
		super(uri, accessToken);
		
		if (httpMethod == null)
			throw new IllegalArgumentException("The HTTP method must not be null");
		
		this.httpMethod = httpMethod;
		
		
		if (accessToken == null)
			throw new IllegalArgumentException("The access token must not be null");
	}
	
	
	/**
	 * Gets the HTTP method for this UserInfo request.
	 *
	 * @return The HTTP method.
	 */
	public HTTPRequest.Method getMethod() {
	
		return httpMethod;
	}
	
	@Override
	public HTTPRequest toHTTPRequest() {
		if (getEndpointURI() == null)
			throw new SerializeException("The endpoint URI is not specified");

		URL endpointURL;

		try {
			endpointURL = getEndpointURI().toURL();

		} catch (MalformedURLException e) {

			throw new SerializeException(e.getMessage(), e);
		}
	
		HTTPRequest httpRequest = new HTTPRequest(httpMethod, endpointURL);
		
		switch (httpMethod) {
		
			case GET:
				httpRequest.setAuthorization(getAccessToken().toAuthorizationHeader());
				break;
				
			case POST:
				httpRequest.setContentType(CommonContentTypes.APPLICATION_URLENCODED);
				httpRequest.setQuery("access_token=" + getAccessToken().getValue());
				break;
			
			default:
				throw new SerializeException("Unexpected HTTP method: " + httpMethod);
		}
		
		return httpRequest;
	}

	/**
	 * Parses the specified HTTP request for a UserInfo request.
	 *
	 * @param httpRequest The HTTP request. Must not be {@code null}.
	 *
	 * @return The UserInfo request.
	 *
	 * @throws ParseException If the HTTP request couldn't be parsed to a 
	 *                        UserInfo request.
	 */
	public static EduInfoRequest parse(final HTTPRequest httpRequest)
		throws ParseException {
		
		HTTPRequest.Method httpMethod = httpRequest.getMethod();
		
		BearerAccessToken accessToken = BearerAccessToken.parse(httpRequest);

		URI endpointURI;

		try {

			endpointURI = httpRequest.getURL().toURI();

		} catch (URISyntaxException e) {

			throw new ParseException(e.getMessage(), e);
		}
	
		return new EduInfoRequest(endpointURI, httpMethod, accessToken);
	}
}
