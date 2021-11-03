package br.com.zup.adrianoavelino.proposta.avisoviagem;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SolicitacaoAvisoViagemRequest {
    @NotBlank
    @JsonProperty
    private String destino;

    @NotNull
    @FutureOrPresent
    @JsonProperty
    private LocalDate validoAte;

    public SolicitacaoAvisoViagemRequest(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }
}
