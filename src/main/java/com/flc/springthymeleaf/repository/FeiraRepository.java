package com.flc.springthymeleaf.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.flc.springthymeleaf.domain.Feira;
import com.flc.springthymeleaf.enums.StatusFeira;



public interface FeiraRepository extends JpaRepository<Feira, Integer>, PagingAndSortingRepository<Feira, Integer> {

    @Query("SELECT MAX(c.numero) FROM Feira c")
    Long findMaxNumero();

    default Feira findByDataFeiraAndStatusFeira(LocalDate data, String statusString) {
        StatusFeira status = StatusFeira.valueOf(statusString);
        return findByDataFeiraAndStatusFeira(data, status);
    }

    Feira findByDataFeiraAndStatusFeira(LocalDate data, StatusFeira status);

    boolean existsByDataFeira(LocalDate dataFeira);

    Feira findByDataFeira(LocalDate dataFeira);

    Feira findByNumero(Long numero);
    
    void deleteById(Integer id);

    @Query("SELECT f FROM Feira f WHERE f.dataFeira BETWEEN :dataInicio AND :dataFim ORDER BY f.dataFeira DESC")
    Page<Feira> findByDataFeiraBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    @Query("SELECT f FROM Feira f ORDER BY f.dataFeira DESC")
    Page<Feira> findAll(Pageable pageable);

	Optional<Feira> findFirstByOrderByDataFeiraDesc();
	
	@Query("SELECT f FROM Feira f WHERE f.statusFeira = :status ORDER BY f.dataFeira DESC")
	Page<Feira> findFirstByStatusFeiraOrderByDataFeiraDesc(@Param("status") StatusFeira status, Pageable pageable);

}
