package ca.usask.cs.srlab.simcad;

public class SimcadException extends RuntimeException {

	private static final long serialVersionUID = 3760196947658248202L;

	public SimcadException() {
		super();
	}

	public SimcadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SimcadException(String arg0) {
		super(arg0);
	}

	public SimcadException(Throwable arg0) {
		super(arg0);
	}

}
