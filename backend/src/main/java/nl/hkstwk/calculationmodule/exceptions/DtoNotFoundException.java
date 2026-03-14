package nl.hkstwk.calculationmodule.exceptions;

public class DtoNotFoundException extends RuntimeException{
    public DtoNotFoundException(String message) {
        super(message);
    }
}
