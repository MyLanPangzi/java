package com.hiscat.codec.springrest.controller;

import com.hiscat.codec.springrest.assembler.EmployeeRepresentationModelAssembler;
import com.hiscat.codec.springrest.entity.Employee;
import com.hiscat.codec.springrest.exception.EmployeeNotFoundException;
import com.hiscat.codec.springrest.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Administrator
 */
@RestController
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepresentationModelAssembler employeeRepresentationModelAssembler;

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        return this.employeeRepresentationModelAssembler.toCollectionModel(this.employeeRepository.findAll());
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        Employee save = this.employeeRepository.save(employee);
        EntityModel<Employee> model = this.employeeRepresentationModelAssembler.toModel(save);
        return ResponseEntity
                .created(URI.create(model.getLink(LinkRelation.of("self")).get().getHref()))
                .body(model);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return this.employeeRepresentationModelAssembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EntityModel<Employee>> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {
        Employee employee = this.employeeRepository.findById(id)
                .map(e -> {
                    e.setName(newEmployee.getName());
                    e.setRole(newEmployee.getRole());
                    return this.employeeRepository.save(e);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return this.employeeRepository.save(newEmployee);
                });
        EntityModel<Employee> model = this.employeeRepresentationModelAssembler.toModel(employee);
        return ResponseEntity
                .created(new URI(model.getRequiredLink("self").getHref()))
                .body(model);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        this.employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
