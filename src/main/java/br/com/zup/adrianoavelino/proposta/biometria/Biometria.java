package br.com.zup.adrianoavelino.proposta.biometria;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.Base64Invalido;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "biometrias")
public class Biometria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Lob
    @Column(nullable = false)
    private String impressaoDigital;

    @NotNull
    private LocalDateTime dataCriacao;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String impressaoDigital, Cartao cartao) {
        if (!ehImpressaoDigitalValida(impressaoDigital)) {
            throw new Base64Invalido("Impresão digital, no formato Base64, inválida", HttpStatus.BAD_REQUEST);
        }
        this.impressaoDigital = impressaoDigital;
        this.cartao = cartao;
        this.dataCriacao = LocalDateTime.now();
    }

    private boolean ehImpressaoDigitalValida(String impressaoDigital) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            decoder.decode(impressaoDigital);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Long getId() {
        return id;
    }
}
