package br.com.zup.adrianoavelino.proposta.carteiradigital;

import org.springframework.data.repository.CrudRepository;

public interface CarteiraDigitalDoCartaoRepository  extends CrudRepository<CarteiraDigitalDoCartao, Long> {
    boolean existsByTipoCarteiraDigitalAndCartaoId(TipoCarteiraDigital tipoCarteiraDigital, Long cartaoId);
}
