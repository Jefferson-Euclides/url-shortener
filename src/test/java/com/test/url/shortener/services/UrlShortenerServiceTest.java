package com.test.url.shortener.services;

import com.test.url.shortener.BaseTest;
import com.test.url.shortener.entities.Url;
import com.test.url.shortener.helpers.UrlHelper;
import com.test.url.shortener.repositories.UrlShortenerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class UrlShortenerServiceTest extends BaseTest {

    @Mock
    UrlShortenerRepository urlShortenerRepository;

    @Mock
    UrlHelper urlHelper;

    @InjectMocks
    UrlShortenerService urlShortenerService;

    @Test
    public void encryptExistingUrlTest() {
        Url url = generateUrl();
        String uriString = generateRandomString(10);
        String randomEncodedUrl = generateRandomString(10);
        String content = "http://localhost:8080/short/" + randomEncodedUrl;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(uriString);
        request.setContent(content.getBytes());

        when(urlHelper.validate(anyString())).thenReturn(true);
        when(urlHelper.encodeUrl(anyString())).thenReturn(randomEncodedUrl);
        when(urlShortenerRepository.findByFullUrl(anyString())).thenReturn(Optional.of(url));

        ResponseEntity<String> response = urlShortenerService.saveUrl(uriString, request);

        assertEquals(response.getBody(), content);
        verify(urlShortenerRepository, times(0)).save(url);
    }

    @Test
    public void encryptNewUrlTest() {
        Url url = generateUrl();
        String randomEncodedUrl = url.getEncodedUrl();
        String content = "http://localhost:8080/short/" + randomEncodedUrl;

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(url.getFullUrl());
        request.setContent(content.getBytes());

        when(urlHelper.validate(anyString())).thenReturn(true);
        when(urlHelper.encodeUrl(anyString())).thenReturn(randomEncodedUrl);
        when(urlShortenerRepository.findByFullUrl(anyString())).thenReturn(Optional.empty());
        when(urlShortenerRepository.save(any())).thenReturn(url);

        ResponseEntity<String> response = urlShortenerService.saveUrl(url.getFullUrl(), request);

        assertEquals(response.getBody(), content);
        verify(urlShortenerRepository, times(1)).save(url);
    }
    
    @Test
    public void encryptInvalidUrlTest() {
        Url url = generateUrl();
        String content = "Invalid Url, maybe you are missing the protocol.";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(url.getFullUrl());
        request.setContent(content.getBytes());

        when(urlHelper.validate(anyString())).thenReturn(false);

        ResponseEntity<String> response = urlShortenerService.saveUrl(url.getFullUrl(), request);

        assertEquals(response.getBody(), content);
    }
    
    @Test
    public void metricsTest() {
        List<Url> listUrl = Arrays.asList(generateUrl(), generateUrl(), generateUrl());

        when(urlShortenerRepository.findAll()).thenReturn(listUrl);

        List<Url> metricsList = urlShortenerService.accessMetrics();

        assertEquals(3, metricsList.size());
        verify(urlShortenerRepository, times(1)).findAll();
    }

    @Test
    public void emptyMetricsTest() {
        when(urlShortenerRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<Url> metricsList = urlShortenerService.accessMetrics();

        assertEquals(0, metricsList.size());
        verify(urlShortenerRepository, times(1)).findAll();
    }

    @Test
    public void findByEncodedUrlTest() {
        Url randomUrl = generateUrl();
        String randomString = generateRandomString(10);

        when(urlShortenerRepository.findByEncodedUrl(anyString())).thenReturn(Optional.of(randomUrl));

        Url url = urlShortenerService.findByEncodedUrl(randomString);

        assertEquals(url, randomUrl);
        verify(urlShortenerRepository, times(1)).findByEncodedUrl(randomString);
    }

    @Test
    public void findByInvalidEncodedUrlTest() {
        String randomString = generateRandomString(10);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            urlShortenerService.findByEncodedUrl(randomString);
        });

        String expectedMessage = "404 NOT_FOUND \"Unable to find this Url.\"";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
