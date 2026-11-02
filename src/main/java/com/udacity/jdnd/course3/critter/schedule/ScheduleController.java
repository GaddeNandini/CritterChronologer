package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    public ScheduleController(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO dto) {
        Schedule schedule = new Schedule();
        schedule.setDate(dto.getDate());
        schedule.setActivities(dto.getActivities() == null ? new HashSet<>() : new HashSet<>(dto.getActivities()));
        Schedule saved = scheduleService.createSchedule(schedule,
            dto.getEmployeeIds() == null ? List.of() : dto.getEmployeeIds(),
            dto.getPetIds() == null ? List.of() : dto.getPetIds());
        return toDTO(saved);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable Long employeeId) {
        return userService.getSchedulesForEmployee(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable Long petId) {
        return userService.getSchedulesForPet(petId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable Long customerId) {
        return userService.getSchedulesForCustomer(customerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ScheduleDTO toDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setActivities(schedule.getActivities() == null ? new HashSet<>() : new HashSet<>(schedule.getActivities()));
        dto.setEmployeeIds(schedule.getEmployees() == null ? new ArrayList<>() :
            schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        dto.setPetIds(schedule.getPets() == null ? new ArrayList<>() :
            schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        return dto;
    }
}
