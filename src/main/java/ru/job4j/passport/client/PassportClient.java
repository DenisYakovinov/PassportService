package ru.job4j.passport.client;

import ru.job4j.passport.model.Passport;

import java.util.List;

public interface PassportClient {

    Passport create(Passport passport);

    boolean update(String id, Passport passport);

    boolean delete(String id);

    List<Passport> findAll();

    List<Passport> findAllBySeries(int series);

    List<Passport> getAllExpired();

    List<Passport> getAllNeedToBeReplaced();
}
