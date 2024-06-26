package it.polimi.ingsw.view;

/**
 * This class implements functions that are common to both the UI implementations.
 */
public class UIFunctions {
    /**
     * Non-public constructor, as this class is not meant to be instantiated.
     */
    protected UIFunctions(){}

    /**
     * Evaluates which is the cause of the error while registering nickname on server.
     * @param nickname user chosen identifier
     * @return a string indicating error cause
     */
    public static String evaluateErrorType(String nickname){
        String error ="Invalid nickname!";

        if(nickname.length() < 3)
            error += "\n- Use at least three characters.";
        if(nickname.length() > Client.MAX_NICKNAME_LENGTH)
            error += "\n- Nickname is too long. Do not use more than " + Client.MAX_NICKNAME_LENGTH + " characters";
        if(!nickname.matches(".*[a-zA-Z].*"))
            error += "\n- Nickname must contain at least one letter";
        if(!nickname.matches("[^\n ].*"))
            error += "\n- Nickname can't begin with a space";
        if(!nickname.matches(".*[^\n ]"))
            error += "\n- Nickname can't end with a space";

        return error;
    }
}
