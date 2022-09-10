package ru.job4j.passport.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.passport.aspect.Loggable;
import ru.job4j.passport.model.Passport;

import java.util.Collections;
import java.util.List;

@Service
@Loggable
public class PassportClientRestImpl implements PassportClient {

    @Value("${api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public PassportClientRestImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Passport create(Passport passport) {
        return restTemplate.postForEntity(
                apiUrl, passport, Passport.class
        ).getBody();
    }

    @Override
    public boolean update(String id, Passport passport) {
        return restTemplate.exchange(
                String.format("%s/update?id=%s", apiUrl, id),
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Override
    public boolean delete(String id) {
        return restTemplate.exchange(
                String.format("%s/delete?id=%s", apiUrl, id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    @Override
    public List<Passport> findAll() {
        return getList(String.format(
                "%s/find/all", apiUrl
        ));
    }

    @Override
    public List<Passport> findAllBySeries(int series) {
        return getList(String.format(
                "%s/find?series=%d", apiUrl, series
        ));
    }

    @Override
    public List<Passport> getAllExpired() {
        return getList(String.format(
                "%s/unavaliabe", apiUrl
        ));
    }

    @Override
    public List<Passport> getAllNeedToBeReplaced() {
        return getList(String.format(
                "%s/find-replaceable", apiUrl
        ));
    }

    private List<Passport> getList(String url) {
        List<Passport> body = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
