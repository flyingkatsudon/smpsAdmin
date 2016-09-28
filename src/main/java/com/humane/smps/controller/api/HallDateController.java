package com.humane.smps.controller.api;

import com.humane.smps.model.ExamHallDate;
import com.humane.smps.repository.HallDateRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/hallDate", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HallDateController {
    private final HallDateRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExamHallDate> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

}