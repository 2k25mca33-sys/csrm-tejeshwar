package com.example.demo.service;

import com.example.demo.entity.Resource;
import com.example.demo.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
    
    public List<Resource> getResourcesByType(Resource.ResourceType type) {
        return resourceRepository.findByType(type);
    }

    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }

    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource updateResource(Long id, Resource resourceDetails) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
        resource.setName(resourceDetails.getName());
        resource.setType(resourceDetails.getType());
        resource.setDescription(resourceDetails.getDescription());
        resource.setLocation(resourceDetails.getLocation());
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
}
