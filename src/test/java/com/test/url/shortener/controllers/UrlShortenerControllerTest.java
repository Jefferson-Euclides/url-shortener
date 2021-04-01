package com.test.url.shortener.controllers;

import com.test.url.shortener.BaseTest;
import com.test.url.shortener.entities.Url;
import com.test.url.shortener.services.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UrlShortenerControllerTest extends BaseTest {

    @MockBean
    UrlShortenerService urlShortenerService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void urlShortenerTest() throws Exception {
        String url = generateRandomString(10);
        ResponseEntity<String> responseEntity = new ResponseEntity<>(url, HttpStatus.CREATED);

        given(urlShortenerService.saveUrl(anyString(), any())).willReturn(responseEntity);

        mvc.perform(post("/")
                .param("url", "testing")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void redirectTest() throws Exception {
        Url url = generateUrl();

        given(urlShortenerService.findByEncodedUrl(anyString())).willReturn(url);

        mvc.perform(get("/short/{encodedUrl}", url.getEncodedUrl())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isPermanentRedirect());
    }

    @Test
    public void metricsTest() throws Exception {
        List<Url> listUrl = Arrays.asList(generateUrl(), generateUrl(), generateUrl());

        given(urlShortenerService.accessMetrics()).willReturn(listUrl);

        mvc.perform(get("/metrics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
