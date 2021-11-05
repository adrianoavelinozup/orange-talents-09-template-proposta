package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.EntidadeNaoEncontradaException;
import br.com.zup.adrianoavelino.proposta.compartilhada.seguranca.Ofuscador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/propostas")
public class AcompanharPropostaController {
    private final Logger logger = LoggerFactory.getLogger(AcompanharPropostaController.class);

    @Autowired
    private PropostaRepository propostaRepository;

    @GetMapping("/{propostaId}")
    public ResponseEntity<Object> pesquisar(@PathVariable Long propostaId) {
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> {
                    logger.warn("Proposta = {} não encontrada", propostaId);
                    return new EntidadeNaoEncontradaException("Proposta não encontrada", HttpStatus.NOT_FOUND);
                });
        String documentoOfucado = Ofuscador.documento(proposta.getDocumento());
        logger.info("Proposta = {} documento {} pesquisada", propostaId, documentoOfucado);
        return ResponseEntity.ok(proposta);
    }
}
