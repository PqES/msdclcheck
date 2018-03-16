package visualization;

public class GraphCommunication {
	private String msOrigin;
	private String msDestin;
	private int value;
	public GraphCommunication(String msOrigin, String msDestin, int value) {
		super();
		this.msOrigin = msOrigin;
		this.msDestin = msDestin;
		this.value = 0;
	}
	public String getMsOrigin() {
		return msOrigin;
	}
	public void setMsOrigin(String msOrigin) {
		this.msOrigin = msOrigin;
	}
	public String getMsDestin() {
		return msDestin;
	}
	public void setMsDestin(String msDestin) {
		this.msDestin = msDestin;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
