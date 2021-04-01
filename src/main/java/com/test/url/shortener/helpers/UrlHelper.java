package com.test.url.shortener.helpers;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@Component
public class UrlHelper {
    Logger logger = LoggerFactory.getLogger(UrlHelper.class);

    public String encodeUrl(String url) {
        final String encodedUrl = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();

        return encodedUrl;
    }

    public Boolean validate(String stringUrl) {
        try {
            new java.net.URL(stringUrl);
            logger.debug("Url is valid!");

            return true;
        } catch (MalformedURLException e) {
            logger.error("Url format is wrong!");
            e.printStackTrace();

            return false;
        }
    }

}
