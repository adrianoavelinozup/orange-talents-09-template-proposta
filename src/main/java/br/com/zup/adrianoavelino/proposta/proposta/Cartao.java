package br.com.zup.adrianoavelino.proposta.proposta;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "cartoes")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titular;

    @NotBlank
    @Column(nullable = false)
    private String numeroCartao;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime emitidoEm;

    @OneToOne
    @NotNull
    private Proposta proposta;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String titular, String numeroCartao, LocalDateTime emitidoEm, Proposta proposta) {
        this.titular = titular;
        this.numeroCartao = numeroCartao;
        this.emitidoEm = emitidoEm;
        this.proposta = proposta;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public Long getId() {
        return id;
    }
}
