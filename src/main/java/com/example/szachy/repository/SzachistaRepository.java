package com.example.szachy.repository;

import com.example.szachy.model.Szachista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SzachistaRepository extends JpaRepository<Szachista, Long> {

}