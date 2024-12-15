package DATA;

public class HocPhan {
	private String subject_ID;
	private String subject_Name;
	private int tin_chi;
	private float price;
	
	public String getSubject_ID() {
		return subject_ID;
	}
	public void setSubject_ID(String subject_ID) {
		this.subject_ID = subject_ID;
	}
	public String getSubject_Name() {
		return subject_Name;
	}
	public void setSubject_Name(String subject_Name) {
		this.subject_Name = subject_Name;
	}
	public int getTin_chi() {
		return tin_chi;
	}
	public void setTin_chi(int tin_chi) {
		this.tin_chi = tin_chi;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public HocPhan() {}
	public HocPhan(String sub_ID,String sub_Name,int credit,float price) {
		this.subject_ID=sub_ID;
		this.subject_Name=sub_Name;
		this.tin_chi=credit;
		this.price=price;
	}
	public Object[] toArray() {
        return new Object[]{"", this.subject_ID, this.subject_Name, this.tin_chi, this.price};
    }
	
	@Override
	public String toString() {
		return "SinhVien [sub_ID=" + subject_ID + ", sub_Name=" + subject_Name + ", credit=" + tin_chi + ", price=" + price+ "]";
	}
}
