package SocketClient;



public class Notification {

	private static Notification instanz = null;
	private String text;
	private Notification() {
	}

	public static Notification getInstanz() {
		if (instanz == null)
			return new Notification();
		return instanz;

	}

	public String getText() {
		return text;
	}

	public void setText(String s) {
		this.text = s;
	}

}
