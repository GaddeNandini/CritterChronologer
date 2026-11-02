package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;
    private final ScheduleRepository scheduleRepository;

    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                       PetRepository petRepository, ScheduleRepository scheduleRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Customer saveCustomer(Customer customer) {
        if (customer.getPets() != null) {
            for (Pet pet : customer.getPets()) pet.setOwner(customer);
        }
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> getCustomers() { return customerRepository.findAll(); }

    @Transactional(readOnly = true)
    public Customer getCustomerByPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new EntityNotFoundException("Pet not found: " + petId));
        if (pet.getOwner() == null) throw new EntityNotFoundException("Pet has no owner: " + petId);
        return pet.getOwner();
    }

    public Employee saveEmployee(Employee employee) { return employeeRepository.save(employee); }

    @Transactional(readOnly = true)
    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
    }

    public Employee setEmployeeAvailability(Set<DayOfWeek> days, Long employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setWorkDays(days == null ? new HashSet<>() : new HashSet<>(days));
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<Employee> findEmployeesForService(EmployeeRequestDTO request) {
        if (request.getDate() == null) throw new IllegalArgumentException("date is required");
        Set<EmployeeSkill> required = request.getSkills() == null ? Set.of() : request.getSkills();
        DayOfWeek day = request.getDate().getDayOfWeek();
        return employeeRepository.findAll().stream()
            .filter(e -> e.getWorkDays() != null && e.getWorkDays().contains(day))
            .filter(e -> e.getSkills() != null && e.getSkills().containsAll(required))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForEmployee(Long id) {
        getEmployee(id);
        return scheduleRepository.findAll().stream()
            .filter(s -> s.getEmployees().stream().anyMatch(e -> e.getId().equals(id)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForPet(Long id) {
        petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pet not found: " + id));
        return scheduleRepository.findAll().stream()
            .filter(s -> s.getPets().stream().anyMatch(p -> p.getId().equals(id)))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesForCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + id));
        Set<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toSet());
        return scheduleRepository.findAll().stream()
            .filter(s -> s.getPets().stream().anyMatch(p -> petIds.contains(p.getId())))
            .collect(Collectors.toList());
    }
}
