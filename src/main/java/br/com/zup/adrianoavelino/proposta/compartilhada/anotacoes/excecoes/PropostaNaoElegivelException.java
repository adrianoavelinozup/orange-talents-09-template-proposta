package br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes;

public class PropostaNaoElegivelException extends RegraDeNegocioException {
    public PropostaNaoElegivelException(String mensagem) {
        super(mensagem);
    }
}
