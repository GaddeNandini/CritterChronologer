
package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.schedule.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PetService petService;
    private final ScheduleService scheduleService;

    public UserController(UserService userService,
                          PetService petService,
                          ScheduleService scheduleService) {
        this.userService = userService;
        this.petService = petService;
        this.scheduleService = scheduleService;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setNotes(dto.getNotes());

        if (dto.getPetIds() != null && !dto.getPetIds().isEmpty()) {
            List<Pet> pets = petService.getPetsByIds(dto.getPetIds());

            if (pets.size() != dto.getPetIds().size()) {
                throw new IllegalArgumentException("One or more pet IDs are invalid");
            }

            customer.setPets(pets);
            pets.forEach(p -> p.setOwner(customer));
        }

        return toCustomerDTO(userService.saveCustomer(customer));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        return userService.getCustomers()
                .stream()
                .map(this::toCustomerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable Long petId) {
        return toCustomerDTO(userService.getCustomerByPet(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());

        employee.setSkills(dto.getSkills() == null
                ? new HashSet<>()
                : new HashSet<>(dto.getSkills()));

        employee.setWorkDays(dto.getDaysAvailable() == null
                ? new HashSet<>()
                : new HashSet<>(dto.getDaysAvailable()));

        return toEmployeeDTO(userService.saveEmployee(employee));
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable Long employeeId) {
        return toEmployeeDTO(userService.getEmployee(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public EmployeeDTO setAvailability(
            @RequestBody Set<DayOfWeek> daysAvailable,
            @PathVariable Long employeeId) {
        return toEmployeeDTO(
                userService.setEmployeeAvailability(daysAvailable, employeeId));
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(
            @RequestBody EmployeeRequestDTO request) {
        return userService.findEmployeesForService(request)
                .stream()
                .map(this::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    private CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setNotes(customer.getNotes());

        dto.setPetIds(customer.getPets() == null
                ? new ArrayList<>()
                : customer.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList()));

        return dto;
    }

    private EmployeeDTO toEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());

        dto.setSkills(employee.getSkills() == null
                ? new HashSet<>()
                : new HashSet<>(employee.getSkills()));

        dto.setDaysAvailable(employee.getWorkDays() == null
                ? new HashSet<>()
                : new HashSet<>(employee.getWorkDays()));

        return dto;
    }
}
//check update
