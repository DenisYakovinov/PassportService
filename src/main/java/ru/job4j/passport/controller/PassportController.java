package ru.job4j.passport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.passport.aspect.Loggable;
import ru.job4j.passport.model.Passport;
import ru.job4j.passport.service.PassportService;

import java.util.Set;

@RestController
@RequestMapping("/api/passport")
@Loggable
public class PassportController {

    private final PassportService passportService;

    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @PostMapping("/save")
    public Passport save(@RequestBody Passport passport) {
        return passportService.save(passport);
    }

    @PutMapping("/update")
    public ResponseEntity<Passport> update(@RequestParam int id, @RequestBody Passport passport) {
        passport.setId(id);
        passportService.update(passport);
        return ResponseEntity.status(HttpStatus.OK).body(passportService.update(passport));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Passport> delete(@RequestParam int id, @RequestBody Passport passport) {
        passport.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(passportService.update(passport));
    }

    @GetMapping("/find")
    public Set<Passport> getAllBySeries(@RequestParam(required = true) Integer series) {
        return passportService.getPassportsBySeries(series);
    }

    @GetMapping("/find/all")
    public Set<Passport> getAll() {
        return passportService.getAll();
    }

    @GetMapping("/unavaliabe")
    public Set<Passport> getAllExpired() {
        return passportService.getExpiredPassports();
    }

    @GetMapping("/find-replaceable")
    public Set<Passport> getAllNeedToBeReplaced() {
        return passportService.getNeedReplaceInThreeMonth();
    }
}
