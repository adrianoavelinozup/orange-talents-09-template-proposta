package br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.dto;

public class CampoComErro {
    private String nome;
    private String mensagem;

    public CampoComErro(String nome, String mensagem) {
        this.nome = nome;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public String getMensagem() {
        return mensagem;
    }
}
