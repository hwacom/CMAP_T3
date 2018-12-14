package com.cmap.extension.openid.connect.sdk;

import javax.mail.internet.ContentType;

import com.cmap.extension.openid.connect.sdk.claims.GeneralInfo;
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.SuccessResponse;
import com.nimbusds.oauth2.sdk.http.CommonContentTypes;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public class EduInfoSuccessResponse extends EduInfoResponse implements SuccessResponse {

	/**
	 * The GeneralInfo claims set, serialisable to a JSON object.
	 */
	private final GeneralInfo claimsSet;
	
	
	/**
	 * The GeneralInfo claims set, as plain, signed or encrypted JWT.
	 */
	private final JWT jwt;
	
	
	/**
	 * Creates a new GeneralInfo success response where the claims are 
	 * specified as an unprotected GeneralInfo claims set.
	 *
	 * @param claimsSet The GeneralInfo claims set. Must not be {@code null}.
	 */
	public EduInfoSuccessResponse(final GeneralInfo claimsSet) {
	
		if (claimsSet == null)
			throw new IllegalArgumentException("The claims must not be null");
		
		this.claimsSet = claimsSet;
		
		this.jwt = null;
	}
	
	
	/**
	 * Creates a new GeneralInfo success response where the claims are 
	 * specified as a plain, signed or encrypted JSON Web Token (JWT).
	 *
	 * @param jwt The GeneralInfo claims set. Must not be {@code null}.
	 */
	public EduInfoSuccessResponse(final JWT jwt) {
	
		if (jwt == null)
			throw new IllegalArgumentException("The claims JWT must not be null");
		
		this.jwt = jwt;
		
		this.claimsSet = null;
	}
	
	
	@Override
	public boolean indicatesSuccess() {
	
		return true;
	}
	
	
	/**
	 * Gets the content type of this GeneralInfo response.
	 *
	 * @return The content type, according to the claims format.
	 */
	public ContentType getContentType() {
	
		if (claimsSet != null)
			return CommonContentTypes.APPLICATION_JSON;
		else
			return CommonContentTypes.APPLICATION_JWT;
	}
	
	
	/**
	 * Gets the GeneralInfo claims set as an unprotected GeneralInfo claims set.
	 *
	 * @return The GeneralInfo claims set, {@code null} if it was specified as
	 *         JSON Web Token (JWT) instead.
	 */
	public GeneralInfo getGeneralInfo() {
	
		return claimsSet;
	}
	
	
	/**
	 * Gets the GeneralInfo claims set as a plain, signed or encrypted JSON
	 * Web Token (JWT).
	 *
	 * @return The GeneralInfo claims set as a JSON Web Token (JWT), 
	 *         {@code null} if it was specified as an unprotected GeneralInfo
	 *         claims set instead.
	 */
	public JWT getGeneralInfoJWT() {
	
		return jwt;
	}
	
	
	@Override
	public HTTPResponse toHTTPResponse() {
	
		HTTPResponse httpResponse = new HTTPResponse(HTTPResponse.SC_OK);
		
		httpResponse.setContentType(getContentType());
		
		String content;
		
		if (claimsSet != null) {
		
			content = claimsSet.toJSONObject().toString();
	
		} else {
			
			try {
				content = jwt.serialize();
				
			} catch (IllegalStateException e) {
			
				throw new SerializeException("Couldn't serialize GeneralInfo claims JWT: " + 
					                     e.getMessage(), e);
			}
		}
		
		httpResponse.setContent(content);
	
		return httpResponse;
	}
	
	
	/**
	 * Parses a GeneralInfo response from the specified HTTP response.
	 *
	 * <p>Example HTTP response:
	 *
	 * <pre>
	 * HTTP/1.1 200 OK
	 * Content-Type: application/json
	 * 
	 * {
	 *  "sub"         : "248289761001",
	 *  "name"        : "Jane Doe"
	 *  "given_name"  : "Jane",
	 *  "family_name" : "Doe",
	 *  "email"       : "janedoe@example.com",
	 *  "picture"     : "http://example.com/janedoe/me.jpg"
	 * }
	 * </pre>
	 *
	 * @param httpResponse The HTTP response. Must not be {@code null}.
	 *
	 * @return The GeneralInfo response.
	 *
	 * @throws ParseException If the HTTP response couldn't be parsed to a 
	 *                        GeneralInfo response.
	 */
	public static EduInfoSuccessResponse parse(final HTTPResponse httpResponse)
		throws ParseException {
		
		httpResponse.ensureStatusCode(HTTPResponse.SC_OK);
		
		httpResponse.ensureContentType();
		
		ContentType ct = httpResponse.getContentType();
		
		
		EduInfoSuccessResponse response;
		
		if (ct.match(CommonContentTypes.APPLICATION_JSON)) {
		
			GeneralInfo claimsSet;
			
			try {
				claimsSet = new GeneralInfo(httpResponse.getContentAsJSONObject());
				
			} catch (Exception e) {
				
				throw new ParseException("Couldn't parse GeneralInfo claims: " + 
					                 e.getMessage(), e);
			}
			
			response = new EduInfoSuccessResponse(claimsSet);
		}
		else if (ct.match(CommonContentTypes.APPLICATION_JWT)) {
		
			JWT jwt;
			
			try {
				jwt = httpResponse.getContentAsJWT();
				
			} catch (ParseException e) {
			
				throw new ParseException("Couldn't parse GeneralInfo claims JWT: " + 
					                 e.getMessage(), e);
			}
			
			response = new EduInfoSuccessResponse(jwt);
		}
		else {
			throw new ParseException("Unexpected Content-Type, must be " + 
			                         CommonContentTypes.APPLICATION_JSON +
						 " or " +
						 CommonContentTypes.APPLICATION_JWT);
		}
		
		return response;
	}
}