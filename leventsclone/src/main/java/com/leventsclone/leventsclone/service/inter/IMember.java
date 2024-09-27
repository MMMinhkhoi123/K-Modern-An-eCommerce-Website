package com.leventsclone.leventsclone.service.inter;

import com.leventsclone.leventsclone.data.use.MemberUse;
import com.leventsclone.leventsclone.entity.Member;

import java.util.List;

public interface IMember {
    List<MemberUse> getAll();

    void delete(List<Long> list);
    MemberUse getById(Long id);
    Member getByIdF(Long id);

    Member getMemberInit();

    void save(MemberUse memberUse);

    void update(MemberUse memberUse);
    boolean checkPrice(Double price);
}
