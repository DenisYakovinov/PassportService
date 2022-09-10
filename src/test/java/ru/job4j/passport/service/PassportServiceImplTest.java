package ru.job4j.passport.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.passport.exception.EntityNotFoundException;
import ru.job4j.passport.exception.PassportReservedException;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassportServiceImplTest {
    
    private static final long PASSPORT_ID = 1;
    private static final int PASSPORT_NUMBER = 1;
    private static final int PASSPORT_SERIES = 2;

    @Mock
    PassportRepository passportRepository;

    @InjectMocks
    PassportServiceImpl passportService;

    @Captor
    ArgumentCaptor<LocalDate> date;

    @Test
    void getAll_shouldCallRepositoryThenReturnPassports() {
        Set<Passport> expected = Set.of(Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES),
                Passport.of(2, PASSPORT_SERIES));
        when(passportRepository.findAll()).thenReturn(expected);
        assertEquals(expected, passportService.getAll());
    }

    @Test
    void getPassportsBySeries_shouldCallRepositoryThenReturnPassports_whenPassportSeries() {
        Set<Passport> expected = Set.of(Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES),
                Passport.of(2, PASSPORT_SERIES));
        when(passportRepository.findAllBySeries(2)).thenReturn(expected);
        assertEquals(expected, passportService.getPassportsBySeries(2));
    }

    @Test
    void getExpiredPassports_shouldCallRepositoryThenReturnPassports() {
        Set<Passport> expected = Set.of(Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES),
                Passport.of(2, PASSPORT_SERIES));
        when(passportRepository.findAllByExpiredBefore(any(LocalDate.class))).thenReturn(expected);
        assertEquals(expected, passportService.getExpiredPassports());
    }

    @Test
    void getNeedReplaceInThreeMonth_shouldReturnPassportsExpiringInNextThreeMonths() {
        Set<Passport> expected = Set.of(Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES),
                Passport.of(2, PASSPORT_SERIES));
        when(passportRepository.findAllByExpiredBetween(date.capture(), date.capture())).thenReturn(expected);
        assertEquals(expected, passportService.getNeedReplaceInThreeMonth());
        assertEquals(date.getAllValues().get(0).plusMonths(3), date.getAllValues().get(1));
    }

    @Test
    void save_shouldReturnSavedPassport_whenPassport() {
        Passport expected = Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES);
        when(passportRepository.save(expected)).thenReturn(expected);
        assertEquals(expected, passportService.save(expected));
    }

    @Test
    void save_shouldThrowPassportReservedException_whenPassportExists() {
        Passport expected = Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES);
        when(passportRepository.findBySeriesAndNumber(expected.getSeries(), expected.getNumber()))
                .thenReturn(Optional.of(expected));
        assertThrows(PassportReservedException.class, () -> passportService.save(expected));
    }

    @Test
    void update_shouldReturnUpdatedPassport_whenPassport() {
        Passport expected = Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES);
        expected.setId(PASSPORT_ID);
        when(passportRepository.existsById(expected.getId())).thenReturn(true);
        when(passportRepository.save(expected)).thenReturn(expected);
        assertEquals(expected, passportService.update(expected));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenPassportDoesNotExist() {
        Passport expected = Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES);
        expected.setId(PASSPORT_ID);
        when(passportRepository.existsById(expected.getId()))
                .thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> passportService.update(expected));
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenPassportDoesNotExist() {
        Passport expected = Passport.of(PASSPORT_NUMBER, PASSPORT_SERIES);
        expected.setId(PASSPORT_ID);
        when(passportRepository.existsById(expected.getId()))
                .thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> passportService.delete(expected));
    }
}
