package br.com.zup.adrianoavelino.proposta.biometria;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.EntidadeNaoEncontradaException;
import br.com.zup.adrianoavelino.proposta.compartilhada.seguranca.Ofuscador;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/cartoes")
public class BiometriaController {
    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BiometriaRepository biometriaRepository;

    private final Logger logger = LoggerFactory.getLogger(BiometriaController.class);

    @PostMapping("/{cartaoId}/biometrias")
    public ResponseEntity<Object> cadastrar(@PathVariable("cartaoId") Long cartaoId,
                                       @Valid @RequestBody BiometriaRequest request,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> {
                    logger.warn("Cartão id={} não encontrado", cartaoId);
                    return new EntidadeNaoEncontradaException("Cartão não encontrado", HttpStatus.NOT_FOUND);
                });
        Biometria biometria = request.toModel(cartao);
        biometriaRepository.save(biometria);
        logger.info("Biometria id={} cartão {} adicionada com sucesso!", biometria.getId(), Ofuscador.numeroCartao(cartao.getNumeroCartao()));
        URI uri = uriComponentsBuilder.path("/v1/cartoes/{idCartao}/biometrias/{id}")
                .buildAndExpand(cartaoId, biometria.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
