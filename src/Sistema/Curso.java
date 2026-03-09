package Sistema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Curso {
    private int id;
    private String name;
    private double price;
    private int capacity;
    private double passingGrade;
    private int numPartialGrades;
    private Map<Alumno, List<Double>> partialGrades = new HashMap<>();
    private LocalDate promoStartDate;
    private LocalDate promoEndDate;
    private double promoPrice;
    private List<Alumno> enrolledStudents = new ArrayList<>();
    private Map<Alumno, Integer> finalGrades = new HashMap<>();

    public Curso(int id, String name, double price, int capacity, double passingGrade, int numPartialGrades,
            LocalDate promoStartDate, LocalDate promoEndDate, double promoPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.passingGrade = passingGrade;
        this.numPartialGrades = numPartialGrades;
        this.promoStartDate = promoStartDate;
        this.promoEndDate = promoEndDate;
        this.promoPrice = promoPrice;
        this.partialGrades = new HashMap<>();
        this.finalGrades = new HashMap<>();
    }

    public void addPartialGrade(Alumno student, double grade) {
        partialGrades.putIfAbsent(student, new ArrayList<>());
        partialGrades.get(student).add(grade);
    }

    public boolean studentCanTakeFinal(Alumno student) {
        List<Double> grades = partialGrades.get(student);
        if (grades == null || grades.size() < numPartialGrades)
            return false;
        for (Double g : grades) {
            if (g < passingGrade)
                return false;
        }
        return true;
    }

    public double calculatePriceByDate(LocalDate enrollmentDate) {
        if (promoStartDate != null && promoEndDate != null
                && !enrollmentDate.isBefore(promoStartDate)
                && !enrollmentDate.isAfter(promoEndDate)) {
            return promoPrice;
        }
        return price;
    }

    public double calculateRevenue() {
        return enrolledStudents.size() * price;
    }

    public String revenueReport() {
        return "Course: " + name + " | Students: " + enrolledStudents.size() +
                " | Price per student: $" + price +
                " | Total revenue: $" + calculateRevenue();
    }

    public void registerFinalGrade(Alumno student, int grade) {
        if (enrolledStudents.contains(student)) {
            finalGrades.put(student, grade);
        }
    }

    public long countApproved() {
        return finalGrades.entrySet()
                .stream()
                .filter(e -> e.getValue() >= passingGrade)
                .count();
    }

    public String approvedReport() {
        return "Course: " + name +
                " | Enrolled: " + enrolledStudents.size() +
                " | Approved: " + countApproved();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPassingGrade() {
        return passingGrade;
    }

    public void setPassingGrade(double passingGrade) {
        this.passingGrade = passingGrade;
    }

    public int getNumPartialGrades() {
        return numPartialGrades;
    }

    public void setNumPartialGrades(int numPartialGrades) {
        this.numPartialGrades = numPartialGrades;
    }

    public Map<Alumno, List<Double>> getPartialGrades() {
        return partialGrades;
    }

    public void setPartialGrades(Map<Alumno, List<Double>> partialGrades) {
        this.partialGrades = partialGrades;
    }

    public LocalDate getPromoStartDate() {
        return promoStartDate;
    }

    public void setPromoStartDate(LocalDate promoStartDate) {
        this.promoStartDate = promoStartDate;
    }

    public LocalDate getPromoEndDate() {
        return promoEndDate;
    }

    public void setPromoEndDate(LocalDate promoEndDate) {
        this.promoEndDate = promoEndDate;
    }

    public double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Map<Alumno, Integer> getFinalGrades() {
        return finalGrades;
    }

    public void setFinalGrades(Map<Alumno, Integer> finalGrades) {
        this.finalGrades = finalGrades;
    }

    @Override
    public String toString() {
        return id + " - " + name; // Show only ID + Name
    }
}
