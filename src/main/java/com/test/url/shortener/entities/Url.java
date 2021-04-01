package com.test.url.shortener.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class Url {

    @Id
    @GeneratedValue
    private Long id;

    private String encodedUrl;

    private String fullUrl;

    private Long accessesNumber;

    public Url(){

    }

    public Url(String encodedUrl, String fullUrl) {
        this.encodedUrl = encodedUrl;
        this.fullUrl = fullUrl;
    }

    public Url(Long id, String encodedUrl, String fullUrl) {
        this.id = id;
        this.encodedUrl = encodedUrl;
        this.fullUrl = fullUrl;
    }

    public Url(Long id, String encodedUrl, String fullUrl, Long accessesNumber) {
        this.id = id;
        this.encodedUrl = encodedUrl;
        this.fullUrl = fullUrl;
        this.accessesNumber = accessesNumber;
    }

    public Url(String encodedUrl) {
        this.encodedUrl = encodedUrl;
    }

}
