package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);

    @Query("SELECT p FROM Proposta p WHERE p.statusProposta = 'ELEGIVEL' AND p.cartao = null")
    List<Proposta> findByPropostaElegivelSemCartao();
}
