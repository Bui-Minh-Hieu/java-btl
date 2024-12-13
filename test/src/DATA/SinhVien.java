package DATA;

public class SinhVien {
    private String sV_ID;
    private String sV_Name;
    private String class_Name;
    private String cCCD;
    private String email;
    
	public String getsV_ID() {
		return sV_ID;
	}
	public void setsV_ID(String sV_ID) {
		this.sV_ID = sV_ID;
	}
	public String getsV_Name() {
		return sV_Name;
	}
	public void setsV_Name(String sV_Name) {
		this.sV_Name = sV_Name;
	}
	public String getClass_Name() {
		return class_Name;
	}
	public void setClass_Name(String class_Name) {
		this.class_Name = class_Name;
	}
	public String getcCCD() {
		return cCCD;
	}
	public void setcCCD(String cCCD) {
		this.cCCD = cCCD;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public SinhVien(String sV_ID, String sV_Name, String class_Name, String cCCD, String email) {
		this.sV_ID = sV_ID;
		this.sV_Name = sV_Name;
		this.class_Name = class_Name;
		this.cCCD = cCCD;
		this.email = email;
	}
	public SinhVien() {}
		
	public Object[] toArray() {
        return new Object[]{"", sV_ID, sV_Name, class_Name, cCCD, email};
    }
	
	@Override
	public String toString() {
		return "SinhVien [sV_ID=" + sV_ID + ", sV_Name=" + sV_Name + ", class_Name=" + class_Name + ", cCCD=" + cCCD
				+ ", email=" + email + "]";
	}

}

