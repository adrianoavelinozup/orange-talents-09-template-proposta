package br.com.zup.adrianoavelino.proposta.biometria;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.EntidadeNaoEncontradaException;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import br.com.zup.adrianoavelino.proposta.proposta.CartaoRepository;
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

    @PostMapping("/{cartaoId}/biometrias")
    public ResponseEntity<?> cadastrar(@PathVariable("cartaoId") Long cartaoId,
                                       @Valid @RequestBody BiometriaRequest request,
                                       UriComponentsBuilder uriComponentsBuilder) {
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cartão não encontrado", HttpStatus.NOT_FOUND));
        Biometria biometria = request.toModel(cartao);
        biometriaRepository.save(biometria);
        URI uri = uriComponentsBuilder.path("/v1/cartoes/{idCartao}/biometrias/{id}")
                .buildAndExpand(cartaoId, biometria.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
