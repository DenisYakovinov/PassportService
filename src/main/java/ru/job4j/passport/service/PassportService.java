package ru.job4j.passport.service;

import ru.job4j.passport.model.Passport;

import java.util.Set;

public interface PassportService {

    Passport save(Passport passport);

    Passport update(Passport passport);

    void delete(Passport passport);

    Set<Passport> getAll();

    Set<Passport> getPassportsBySeries(int series);

    Set<Passport> getExpiredPassports();

    Set<Passport> getNeedReplaceInThreeMonth();
}
