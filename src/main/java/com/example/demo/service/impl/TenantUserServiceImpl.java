package com.example.demo.service.impl;

import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.TenantEntity;
import com.example.demo.model.TenantUser;
import com.example.demo.repository.TenantEntityRepository;
import com.example.demo.service.TenantUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantUserServiceImpl implements TenantUserService {

    private final TenantEntityRepository repository;

    @Override
    public List<TenantUser> getTenantUsers() {
        List<TenantEntity> tenantEntities = repository.findAll();
        return tenantEntities.stream().map(entity -> {
            String roles = entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.joining(","));
            return new TenantUser(entity.getTenantId(), entity.getUsername(), roles);
        }).toList();
    }


}
