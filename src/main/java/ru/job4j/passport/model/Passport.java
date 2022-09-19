package ru.job4j.passport.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Passport", uniqueConstraints = {@UniqueConstraint(name = "unique_series_number",
        columnNames = {"series", "number"})})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private long id;

    @Column(name = "series")
    @EqualsAndHashCode.Include
    private int series;

    @Column(name = "number")
    @EqualsAndHashCode.Include
    private int number;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "expired")
    private LocalDate expired;

    public static Passport of(int number, int series) {
        Passport passport = new Passport();
        passport.setNumber(number);
        passport.setSeries(series);
        return passport;
    }
}
