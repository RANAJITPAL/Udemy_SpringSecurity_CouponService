package com.ranajit.couponservice.controller;


import com.ranajit.couponservice.entity.Coupon;
import com.ranajit.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couponapi")
@CrossOrigin
public class CouponRestController {

    @Autowired
    CouponRepository couponRepository;

    @PostMapping("/coupons")
    public Coupon createCoupon(@RequestBody Coupon coupon){
        return couponRepository.save(coupon);
    }

    @GetMapping("/coupons/{code}")
    public Coupon getCoupon(@PathVariable("code") String code){
        return couponRepository.findByCode(code);
    }

}
