CREATE DATABASE qlsv
go
use qlsv
go
CREATE TABLE Admin (
    admin_ID VARCHAR(50) PRIMARY KEY, -- Tự động tăng ID
    admin_Name NVARCHAR(255) NOT NULL,
    pass VARCHAR(255) NOT NULL -- Nên mã hóa mật khẩu trước khi lưu
);
go
CREATE TABLE SinhVien (
    sV_ID VARCHAR(20) PRIMARY KEY, -- Mã sinh viên có thể là dạng chuỗi
    sV_Name NVARCHAR(255) NOT NULL,
    class VARCHAR(20),
    cCCD VARCHAR(20),
    email VARCHAR(255) UNIQUE
);
go
CREATE TABLE HocPhan (
    subject_ID VARCHAR(20) PRIMARY KEY,
    subject_Name NVARCHAR(255) NOT NULL,
    tin_Chi INT,
    price FLOAT -- Giá học phần
);
go
CREATE TABLE Thi (
    exam_ID VARCHAR(20) PRIMARY KEY,
    subject_ID VARCHAR(20) UNIQUE NOT NULL, -- Mỗi học phần chỉ có một kỳ thi
    exam_Date DATETIME, -- Ngày và giờ thi
    FOREIGN KEY (subject_ID) REFERENCES HocPhan(subject_ID)
);
go 
CREATE TABLE SinhVien_HocPhan (
    sV_ID VARCHAR(20),
    subject_ID VARCHAR(20),
    score FLOAT, -- Điểm số của sinh viên trong học phần (có thể dùng DECIMAL nếu cần độ chính xác cao hơn)
    PRIMARY KEY (sV_ID, subject_ID),
    FOREIGN KEY (sV_ID) REFERENCES SinhVien(sV_ID),
    FOREIGN KEY (subject_ID) REFERENCES HocPhan(subject_ID)
);


-- Insert data into Admin table
INSERT INTO Admin (admin_ID, admin_Name, pass)
VALUES
    ('admin1', 'Bui Minh Hieu', '123'),
    ('admin2', 'Hoang Ngoc Anh', '123');

-- Insert data into SinhVien table
INSERT INTO SinhVien (sV_ID, sV_Name, class, cCCD, email)
VALUES
    ('SV001', 'Nguyen Van A', 'K62', '123456789', 'nguyenvanA@example.com'),
    ('SV002', 'Tran Thi B', 'K62', '987654321', 'tranthiB@example.com'),
    ('SV003', 'Le Van C', 'K63', '111222333', 'levanC@example.com');

-- Insert data into HocPhan table
INSERT INTO HocPhan (subject_ID, subject_Name, tin_Chi, price)
VALUES
    ('HP001', 'Database Systems', 3, 500000),
    ('HP002', 'Programming Fundamentals', 4, 600000),
    ('HP003', 'Data Structures and Algorithms', 3, 450000);

-- Insert data into Thi table
INSERT INTO Thi (exam_ID, subject_ID, exam_Date)
VALUES
    ('TH001', 'HP001', '2023-12-15 14:00:00'),
    ('TH002', 'HP002', '2023-12-20 09:00:00'),
    ('TH003', 'HP003', '2023-12-22 13:30:00');

-- Insert data into SinhVien_HocPhan table
INSERT INTO SinhVien_HocPhan (sV_ID, subject_ID, score)
VALUES
    ('SV001', 'HP001', 8.5),
    ('SV001', 'HP002', 7.0),
    ('SV002', 'HP001', 9.0),
    ('SV002', 'HP003', 6.5),
    ('SV003', 'HP002', 8.2);