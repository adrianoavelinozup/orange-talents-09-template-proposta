package br.com.zup.adrianoavelino.proposta.bloqueio;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueios_cartoes")
public class BloqueioCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @NotBlank
    @Column(nullable = false)
    private String ip;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime instante;

    @OneToOne
    @NotNull
    private Cartao cartao;

    @Deprecated
    public BloqueioCartao() {
    }

    public BloqueioCartao(String userAgent, String ip, Cartao cartao) {
        this.userAgent = userAgent;
        this.ip = ip;
        this.instante = LocalDateTime.now();
        this.cartao = cartao;
    }
}
