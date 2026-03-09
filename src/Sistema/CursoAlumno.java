package Sistema;

public class CursoAlumno {
    private int courseId;
    private int studentId;
    private Double partialGrade;
    private Integer finalGrade;

    public CursoAlumno(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public CursoAlumno(int courseId, int studentId, Double partialGrade, Integer finalGrade) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.partialGrade = partialGrade;
        this.finalGrade = finalGrade;
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Double getPartialGrade() {
        return partialGrade;
    }

    public void setPartialGrade(Double partialGrade) {
        this.partialGrade = partialGrade;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Integer finalGrade) {
        this.finalGrade = finalGrade;
    }

    @Override
    public String toString() {
        return "CursoAlumno{" +
                "courseId=" + courseId +
                ", studentId=" + studentId +
                ", partialGrade=" + partialGrade +
                ", finalGrade=" + finalGrade +
                '}';
    }
}
