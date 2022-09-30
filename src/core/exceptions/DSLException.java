package core.exceptions;

public abstract class DSLException extends RuntimeException {
    public String name() {
        return this.getClass().getSimpleName();
    }

    public String message() {
        return this.name() + ": " + this.getDetails();
    }

    protected String getDetails() {
        return "";
    }
}
