package br.com.zup.adrianoavelino.proposta.bloqueio;

import org.springframework.data.repository.CrudRepository;

public interface BloqueioCartaoRepository extends CrudRepository<BloqueioCartao, Long> {
    boolean existsByCartaoNumeroCartao(String numeroCartao);
}
