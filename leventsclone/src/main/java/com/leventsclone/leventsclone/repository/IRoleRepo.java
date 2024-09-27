package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IRoleRepo extends JpaRepository<Role, Long> {
}
