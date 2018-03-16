package visualization;

public class DependencyCommunication {
	private int posicao_i;
	private int posicao_j;
	private String msName;
	private String origin;
	private String destin;
	private int value;
	
	public DependencyCommunication(int posicao_i, int posicao_j, String msName) {
		this.posicao_i = posicao_i;
		this.posicao_j = posicao_j;
		this.msName = msName;
	}
	
	public DependencyCommunication(int posicao_i, int posicao_j, int value) {
		this.posicao_i = posicao_i;
		this.posicao_j = posicao_j;
		this.value = value;
	}
	public DependencyCommunication(String origin, String destin, int value) {
		this.origin = origin;
		this.destin = destin;
		this.value = value;
	}
	
	public DependencyCommunication(int posicao_i, int posicao_j, String origin, String destin, int value) {
		super();
		this.posicao_i = posicao_i;
		this.posicao_j = posicao_j;
		this.origin = origin;
		this.destin = destin;
		this.value = value;
	}

	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestin() {
		return destin;
	}
	public void setDestin(String destin) {
		this.destin = destin;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public DependencyCommunication() {}
	public int getPosicao_i() {
		return posicao_i;
	}
	public void setPosicao_i(int posicao_i) {
		this.posicao_i = posicao_i;
	}
	public int getPosicao_j() {
		return posicao_j;
	}
	public void setPosicao_j(int posicao_j) {
		this.posicao_j = posicao_j;
	}
	public String getMsName() {
		return msName;
	}
	public void setMsName(String msName) {
		this.msName = msName;
	}
	
}
