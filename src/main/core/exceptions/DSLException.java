package core.exceptions;

import core.Node;

public abstract class DSLException extends RuntimeException {
    private final String defaultDetails;
    private int linePosition = -1;
    private int columnPosition = -1;

    public DSLException(String defaultDetails) {
        this.defaultDetails = defaultDetails;
    }
    public DSLException() {
        this("");
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getMessage() {
        return this.name() + this.getPositionText() + ": " + this.getDetails();
    }

    protected String getDetails() {
        return this.defaultDetails;
    }


    //region Position methods
    public int getLinePosition() {
        return linePosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }

    /**
     * Get formatted position text
     * @return
     */
    protected String getPositionText() {
        if (linePosition < 0) return "";
        if (columnPosition < 0) return " [Line " + linePosition + "]";
        return " [Line " + linePosition + ":" + columnPosition + "]";
    }

    /**
     * Check if position is set
     * @return
     */
    private boolean isPositionSet() {
        return this.linePosition >= 0;
    }


    /**
     * Set line, column position of the exception
     * @param linePosition
     * @param columnPosition
     * @return
     */
    public DSLException withPosition(int linePosition, int columnPosition) {
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
        return this;
    }

    /**
     * Set line, column position of the exception if it is set in the Node and it is not already set
     * @param node
     * @return
     */
    public DSLException withPosition(Node node) {
        return !this.isPositionSet() && node.isPositionSet() ?
            this.withPosition(node.getLinePosition(), node.getColumnPosition()):
            this;
    }
    //endregion
}
