package br.com.zup.adrianoavelino.proposta.carteiradigital;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApiCartaoCarteiraDigitalRequest {
    @NotBlank
    @Email
    @JsonProperty
    private String email;

    @NotNull
    @JsonProperty
    private TipoCarteiraDigital tipoCarteiraDigital;

    public ApiCartaoCarteiraDigitalRequest(@NotNull CarteiraDigitalDoCartao carteiraDigitalDoCartao) {
        this.email = carteiraDigitalDoCartao.getEmail();
        this.tipoCarteiraDigital = carteiraDigitalDoCartao.getTipoCarteiraDigital();
    }

    public ApiCartaoCarteiraDigitalRequest(String email, TipoCarteiraDigital tipoCarteiraDigital) {
        this.email = email;
        this.tipoCarteiraDigital = tipoCarteiraDigital;
    }

    public ApiCartaoCarteiraDigitalRequest() {
    }
}
