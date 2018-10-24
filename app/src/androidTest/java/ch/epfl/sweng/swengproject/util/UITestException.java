package ch.epfl.sweng.swengproject.util;

/**
 * This class may be used in the UnitTests when we want to throw an error
 */
public class UITestException extends RuntimeException {
    public UITestException(){
        super("A UITestException that may have been created from the UITests");
    }
}
