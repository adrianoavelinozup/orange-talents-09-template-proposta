package br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.validacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErroDto {
    private Integer status;
    private LocalDateTime data;
    private String descricao;
    private List<CampoComErro> erros = new ArrayList<>();

    public ErroDto(Integer status, LocalDateTime data, String descricao) {
        this.status = status;
        this.data = data;
        this.descricao = descricao;
    }

    public ErroDto(Integer status, LocalDateTime data, String descricao, List<CampoComErro> erros) {
        this.status = status;
        this.data = data;
        this.descricao = descricao;
        this.erros = erros = erros;
    }

    public void adiciona(CampoComErro campo) {
        this.erros.add(campo);
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<CampoComErro> getErros() {
        return erros;
    }
}
