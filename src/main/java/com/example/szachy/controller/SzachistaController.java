package com.example.szachy.controller;

import com.example.szachy.model.Szachista;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.szachy.repository.SzachistaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class SzachistaController {
    private final SzachistaRepository szachistaRepository;

    @Autowired
    public SzachistaController(SzachistaRepository szachistaRepository) {
        this.szachistaRepository = szachistaRepository;
    }

    @GetMapping("/")
    public String listaSzachistow(Model model){
        List<Szachista> szachisci = szachistaRepository.findAll();
        szachisci.sort((s1, s2) -> Integer.compare(s2.getRanking(), s1.getRanking()));
        model.addAttribute("szachisci", szachisci);
        return "lista";
    }

    @PostMapping("/dodaj")
    public String dodajSzachiste(@ModelAttribute("szachista") Szachista szachista, RedirectAttributes redirectAttributes){
        szachistaRepository.save(szachista);
        redirectAttributes.addFlashAttribute("message", "Szachista dodany pomyślnie");
        return "redirect:/";
    }

    @GetMapping("/usun/{id}")
    public String usunSzachiste(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Szachista szachista = szachistaRepository.findById(id).orElse(null);
        szachistaRepository.delete(szachista);
        redirectAttributes.addFlashAttribute("message", "Szachista usunięty pomyślnie");
        return "redirect:/";
    }

    @GetMapping("/edytuj/{id}")
    public String pokazFormularzEdycji(@PathVariable("id") Long id, Model model){
        Szachista szachista = szachistaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Nieprawidłowe ID szachisty " + id));
        model.addAttribute("szachista", szachista);
        return "formularz";
    }
}
