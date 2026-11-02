package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ScheduleDTO {
    private Long id;
    private List<Long> employeeIds;
    private List<Long> petIds;
    private LocalDate date;
    private Set<Activity> activities;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Long> getEmployeeIds() { return employeeIds; }
    public void setEmployeeIds(List<Long> employeeIds) { this.employeeIds = employeeIds; }
    public List<Long> getPetIds() { return petIds; }
    public void setPetIds(List<Long> petIds) { this.petIds = petIds; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Set<Activity> getActivities() { return activities; }
    public void setActivities(Set<Activity> activities) { this.activities = activities; }
}
