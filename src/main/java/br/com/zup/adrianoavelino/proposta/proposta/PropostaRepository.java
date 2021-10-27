package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    @Query(value = "SELECT p.* FROM propostas p " +
                   "WHERE p.status_proposta = 'ELEGIVEL' AND p.cartao_id IS NULL " +
                   "LIMIT :quantidadeItensPorConsulta", nativeQuery = true)
    List<Proposta> findByPropostaElegivelSemCartao(Long quantidadeItensPorConsulta);
}
