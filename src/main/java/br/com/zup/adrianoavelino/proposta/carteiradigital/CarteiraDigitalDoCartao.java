package br.com.zup.adrianoavelino.proposta.carteiradigital;

import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.RegraDeNegocioException;
import br.com.zup.adrianoavelino.proposta.proposta.Cartao;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "carteiras_digitais")
public class CarteiraDigitalDoCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCarteiraDigital tipoCarteiraDigital;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

    @Deprecated
    public CarteiraDigitalDoCartao() {
    }

    public CarteiraDigitalDoCartao(String email, TipoCarteiraDigital tipoCarteiraDigital, Cartao cartao) {
        this.email = email;
        this.tipoCarteiraDigital = tipoCarteiraDigital;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public TipoCarteiraDigital getTipoCarteiraDigital() {
        return tipoCarteiraDigital;
    }

    public String getEmail() {
        return email;
    }

    public boolean jaEstaCadastrada(CarteiraDigitalDoCartaoRepository repository) {
        return repository.existsByTipoCarteiraDigitalAndCartaoId(this.tipoCarteiraDigital, this.cartao.getId());
    }
}
