/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.htmlformentry.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.EncounterRole;
import org.openmrs.Provider;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.util.LocaleUtility;

/**
 * Tag support functions, providing common methods for parsing parameters
 */
public class TagUtil {
	
	public static <T> List<T> parseListParameter(Map<String, String> parameters, String paramName, Class<T> type) {
		List<T> ret = new ArrayList<T>();
		String csvValue = parseParameter(parameters, paramName, String.class);
		if (csvValue != null) {
			for (String s : csvValue.split(",")) {
				ret.add(parseValue(s.trim(), type));
			}
		}
		return ret;
	}
	
	public static <T> T parseParameter(Map<String, String> parameters, String paramName, Class<T> type) {
		return parseParameter(parameters, paramName, type, null);
	}
	
	public static <T> T parseParameter(Map<String, String> parameters, String paramName, Class<T> type, T defaultValue) {
		T paramValue = parseValue(parameters.get(paramName), type);
		if (paramValue == null) {
			paramValue = defaultValue;
		}
		return paramValue;
	}
	
	public static <T> T parseValue(String valueToParse, Class<T> type) {
		T ret = null;
		if (valueToParse != null) {
			if (type == String.class) {
				ret = (T) valueToParse;
			} else if (type == Integer.class) {
				ret = (T) Integer.valueOf(valueToParse);
			} else if (type == Double.class) {
				ret = (T) Double.valueOf(valueToParse);
			} else if (type == Boolean.class) {
				ret = (T) Boolean.valueOf(valueToParse);
			} else if (type == Locale.class) {
				ret = (T) LocaleUtility.fromSpecification(valueToParse);
			} else if (type == Concept.class) {
				ret = (T) HtmlFormEntryUtil.getConcept(valueToParse);
			} else if (type == Drug.class) {
				ret = (T) HtmlFormEntryUtil.getDrug(valueToParse);
			} else if (type == EncounterRole.class) {
				ret = (T) HtmlFormEntryUtil.getEncounterRole(valueToParse);
			} else if (type == Provider.class) {
				ret = (T) HtmlFormEntryUtil.getProvider(valueToParse);
			} else if (type.isEnum()) {
				for (T enumConstant : type.getEnumConstants()) {
					if (enumConstant.toString().equalsIgnoreCase(valueToParse)) {
						ret = enumConstant;
					}
				}
			}
			if (ret == null) {
				throw new IllegalArgumentException("Unable to parse " + valueToParse + " into: " + type);
			}
		}
		return ret;
	}
}
