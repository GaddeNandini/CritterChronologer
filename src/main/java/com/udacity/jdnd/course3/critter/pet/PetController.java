package com.udacity.jdnd.course3.critter.pet;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) { this.petService = petService; }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO dto) {
        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setType(dto.getType());
        pet.setBirthDate(dto.getBirthDate());
        pet.setNotes(dto.getNotes());
        return toDTO(petService.savePet(pet, dto.getOwnerId()));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable Long petId) { return toDTO(petService.getPet(petId)); }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable Long ownerId) {
        return petService.getPetsByOwner(ownerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setType(pet.getType());
        dto.setBirthDate(pet.getBirthDate());
        dto.setNotes(pet.getNotes());
        dto.setOwnerId(pet.getOwner() == null ? null : pet.getOwner().getId());
        return dto;
    }
}
