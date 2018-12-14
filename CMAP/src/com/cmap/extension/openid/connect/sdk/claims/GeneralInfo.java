/*
 * oauth2-oidc-sdk
 *
 * Copyright 2012-2016, Connect2id Ltd and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.cmap.extension.openid.connect.sdk.claims;


import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONObjectUtils;
import com.nimbusds.openid.connect.sdk.claims.ClaimsSet;

import net.minidev.json.JSONObject;


/**
 * GeneralInfo claims set, serialisable to a JSON object.
 *
 * <p>Related specifications:
 *
 * <ul>
 *     <li>OpenID Connect Core 1.0, sections 5.1 and 5.6.
 * </ul>
 */
public class GeneralInfo extends ClaimsSet {


	/**
	 * Creates a new GeneralInfo claims set from the specified JSON object.
	 *
	 * @param jsonObject The JSON object. Must not be {@code null}.
	 *
	 * @throws IllegalArgumentException If the JSON object doesn't contain
	 *                                  a subject {@code sub} string claim.
	 */
	public GeneralInfo(final JSONObject jsonObject) {

		super(jsonObject);
	}

	/**
	 * Parses a GeneralInfo claims set from the specified JSON object string.
	 *
	 * @param json The JSON object string to parse. Must not be
	 *             {@code null}.
	 *
	 * @return The GeneralInfo claims set.
	 *
	 * @throws ParseException If parsing failed.
	 */
	public static GeneralInfo parse(final String json)
		throws ParseException {

		JSONObject jsonObject = JSONObjectUtils.parse(json);

		try {
			return new GeneralInfo(jsonObject);

		} catch (IllegalArgumentException e) {

			throw new ParseException(e.getMessage(), e);
		}
	}
}
