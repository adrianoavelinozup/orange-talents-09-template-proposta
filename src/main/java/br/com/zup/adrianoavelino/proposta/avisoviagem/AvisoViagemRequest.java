package br.com.zup.adrianoavelino.proposta.avisoviagem;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.text.Caret;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoViagemRequest {
    @NotBlank
    @JsonProperty
    private String destino;



    @NotNull
    @FutureOrPresent
    @JsonProperty
    private LocalDate dataTermino;

    public AvisoViagemRequest(String destino, LocalDate dataTermino) {
        this.destino = destino;
        this.dataTermino = dataTermino;
    }

    public AvisoViagem toModel(String userAgent, String ip, Cartao cartao) {
        return new AvisoViagem(this.destino, userAgent, ip, this.dataTermino, cartao);
    }
}
