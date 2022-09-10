package ru.job4j.passport.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.passport.aspect.Loggable;
import ru.job4j.passport.exception.EntityNotFoundException;
import ru.job4j.passport.exception.PassportReservedException;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.time.LocalDate;
import java.util.Set;

@Service
@Loggable
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;

    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    @Transactional
    public Passport save(Passport passport) {
        int series = passport.getSeries();
        int number = passport.getNumber();
        if (passportRepository.findBySeriesAndNumber(series, number).isPresent()) {
            throw new PassportReservedException(
                    String.format("The passport with series = %d and number = %s already reserved", series, number));
        }
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public Passport update(Passport passport) {
        long id = passport.getId();
        if (!passportRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("can't update, the passport with id = %d wasn't found.", id));
        }
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public void delete(Passport passport) {
        long id = passport.getId();
        if (!passportRepository.existsById(id)) {
            throw new EntityNotFoundException(String.format("can't delete, the passport with id = %d wasn't found.", id));
        }
        passportRepository.delete(passport);
    }

    @Override
    public Set<Passport> getAll() {
        return passportRepository.findAll();
    }

    @Override
    public Set<Passport> getPassportsBySeries(int series) {
        return passportRepository.findAllBySeries(series);
    }

    @Override
    public Set<Passport> getExpiredPassports() {
        return passportRepository.findAllByExpiredBefore(LocalDate.now());
    }

    @Override
    public Set<Passport> getNeedReplaceInThreeMonth() {
        return passportRepository.findAllByExpiredBetween(LocalDate.now(), LocalDate.now().plusMonths(3));
    }
}
