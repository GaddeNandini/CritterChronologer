package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Employee extends Users {
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<EmployeeSkill> skills = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> workDays = new HashSet<>();

    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules;

    public Set<EmployeeSkill> getSkills() { return skills; }
    public void setSkills(Set<EmployeeSkill> skills) { this.skills = skills; }
    public Set<DayOfWeek> getWorkDays() { return workDays; }
    public void setWorkDays(Set<DayOfWeek> workDays) { this.workDays = workDays; }
    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }
}
