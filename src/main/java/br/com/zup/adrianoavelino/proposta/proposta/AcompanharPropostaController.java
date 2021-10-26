package br.com.zup.adrianoavelino.proposta.proposta;

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
    @Autowired
    private PropostaRepository propostaRepository;

    @GetMapping("/{propostaId}")
    public ResponseEntity<?> pesquisar(@PathVariable Long propostaId) {
        Proposta proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Proposta não encontrada", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(proposta);
    }

}
