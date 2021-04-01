package com.test.url.shortener.controllers;

import com.test.url.shortener.entities.Url;
import com.test.url.shortener.services.UrlShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
public class UrlShortenerController {

    Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    @Autowired
    UrlShortenerService urlShortenerService;

    /**
     * @param url to be encrypted
     * @param req
     * @return Shortened URL
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> urlShortener(@Validated @RequestParam String url, HttpServletRequest req) {
        logger.debug("Shortening Url: " + url);
        return urlShortenerService.saveUrl(url, req);
    }

    @GetMapping(value = "/short/{encodedUrl}")
    @ResponseStatus(HttpStatus.PERMANENT_REDIRECT)
    public ModelAndView redirect(@Validated @PathVariable String encodedUrl){
        Url url = urlShortenerService.findByEncodedUrl(encodedUrl);

        logger.debug("Redirecting to: " + url.getFullUrl());
        return new ModelAndView("redirect:" + url.getFullUrl());
    }

    @GetMapping("/{metrics}")
    @ResponseStatus(HttpStatus.OK)
    public List<Url> accessMetrics() {
        return urlShortenerService.accessMetrics();
    }

}
