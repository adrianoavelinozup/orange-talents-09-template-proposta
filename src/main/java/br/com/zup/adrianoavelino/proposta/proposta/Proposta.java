package br.com.zup.adrianoavelino.proposta.proposta;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.DocumentoValido;
import br.com.zup.adrianoavelino.proposta.compartilhada.excecoes.PropostaNaoElegivelException;
import br.com.zup.adrianoavelino.proposta.compartilhada.seguranca.CriptadorConverter;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "propostas")
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DocumentoValido
    @NotBlank
    @Column(unique = true, nullable = false)
    @Convert(converter = CriptadorConverter.class)
    private String documento;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String endereco;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta;

    @OneToOne(cascade = CascadeType.MERGE)
    private Cartao cartao;

    @Deprecated
    public Proposta() {
    }

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public String getDocumento() {
        return documento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getNumeroDoCartao() {
        if (cartao != null) return cartao.getNumeroCartao();
        return null;
    }

    public boolean ehRepetida(PropostaRepository propostaRepository) {
        return propostaRepository.existsByDocumento(this.documento);
    }

    public void adicionaStatus(StatusProposta statusProposta) {
        this.statusProposta = statusProposta;
    }

    public void associarCartao(Cartao cartao) {
        if (!this.statusProposta.equals(StatusProposta.ELEGIVEL)) {
            throw new PropostaNaoElegivelException("Não é possível adicionar cartão para uma proposta não elegível", HttpStatus.BAD_REQUEST);
        }
        this.cartao = cartao;
    }
}
