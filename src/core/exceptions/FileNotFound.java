package core.exceptions;

public class FileNotFound extends DSLException {
    private final String msg;

    public FileNotFound(String msg) {
        this.msg=msg;
    }
    @Override
    public String message() {
        return this.msg;
    }
}
