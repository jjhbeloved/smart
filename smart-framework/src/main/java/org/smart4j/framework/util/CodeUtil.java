package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.ConfigConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/8.
 */
public class CodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);

    public static String encodeURL(String source) {
        try {
            return URLEncoder.encode(source, ConfigConstant.ENC_UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url failure", e);
            throw new RuntimeException(e);
        }
    }

    public static String decodeURL(String source) {
        try {
            return URLEncoder.encode(source, ConfigConstant.ENC_UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("decode url failure", e);
            throw new RuntimeException(e);
        }
    }
}
