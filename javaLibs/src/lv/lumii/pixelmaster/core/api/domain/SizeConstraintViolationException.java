
package lv.lumii.pixelmaster.core.api.domain;

/**
 * Exception that is thrown when an image is two big or too small.
 *
 * @author Jevgeny Jonas
 */
public final class SizeConstraintViolationException extends Exception {

	public SizeConstraintViolationException(String message) {
		super(message);
	}
}
