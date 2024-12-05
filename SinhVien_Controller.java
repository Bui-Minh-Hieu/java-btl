import java.util.Scanner;

public class SinhVien_Controller implements Controller{
	private SinhVien[] list_SV;
	private int viTri;
	
	Scanner sc = new Scanner(System.in);
	
	public SinhVien_Controller(int maxSV)
	{
		list_SV = new SinhVien[maxSV];
		viTri = 0;
	}
	
	@Override
	public void AddSV(int n) {
		
		for(int i = viTri; i < viTri+n; i++) {
			
			System.out.println("Nhap thong tin moi cua sinh vien \n");
			System.out.println("Nhap ten: ");
			String name = sc.nextLine();
			System.out.println("\nNhap gioi tinh: ");
			String sex = sc.nextLine();
			System.out.println("\nNhap ma sinh vien: ");
			String iD= sc.nextLine();
			System.out.println("\nNhap nghe nghiep: ");
			String job=sc.nextLine();
			
			SinhVien sV = new SinhVien(name,sex,iD,job);
			list_SV[i] = sV;
		}
		viTri+=n;
		System.out.println("Them sv thanh cong");
		
	}
	@Override
	public void FixSV(String iDSua) {
		for(int i = 0; i < viTri; i++) {
			if(list_SV[i].getsV_ID().equals(iDSua)) {
				System.out.println("Nhap thong tin moi cua sinh vien \n");
				System.out.println("Nhap ten: ");
				list_SV[i].setName(sc.nextLine());
				System.out.println("\nNhap gioi tinh: ");
				list_SV[i].setSex(sc.nextLine());
				System.out.println("\nNhap ma sinh vien: ");
				list_SV[i].setsV_ID(sc.nextLine());
				System.out.println("\nNhap nghe nghiep: ");
				list_SV[i].setsV_Job(sc.nextLine());
				System.out.println("Sua xong");
				return;
			}
		}
	}	
	@Override
	public void DelSV(String iDSua) {
		for(int i = 0; i < viTri; i++) {
			if(list_SV[i].getsV_ID().equals(iDSua)) {
				list_SV[i].setName(list_SV[viTri-1].getName());
				list_SV[i].setSex(list_SV[viTri-1].getSex());
				list_SV[i].setsV_ID(list_SV[viTri-1].getsV_ID());
				list_SV[i].setsV_Job(list_SV[viTri-1].getsV_Job());
				list_SV[viTri-1]=null;
				System.out.println("Xoa thanh cong.");
				viTri-=1;
				return;
			}
		}
	}
	
	@Override
	public void FindSV(String iDSua) {
		System.out.println("Sinh vien can tim: ");
		for(int i = 0; i < viTri; i++) {
			if(list_SV[i].getsV_ID().equals(iDSua)) {
				System.out.println(list_SV[i].toString());
				return;
			}
		}
	}
	
	public void hienThiDanhSach() {
	    for (int i = 0; i < viTri; i++) {
	      hienThiSinhVien(list_SV[i]);
	    }
	  }

	  public void hienThiSinhVien(SinhVien sv) {
	    System.out.println("Ho Ten: " + sv.getName());
	    System.out.println("Gioi tinh: " + sv.getSex());
	    System.out.println("Ma sinh vien: " + sv.getsV_ID());
	    System.out.println("Nganh nghe: " + sv.getsV_Job());
	    System.out.println("---------------------");
	  }
}
