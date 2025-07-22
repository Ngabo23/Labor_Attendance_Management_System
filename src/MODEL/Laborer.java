/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author Donovan
 */
public class Laborer {
    public int id;
    public String name;
    private String position;
    private double wagePerDay;
    private String national_id;
 

    public Laborer(int id, String name, String position, double wagePerDay, String national_id ) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.wagePerDay = wagePerDay;
        this.national_id = national_id;
    }

    public Laborer(String name, String position, double wagePerDay, String national_id) {
        this.name = name;
        this.position = position;
        this.wagePerDay = wagePerDay;
        this.national_id = national_id;
    }
@Override
public String toString() {
    return name + " (" + position + ")";
}



    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getWagePerDay() { return wagePerDay; }
    public String getNationalId() { return national_id; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setWagePerDay(double wagePerDay) { this.wagePerDay = wagePerDay; }
    public void setNationalId(String nationalId) { this.national_id = nationalId; }
}
