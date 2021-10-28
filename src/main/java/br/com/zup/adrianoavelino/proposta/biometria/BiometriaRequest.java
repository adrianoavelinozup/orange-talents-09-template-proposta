package br.com.zup.adrianoavelino.proposta.biometria;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {
    @NotBlank
    @JsonProperty
    private String impressaoDigital;

    public BiometriaRequest() {
    }

    public BiometriaRequest(String impressaoDigital) {
        this.impressaoDigital = impressaoDigital;
    }

    public Biometria toModel(Cartao cartao) {
        return new Biometria(this.impressaoDigital, cartao);
    }

    public String getImpressaoDigital() {
        return impressaoDigital;
    }
}
