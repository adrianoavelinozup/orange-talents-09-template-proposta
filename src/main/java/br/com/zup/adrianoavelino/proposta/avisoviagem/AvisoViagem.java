package br.com.zup.adrianoavelino.proposta.avisoviagem;

import br.com.zup.adrianoavelino.proposta.proposta.Cartao;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "avisos_viagens")
public class AvisoViagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String destino;

    @NotBlank
    @Column(nullable = false)
    private String userAgent;

    @NotBlank
    @Column(nullable = false)
    private String ip;

    @NotNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate dataTermino;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;
    
    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(String destino, String userAgent, String ip, LocalDate dataTermino, Cartao cartao) {
        this.destino = destino;
        this.userAgent = userAgent;
        this.ip = ip;
        this.dataTermino = dataTermino;
        this.cartao = cartao;
        this.dataCriacao = LocalDateTime.now();
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }
}
