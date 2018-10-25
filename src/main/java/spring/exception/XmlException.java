package spring.exception;

/**
 * xml异常
 */
public class XmlException extends RuntimeException {
	private Integer code;
	private String msg;

	public XmlException(String msg) {
		super(msg);
	}

}
