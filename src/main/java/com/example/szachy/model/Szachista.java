package com.example.szachy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class Szachista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Imię nie może być puste.")
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków.")
    private String imie;

    @NotEmpty(message = "Nazwisko nie może być puste.")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków.")
    private String nazwisko;

    @Min(value = 0, message = "Ranking nie może być ujemny.")
    private int ranking;

    public Szachista() {}

    public Szachista(String imie, String nazwisko, int ranking) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}