package pl.majkus522.mrpg.common;

public class SkillData
{
    public String label;
    public boolean active;
    public String description;

    @Override
    public String toString() {
        return "SkillData{" +
                "label='" + label + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }
}