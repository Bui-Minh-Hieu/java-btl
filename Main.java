package Bai1;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    System.out.println("Le Thi Hoa\n");
    int n;
    Scanner sc = new Scanner(System.in);

    SinhVien_Controller list = new SinhVien_Controller(100);

    while (true) {
      System.out.println("Chon chuc nang:");
      System.out.println("1. Them sinh vien");
      System.out.println("2. Sua sinh vien");
      System.out.println("3. Xoa sinh vien");
      System.out.println("4. Tim kiem sinh vien theo ma");
      System.out.println("5. Hien thi danh sach sinh vien");
      System.out.println("0. Thoat");

      int choice = sc.nextInt();
      sc.nextLine();

      switch (choice) {
        case 1:
          System.out.println("Nhap so luong sinh vien: ");
          n = sc.nextInt();
          sc.nextLine();
          list.AddSV(n);
          list.hienThiDanhSach();
          break;
        case 2:
          System.out.print("Nhap ma sinh vien can sua: ");
          String maSVToEdit = sc.nextLine();
          list.FixSV(maSVToEdit);
          break;
        case 3:
          System.out.print("Nhap ma sinh vien de xoa: ");
          String maSVToDelete = sc.nextLine();
          list.DelSV(maSVToDelete);
          break;
        case 4:
          System.out.print("Nhap ma sinh vien can tim: "); // Changed prompt to match FindSV functionality
          String tenToFind = sc.nextLine();
          System.out.println("Thong tin sinh vien:");
          list.FindSV(tenToFind);
          break;
        case 5:
          list.hienThiDanhSach();
          break;
        case 0:
          System.out.println("Thoat chuong trinh.");
          sc.close();
          return;
        default:
          System.out.println("Lua chon khong hop le. Vui long chon lai");
      }
    }}
  }
// test deer pull ve
