package br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.validacao;

import br.com.zup.adrianoavelino.proposta.compartilhada.anotacoes.excecoes.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandlerAdvice {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErroDto handle(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        ErroDto erro = new ErroDto(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), "Um ou mais campos estão com problemas");

        fieldErrors.forEach(e -> {
            String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            erro.adiciona(new CampoComErro(e.getField(),mensagem));
        });

        return erro;
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroDto> handlePropostaRepetida(RegraDeNegocioException exception) {
        ErroDto erroDto = new ErroDto(exception.getHttpStatus().value(), LocalDateTime.now(), exception.getMessage(), null);
        return ResponseEntity.status(exception.getHttpStatus()).body(erroDto);
    }

}
