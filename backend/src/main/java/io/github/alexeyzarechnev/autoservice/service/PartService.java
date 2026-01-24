package io.github.alexeyzarechnev.autoservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.alexeyzarechnev.autoservice.model.Part;
import io.github.alexeyzarechnev.autoservice.repository.PartRepository;

@Service
public class PartService {
    
    private final PartRepository partRepository;

    @Autowired
    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public Part savePart(Part part) {
        if (!validatePart(part)) {
            throw new IllegalArgumentException("Invalid part data");
        }
        return partRepository.save(part);
    }

    public Part getPartById(Long id) {
        return partRepository.findById(id).orElse(null);
    }

    public Part updatePart(Long id, Part updatedPart) {
        Part existingPart = partRepository.findById(id).orElse(null);
        if (existingPart == null) {
            return null;
        }
        if (!validatePart(updatedPart)) {
            throw new IllegalArgumentException("Invalid part data");
        }
        existingPart.setName(updatedPart.getName());
        existingPart.setPrice(updatedPart.getPrice());
        existingPart.setRemains(updatedPart.getRemains());
        return partRepository.save(existingPart);
    }

    public boolean deletePart(Long id) {
        if (!partRepository.existsById(id)) {
            return false;
        }
        partRepository.deleteById(id);
        return true;
    }

    private boolean validatePart(Part part) {
        if (part.getName() == null || part.getName().isEmpty()) {
            return false;
        }
        if (part.getPrice() < 0) {
            return false;
        }
        if (part.getRemains() < 0) {
            return false;
        }
        return true;
    }

}
