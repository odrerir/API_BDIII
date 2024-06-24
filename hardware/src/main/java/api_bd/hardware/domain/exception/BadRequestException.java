package api_bd.hardware.domain.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensagem){
        super(mensagem);
    }
    
}
