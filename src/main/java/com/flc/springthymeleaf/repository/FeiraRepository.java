package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.flc.springthymeleaf.domain.Feira;
import enums.StatusFeira;

public interface FeiraRepository extends JpaRepository<Feira, Long>, PagingAndSortingRepository<Feira, Long> {

    @Query("SELECT MAX(c.numero) FROM Feira c")
    Long findMaxNumero();

    default Feira findByDataFeiraAndStatusFeira(LocalDate data, String statusString) {
        // Converter a string para o enum StatusFeira
        StatusFeira status = StatusFeira.valueOf(statusString);
        return findByDataFeiraAndStatusFeira(data, status);
    }

    Feira findByDataFeiraAndStatusFeira(LocalDate data, StatusFeira status);

    boolean existsByDataFeira(LocalDate dataFeira); // Método existsByDataFeira do Spring Data JPA

    Feira findByDataFeira(LocalDate dataFeira);

    Feira findByNumero(Long numero);

    @Query("SELECT f FROM Feira f WHERE f.dataFeira BETWEEN :dataInicio AND :dataFim ORDER BY f.dataFeira DESC")
    Page<Feira> findByDataFeiraBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);
}
