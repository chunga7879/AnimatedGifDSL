package core.checkers;

import core.values.Unknown;
import core.values.Value;

public final class TypeChecker {
    /**
     * Check if the type of the value is typeName or Unknown
     * @param value Value we are checking the type of
     * @param typeName Type we are checking against - Unknown means value can be any type
     * @return Returns
     */
    public static boolean checkValueIsTypeOrUnknown(Value value, String typeName) {
        String valueTypeName = value.getTypeName();
        return valueTypeName.equals(typeName) || valueTypeName.equals(Unknown.NAME) || typeName.equals(Unknown.NAME);
    }

    /**
     * Check if the type of the value is typeName
     * @param value Value we are checking the type of
     * @param typeName Type we are checking against - Unknown means value can be any type
     * @return
     */
    public static boolean checkValueIsType(Value value, String typeName) {
        String valueTypeName = value.getTypeName();
        return valueTypeName.equals(typeName) || typeName.equals(Unknown.NAME);
    }

    /**
     * Check if the type of the value is Unknown
     * @param value
     * @return
     */
    public static boolean checkValueIsUnknown(Value value) {
        return value.getTypeName().equals(Unknown.NAME);
    }
}
