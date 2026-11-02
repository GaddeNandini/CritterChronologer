package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet savePet(Pet pet, Long ownerId) {
        if (ownerId != null) {
            Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + ownerId));
            pet.setOwner(owner);
        }
        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    public Pet getPet(Long petId) {
        return petRepository.findById(petId)
            .orElseThrow(() -> new EntityNotFoundException("Pet not found: " + petId));
    }

    @Transactional(readOnly = true)
    public List<Pet> getPetsByOwner(Long ownerId) {
        if (!customerRepository.existsById(ownerId)) throw new EntityNotFoundException("Customer not found: " + ownerId);
        return petRepository.findByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)
    public List<Pet> getPetsByIds(List<Long> ids) { return petRepository.findAllById(ids); }
}
