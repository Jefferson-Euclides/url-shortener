package com.test.url.shortener.services;

import com.test.url.shortener.entities.Url;
import com.test.url.shortener.helpers.UrlHelper;
import com.test.url.shortener.repositories.UrlShortenerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class UrlShortenerService {

    Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    @Autowired
    UrlShortenerRepository urlShortenerRepository;

    @Autowired
    UrlHelper urlHelper;

    public ResponseEntity<String> saveUrl(String urlToEncode, HttpServletRequest req) {
        final String queryParams = (req.getQueryString() != null) ? "?" + req.getQueryString() : "";
        final String url = (req.getRequestURI() + queryParams).substring(1);


        if (urlHelper.validate(urlToEncode)) {
            String encodedUrl = urlHelper.encodeUrl(url);

            Optional<Url> urlOptional = urlShortenerRepository.findByFullUrl(urlToEncode);

            if (!urlOptional.isPresent()) {
                urlShortenerRepository.save(Url.builder()
                        .encodedUrl(encodedUrl)
                        .fullUrl(urlToEncode)
                        .accessesNumber(0L)
                        .build());
            }

            return new ResponseEntity<>("http://localhost:8080/short/" + encodedUrl, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("Invalid Url, maybe you are missing the protocol.", HttpStatus.BAD_REQUEST);

    }

    public List<Url> accessMetrics() {
        return urlShortenerRepository.findAll();
    }

    public Url findByEncodedUrl(String encodedUrl) {
        Optional<Url> urlOptional = urlShortenerRepository.findByEncodedUrl(encodedUrl);

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();

            url.setAccessesNumber(url.getAccessesNumber() + 1);
            urlShortenerRepository.save(url);

            return url;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find this Url.");
    }

}
