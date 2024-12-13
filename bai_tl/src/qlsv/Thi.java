package qlsv;

import java.util.Date;

public class Thi {
    private String exam_ID;
    private String subject_ID;
    private Date exam_Date;

    public Thi(String exam_ID, String subject_ID, Date exam_Date) {
        this.exam_ID = exam_ID;
        this.subject_ID = subject_ID;
        this.exam_Date = exam_Date;
    }

    public String getExam_ID() {
        return exam_ID;
    }

    public void setExam_ID(String exam_ID) {
        this.exam_ID = exam_ID;
    }

    public String getSubject_ID() {
        return subject_ID;
    }

    public void setSubject_ID(String subject_ID) {
        this.subject_ID = subject_ID;
    }

    public Date getExam_Date() {
        return exam_Date;
    }

    public void setExam_Date(Date exam_Date) {
        this.exam_Date = exam_Date;
    }

    public Object[] toArray() {
        return new Object[]{exam_ID, subject_ID, exam_Date};
    }
}