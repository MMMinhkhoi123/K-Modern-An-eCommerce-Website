package com.leventsclone.leventsclone.repository;

import com.leventsclone.leventsclone.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFeedbackRepo extends JpaRepository<Feedback, Long> {
}
