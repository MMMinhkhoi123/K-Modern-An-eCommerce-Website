package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.response.RatingRes;
import com.leventsclone.leventsclone.data.use.RatingUse;
import com.leventsclone.leventsclone.entity.Option;
import com.leventsclone.leventsclone.entity.Orders;
import com.leventsclone.leventsclone.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IRating {
    void save(String tile, User user,
              String content, Optional<MultipartFile> multipartFile, Long idOpt, String codeOrder, Integer count);
    RatingRes getAllByPage(int page, Long idOption);

    RatingRes getAllByPageSet(int thisPage, int countStar, Long idOption);
    int getSumStarByIdProductAndIdColor(Long idProduct, Long idColor);
    List<RatingUse> getRatingByUserAndOption(User user, Long idOption);
    List<RatingUse> getAll();

    void  updateRating(Boolean state, Long idRating);
}
