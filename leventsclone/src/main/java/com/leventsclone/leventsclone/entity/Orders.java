package com.leventsclone.leventsclone.entity;

import com.leventsclone.leventsclone.jpa.Auditables;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Orders extends Auditables<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrder;

    @Column
    private Integer sumOrder;

    @Column
    private Integer discountMember;

    @Column
    private String stateOrder;

    @Column
    private String imgPaymentOrder;
    @Column
    private String keyImagePaymentOrder;

    @Column
    private String imgRefundPaymentOrder;

    @Column
    private String keyImageRefundPaymentOrder;

    @Column(unique = true)
    private String CodeOrder;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private InfoOrder infoOrder;

    @ManyToMany
    @JoinTable(
            name = "order_voucher",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "idOrder"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "idVoucher")
    )
    private List<Voucher> vouchers = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    private Rating rating;

}
