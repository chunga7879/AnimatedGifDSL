package core;

public abstract class Node {
    private int linePosition = -1;
    private int columnPosition = -1;

    public void setPosition(int linePosition) {
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
    }

    public void setPosition(int linePosition, int columnPosition) {
        this.linePosition = linePosition;
        this.columnPosition = columnPosition;
    }

    public int getLinePosition() {
        return this.linePosition;
    }

    public int getColumnPosition() {
        return this.columnPosition;
    }

    public boolean isPositionSet() {
        return this.linePosition >= 0;
    }
}
