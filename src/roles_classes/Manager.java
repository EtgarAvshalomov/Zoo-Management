package roles_classes;
import enums.CreatureType;
import java.util.Date;
// Manager Class
public class Manager extends Employee{
    private float budget;
    private CreatureType habitat;
    // Class Constructor
    public Manager(String id, String f_name, String l_name, Date b_date, float salary, float seniority, float budget) {
        super(id, f_name, l_name, b_date, salary,seniority);
        this.budget = budget;
    }
    // Public mathod to GET the budget of the manager
    public float getBudget() {
        return budget;
    }
    // Public mathod to SET the budget of the manager
    public void setBudget(float budget) {
        this.budget = budget;
    }
    // Public mathod to GET the habitat of the manager
    public CreatureType getHabitat() {
        return habitat;
    }
    // Public mathod to SET the habitat of the manager
    public void setHabitat(CreatureType habitat) {
        this.habitat = habitat;
    }
}
