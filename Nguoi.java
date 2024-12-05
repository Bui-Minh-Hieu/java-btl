public class Nguoi {
	private String name;
	private String sex;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Nguoi(String name, String sex) {
		super();
		this.name = name;
		this.sex = sex;
	}
	public Nguoi () {};
	@Override
	public String toString() {
		return "Ho va ten: " + name +"\nGioi tinh:  "+ sex;
	}
}
