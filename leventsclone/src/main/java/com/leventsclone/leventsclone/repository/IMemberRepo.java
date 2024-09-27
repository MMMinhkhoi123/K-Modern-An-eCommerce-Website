package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMemberRepo extends JpaRepository<Member, Long> {
    public Optional<Member> findByPriceAchieveMember(Double price);

}
