package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.PropostaRepetidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/propostas")
public class PropostaController {
    @Autowired
    private PropostaRepository propostaRepository;

    private final Logger logger = LoggerFactory.getLogger(PropostaController.class);

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriBuilder) {
        Proposta proposta = request.toModel();
        if(proposta.ehRepetida(propostaRepository)) {
            logger.warn("Proposta com o documento {} já está cadastrada!", proposta.getDocumento());
            throw  new PropostaRepetidaException("Proposta repetida. Não pode haver mais de uma proposta por documento");
        }
        propostaRepository.save(proposta);
        logger.info("Proposta documento={}, salário={} criada com sucesso!", proposta.getDocumento(), proposta.getSalario());
        URI uri = uriBuilder.path("/v1/propostas/{id}").buildAndExpand(proposta.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
