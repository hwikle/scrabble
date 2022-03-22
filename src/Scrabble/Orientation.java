package Scrabble;

public enum Orientation {
    ACROSS,
    DOWN,
    BACKWARDS,
    UP,
    DIAG_ACROSS_DOWN,
    DIAG_ACROSS_UP,
    DIAG_BACK_DOWN,
    DIAG_BACK_UP;

    public Orientation reverse() {
        return switch (this) {
            case ACROSS -> BACKWARDS;
            case DOWN -> UP;
            case BACKWARDS -> ACROSS;
            case UP -> DOWN;
            case DIAG_ACROSS_DOWN -> DIAG_BACK_UP;
            case DIAG_ACROSS_UP -> DIAG_BACK_DOWN;
            case DIAG_BACK_DOWN -> DIAG_ACROSS_UP;
            case DIAG_BACK_UP -> DIAG_ACROSS_DOWN;
        };
    }

    public Orientation getPerpendicular() {
        return switch (this) {
            case ACROSS -> DOWN;
            case DOWN -> ACROSS;
            case BACKWARDS -> UP;
            case UP -> BACKWARDS;
            case DIAG_ACROSS_DOWN -> DIAG_ACROSS_UP;
            case DIAG_ACROSS_UP -> DIAG_ACROSS_DOWN;
            case DIAG_BACK_DOWN -> DIAG_BACK_UP;
            case DIAG_BACK_UP -> DIAG_BACK_DOWN;
        };
    }
}
