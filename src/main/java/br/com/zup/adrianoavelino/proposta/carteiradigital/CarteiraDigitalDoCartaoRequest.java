package br.com.zup.adrianoavelino.proposta.carteiradigital;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CarteiraDigitalDoCartaoRequest {
    @NotBlank
    @Email
    private String email;

    @NotNull
    private TipoCarteiraDigital tipoCarteiraDigital;

    public CarteiraDigitalDoCartaoRequest() {
    }

    public CarteiraDigitalDoCartaoRequest(String email, TipoCarteiraDigital tipoCarteiraDigital) {
        this.email = email;
        this.tipoCarteiraDigital =tipoCarteiraDigital;
    }

    public CarteiraDigitalDoCartao toModel(Cartao cartao) {
        return new CarteiraDigitalDoCartao(this.email, this.tipoCarteiraDigital, cartao);
    }

    public String getEmail() {
        return email;
    }

    public TipoCarteiraDigital getTipoCarteiraDigital() {
        return tipoCarteiraDigital;
    }
}
