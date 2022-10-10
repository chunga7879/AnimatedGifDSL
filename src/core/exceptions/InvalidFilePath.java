package core.exceptions;

public class InvalidFilePath extends DSLException {
    private final String msg;

    public InvalidFilePath(String msg) {
        this.msg = msg;
    }

    @Override
    public String message() {
        return this.msg;
    }
}
