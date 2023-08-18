package com.example.mammabackend.global.common;

import org.springframework.validation.BindingResult;

public interface AddValid {

    BindingResult verifyAdditional(BindingResult bindingResult);

}
