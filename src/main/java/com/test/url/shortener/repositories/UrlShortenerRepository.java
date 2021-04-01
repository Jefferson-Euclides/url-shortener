package com.test.url.shortener.repositories;

import com.test.url.shortener.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UrlShortenerRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByEncodedUrl(String encodedUrl);

    Optional<Url> findByFullUrl(String fullUrl);

}
