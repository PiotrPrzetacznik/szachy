package com.example.szachy.controller;

import com.example.szachy.model.Szachista;
import com.example.szachy.repository.SzachistaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class SzachistaController {

    private static final Logger log = LoggerFactory.getLogger(SzachistaController.class);

    private final SzachistaRepository szachistaRepository;

    @Autowired
    public SzachistaController(SzachistaRepository szachistaRepository) {
        this.szachistaRepository = szachistaRepository;
    }

    @GetMapping("/")
    public String listaSzachistow(Model model){
        List<Szachista> szachisci = szachistaRepository.findAll();
        szachisci.sort((s1, s2) -> Integer.compare(s2.getRanking(), s1.getRanking())); //sortowanie po rankingu
        model.addAttribute("szachisci", szachisci);
        return "lista";
    }

    @GetMapping("/dodaj")
    public String pokazFormularzDodawania(Model model){
        model.addAttribute("szachista", new Szachista());
        return "formularz";
    }
    @PostMapping("/dodaj")
    public String dodajSzachiste(@Valid @ModelAttribute("szachista") Szachista szachista, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        log.info("Próba dodania szachisty: {}", szachista);
        if(result.hasErrors()){
            log.warn("Błędy walidacji: {}", result.getAllErrors());
            model.addAttribute("szachista", szachista); // Przekazujemy obiekt szachista z powrotem do formularza
            return "formularz";
        }
        try {
            szachistaRepository.save(szachista);
            redirectAttributes.addFlashAttribute("message", "Szachista dodany pomyślnie");
            log.info("Szachista dodany pomyślnie: {}", szachista);
        } catch (Exception e){
            log.error("Błąd podczas zapisu szachisty: ", e);
            model.addAttribute("error", "Wystąpił błąd podczas dodawania szachisty.");
            return "formularz";
        }
        return "redirect:/";
    }

    @GetMapping("/edytuj/{id}")
    public String pokazFormularzEdycji(@PathVariable("id") Long id, Model model){
        Szachista szachista = szachistaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Nieprawidłowe ID szachisty " + id));
        model.addAttribute("szachista", szachista);
        return "formularz";
    }

    @PostMapping("/edytuj/{id}")
    public String edytujSzachiste(@PathVariable("id") Long id, @Valid @ModelAttribute("szachista") Szachista szachista, BindingResult result, RedirectAttributes redirectAttributes, Model model){
        log.info("Próba edycji szachisty o ID: {}, dane: {}", id, szachista);
        if(result.hasErrors()){
            log.warn("Błędy walidacji podczas edycji szachisty o ID: {}, błędy: {}", id, result.getAllErrors());
            model.addAttribute("szachista", szachista);
            return "formularz";
        }
        Szachista szachistaDoEdycji = szachistaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Nieprawidłowe ID szachisty " + id));
        try{
            szachistaDoEdycji.setImie(szachista.getImie());
            szachistaDoEdycji.setNazwisko(szachista.getNazwisko());
            szachistaDoEdycji.setRanking(szachista.getRanking());
            szachistaRepository.save(szachistaDoEdycji);
            redirectAttributes.addFlashAttribute("message", "Szachista zedytowany pomyślnie");
            log.info("Szachista o ID: {} został zedytowany pomyślnie", id);
        } catch (Exception e){
            log.error("Błąd podczas edycji szachisty o ID: " + id, e);
            model.addAttribute("error", "Wystąpił błąd podczas edycji szachisty.");
            return "formularz";
        }
        return "redirect:/";
    }

    @GetMapping("/usun/{id}")
    public String usunSzachiste(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Szachista szachista = szachistaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowe ID szachisty: " + id));
            szachistaRepository.delete(szachista);
            redirectAttributes.addFlashAttribute("message", "Szachista usunięty pomyślnie");
            log.info("Szachista o ID: {} został usunięty pomyślnie", id);
        } catch (Exception e){
            log.error("Błąd podczas usuwania szachisty o ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas usuwania szachisty.");
        }
        return "redirect:/";
    }
}