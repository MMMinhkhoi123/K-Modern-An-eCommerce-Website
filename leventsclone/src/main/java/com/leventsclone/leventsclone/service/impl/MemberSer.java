package com.leventsclone.leventsclone.service.impl;

import com.leventsclone.leventsclone.data.use.ColorUse;
import com.leventsclone.leventsclone.data.use.MemberUse;
import com.leventsclone.leventsclone.entity.Member;
import com.leventsclone.leventsclone.repository.IMemberRepo;
import com.leventsclone.leventsclone.service.inter.IMember;
import com.leventsclone.leventsclone.unstil.ConvertToOtherData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class MemberSer implements IMember {

    private final IMemberRepo MEMBER_REPO;

    private final ConvertToOtherData convertToOtherData = new ConvertToOtherData();

    public MemberSer(IMemberRepo iMemberRepo) {
        MEMBER_REPO = iMemberRepo;
    }


    private void sort(List<MemberUse> colorUses) {
        Comparator<MemberUse> resComparator = new Comparator<MemberUse>() {
            @Override
            public int compare(MemberUse o1, MemberUse o2) {
                return Double.compare(o2.getId() , o1.getId());
            }
        };
        colorUses.sort(resComparator);
    }
    @Override
    public List<MemberUse> getAll() {
        List<MemberUse> memberUses = new ArrayList<>();
        MEMBER_REPO.findAll().forEach((e) -> {
            MemberUse memberUse = convertToOtherData.getMemberUse(e);

            Predicate<Member> streamsPredicate = item -> item.getGradeMember() > memberUse.getGrade();
            List<Member> memberUsess = new ArrayList<>( MEMBER_REPO.findAll().stream()
                    .filter(streamsPredicate)
                    .toList());
            Comparator<Member> resComparator = new Comparator<Member>() {
                @Override
                public int compare(Member o1, Member o2) {
                    return Double.compare(o1.getGradeMember(), o2.getGradeMember());
                }
            };
            memberUsess.sort(resComparator);

            if(!memberUsess.isEmpty()) {
                memberUse.setGradeMemberUp(memberUsess.get(0).getGradeMember());
                memberUse.setPriceMemberUp((int) memberUsess.get(0).getPriceAchieveMember());
            }
            memberUses.add(memberUse);
        });
        sort(memberUses);
        return memberUses;
    }

    @Override
    public void delete(List<Long> list) {
        list.forEach(MEMBER_REPO::deleteById);
    }

    @Override
    public MemberUse getById(Long id) {
        return convertToOtherData.getMemberUse(MEMBER_REPO.findById(id).orElseThrow());
    }

    @Override
    public Member getByIdF(Long id) {
        return MEMBER_REPO.findById(id).orElseThrow();
    }

    @Override
    public Member getMemberInit() {
        return MEMBER_REPO.findByPriceAchieveMember((double) 0).orElseThrow();
    }

    @Override
    public void save(MemberUse memberUse) {
        Member member = new Member();
        member.setGradeMember((int) memberUse.getGrade());
        member.setNameMember(memberUse.getName());
        member.setPriceAchieveMember(memberUse.getPrice());
        member.setPercentDiscountMember(memberUse.getPercent());
        MEMBER_REPO.save(member);
    }

    @Override
    public void update(MemberUse memberUse) {
        Member member = MEMBER_REPO.findById(memberUse.getId()).orElseThrow();
        member.setGradeMember((int) memberUse.getGrade());
        member.setNameMember(memberUse.getName());
        member.setPriceAchieveMember(memberUse.getPrice());
        member.setPercentDiscountMember(memberUse.getPercent());
        MEMBER_REPO.save(member);
    }

    @Override
    public boolean checkPrice(Double price) {
        return MEMBER_REPO.findByPriceAchieveMember(price).isPresent();
    }
}
