package br.com.zup.adrianoavelino.proposta.proposta;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CartaoResponse {
    @NotBlank
    private String titular;

    @NotBlank
    @JsonProperty("id")
    private String numeroCartao;

    @NotNull
    private LocalDateTime emitidoEm;

    @NotNull
    @JsonProperty("idProposta")
    private Long propostaId;

    public CartaoResponse(String titular, String numeroCartao, LocalDateTime emitidoEm, Long propostaId) {
        this.titular = titular;
        this.numeroCartao = numeroCartao;
        this.emitidoEm = emitidoEm;
        this.propostaId = propostaId;
    }

    public Cartao toModel(PropostaRepository propostaRepository) {
        Proposta proposta = propostaRepository.findById(this.propostaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Proposta não encontrada", HttpStatus.NOT_FOUND));
        return new Cartao(this.titular, this.numeroCartao, emitidoEm, proposta);
    }

    public String getTitular() {
        return titular;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public Long getPropostaId() {
        return propostaId;
    }
}
