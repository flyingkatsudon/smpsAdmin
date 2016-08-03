package com.humane.smps.controller.api;

import com.humane.smps.model.ExamHall;
import com.humane.smps.repository.ExamHallRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/examHall", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamHallController {
    private final ExamHallRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ExamHall> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ExamHall> merge(@RequestBody ExamHall examHall) {
        ExamHall rtn = repository.save(examHall);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<ExamHall>> merge(@RequestBody Iterable<ExamHall> examHalls) {
        Iterable<ExamHall> rtn = repository.save(examHalls);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}