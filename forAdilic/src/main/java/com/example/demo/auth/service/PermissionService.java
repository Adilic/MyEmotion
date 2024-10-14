package com.example.demo.auth.service;

import com.example.demo.auth.model.Permission;
import com.example.demo.auth.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public  List<Permission> getAllPermissions(){
        return permissionRepository.findAll();

    }


}
