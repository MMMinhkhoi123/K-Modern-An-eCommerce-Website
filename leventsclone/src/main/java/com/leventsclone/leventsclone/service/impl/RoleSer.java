package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.entity.Role;
import com.leventsclone.leventsclone.repository.IRoleRepo;
import com.leventsclone.leventsclone.service.inter.IRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleSer implements IRole {

    private final IRoleRepo ROLE_REPO;

    @Autowired
    public RoleSer(IRoleRepo iRoleRepo) {
        ROLE_REPO = iRoleRepo;
    }
    @Override
    public Role getRoleCustomer() {
        return ROLE_REPO.findById(2L).orElseThrow();
    }

    @Override
    public List<Role> getAll() {
        return ROLE_REPO.findAll();
    }
}
