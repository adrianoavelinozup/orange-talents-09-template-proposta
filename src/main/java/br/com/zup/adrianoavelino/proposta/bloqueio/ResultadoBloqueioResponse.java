package br.com.zup.adrianoavelino.proposta.bloqueio;

public class ResultadoBloqueioResponse {
    private StatusCartaoResponse resultado;

    public ResultadoBloqueioResponse() {
    }

    public ResultadoBloqueioResponse(StatusCartaoResponse resultado) {
        this.resultado = resultado;
    }

    public StatusCartaoResponse getResultado() {
        return resultado;
    }
}
