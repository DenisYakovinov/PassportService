package ru.job4j.passport.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.passport.model.Passport;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface PassportRepository extends CrudRepository<Passport, Long> {

    Optional<Passport> findBySeriesAndNumber(int series, int number);

    Set<Passport> findAllBySeries(int series);

    Set<Passport> findAllByExpiredBefore(LocalDate localDate);

    @Override
    Set<Passport> findAll();

    Set<Passport> findAllByExpiredBetween(LocalDate start, LocalDate end);
}
