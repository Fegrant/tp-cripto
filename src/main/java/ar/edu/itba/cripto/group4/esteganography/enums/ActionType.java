package ar.edu.itba.cripto.group4.esteganography.enums;

public enum ActionType {
    EMBED,
    EXTRACT;

    public static ActionType getActionType(String actionType) {
        if (actionType == null) {
            throw new IllegalArgumentException("Action type cannot be null.");
        }

        return switch (actionType.toLowerCase()) {
            case "embed" -> ActionType.EMBED;
            case "extract" -> ActionType.EXTRACT;
            default -> throw new IllegalArgumentException("Invalid action type: " + actionType);
        };
    }
}

