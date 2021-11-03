package br.com.zup.adrianoavelino.proposta.avisoviagem;

public class ResultadoAvisoViagemResponse {
    private StatusAvisoViagemResponse resultado;

    public ResultadoAvisoViagemResponse() {
    }

    public ResultadoAvisoViagemResponse(StatusAvisoViagemResponse resultado) {
        this.resultado = resultado;
    }

    public StatusAvisoViagemResponse getResultado() {
        return resultado;
    }
}
