public class SinhVien extends Nguoi{
	private String sV_ID;
	private String sV_Job;
	
	public String getsV_ID() {
		return sV_ID;
	}

	public void setsV_ID(String sV_ID) {
		this.sV_ID = sV_ID;
	}

	public String getsV_Job() {
		return sV_Job;
	}

	public void setsV_Job(String sV_Job) {
		this.sV_Job = sV_Job;
	}
	
	public SinhVien() {}

	public SinhVien(String name, String sex, String sV_ID, String sV_Job) {
		super(name, sex);
		this.sV_ID = sV_ID;
		this.sV_Job = sV_Job;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\nMa sinh vien: " + sV_ID + "\nNam: " + sV_Job;
	}
}
