package com.humane.smps.controller.api;

import com.humane.smps.model.Admission;
import com.humane.smps.repository.AdmissionRepository;
import com.mysema.query.types.Predicate;
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
@RequestMapping(value = "api/admission", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmissionController {
    private final AdmissionRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Admission> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Admission> merge(@RequestBody Admission admission) {
        Admission rtn = repository.save(admission);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<Admission>> merge(@RequestBody Iterable<Admission> admissions) {
        Iterable<Admission> rtn = repository.save(admissions);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}