package com.ranajit.couponservice.controller;

import com.ranajit.couponservice.entity.Coupon;
import com.ranajit.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @GetMapping("/showCreateCoupon")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateCoupon(){
        return "createCoupon";
    }

    @PostMapping("/saveCoupon")
    public String save(Coupon coupon){
        couponRepository.save(coupon);
        return "createResponse";
    }

    @GetMapping("/showGetCoupon")
    public String showGetCoupon(){
        return "getCoupon";
    }


    @PostMapping("/getCoupon")
    public ModelAndView getCoupon(String code){
        ModelAndView mav = new ModelAndView("couponDetails");
        mav.addObject(couponRepository.findByCode(code));
        return mav;
    }
}
