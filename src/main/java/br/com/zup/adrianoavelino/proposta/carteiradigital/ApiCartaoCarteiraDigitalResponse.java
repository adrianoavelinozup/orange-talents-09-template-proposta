package br.com.zup.adrianoavelino.proposta.carteiradigital;

public class ApiCartaoCarteiraDigitalResponse {
    private StatusCarteiraDigitalResponse resultado;
    private String id;

    public ApiCartaoCarteiraDigitalResponse() {
    }

    public ApiCartaoCarteiraDigitalResponse(StatusCarteiraDigitalResponse resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public StatusCarteiraDigitalResponse getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
