package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.carteiradigital.CarteiraDigitalDoCartao;
import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.RegraDeNegocioException;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCartao status;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String titular, String numeroCartao, LocalDateTime emitidoEm, Proposta proposta) {
        this.titular = titular;
        this.numeroCartao = numeroCartao;
        this.emitidoEm = emitidoEm;
        this.proposta = proposta;
        this.status = StatusCartao.DESBLOQUEADO;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public Long getId() {
        return id;
    }

    public void bloquear() {
        this.status = StatusCartao.BLOQUEADO;
    }
}
