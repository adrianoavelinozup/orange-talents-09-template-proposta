package br.com.zup.adrianoavelino.proposta.proposta;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {
    Optional<Proposta> findByDocumento(String documento);
}
