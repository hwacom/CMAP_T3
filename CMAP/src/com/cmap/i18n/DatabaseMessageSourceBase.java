package com.cmap.i18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.cmap.dao.I18nDAO;
import com.cmap.model.I18n;


@Named("databaseMessageSourceBase")
public class DatabaseMessageSourceBase extends AbstractMessageSource implements ResourceLoaderAware {
	private static Log log = LogFactory.getLog(DatabaseMessageSourceBase.class);

	@Autowired
	private I18nDAO i18nDAO;

	/**
	 * Map切分字符
	 */
	protected final String MAP_SPLIT_CODE = "|";
    protected final String DB_SPLIT_CODE = "_";

	private static Map<String, String> properties = new HashMap<String, String>();

	@SuppressWarnings("unused")
	private ResourceLoader resourceLoader;

	@PostConstruct
	public void init() {
		reload();
	}

	public void reload() {
		properties.clear();
		properties.putAll(loadTexts());
	}

	public List<I18n> getResource() {
		return i18nDAO.listI18n();
	}

	/**
	 *
	 * 描述：TODO
	 * 加载数据
	 * @return
	 */
	protected Map<String, String> loadTexts() {
		Map<String, String> mapResource = new HashMap<String, String>();
		List<I18n> resources = this.getResource();
		for (I18n item : resources) {
			String code = item.getKey() + MAP_SPLIT_CODE + item.getLocale();
			mapResource.put(code, item.getValue());
		}
		return mapResource;
	}

	/**
	 *
	 * 描述：TODO
	 * @param code
	 * @param locale 本地化语言
	 * @return
	 */
	public String getMessage(String code, Locale locale, String exception) {
		String localeCode = locale.getLanguage() + DB_SPLIT_CODE + locale.getCountry();
		String key = code + MAP_SPLIT_CODE + localeCode;
		String localeText = properties.get(key);
		String resourceText = exception;

		if(localeText != null) {
			resourceText = localeText;
		}
		else {
			localeCode = Locale.ENGLISH.getLanguage();
			key = code + MAP_SPLIT_CODE + localeCode;
			localeText = properties.get(key);
			if(localeText != null) {
				resourceText = localeText;
			}
			else {
				try {
					if(getParentMessageSource() != null) {
						resourceText = getParentMessageSource().getMessage(code, null, locale);
					}
				} catch (Exception e) {
					logger.error("Cannot find message with code: " + code);
				}
			}
		}
		return resourceText;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = getMessage(code, locale, code);
		return createMessageFormat(msg, locale);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String result = getMessage(code, locale, code);
		return result;
	}
}
