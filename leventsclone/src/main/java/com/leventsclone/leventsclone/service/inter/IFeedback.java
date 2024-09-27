package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.FeedbackUse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IFeedback {
    List<FeedbackUse>  getAll();

    List<FeedbackUse>  getSwiper();

  FeedbackUse  getById(Long id);

    void save(MultipartFile multipartFile,Long idOption);

    void delete(List<Long> delete);
}
