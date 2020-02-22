package com.hiscat.codec.springrest.assembler;

import com.hiscat.codec.springrest.controller.EmployeeController;
import com.hiscat.codec.springrest.entity.Employee;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Administrator
 */
@Component
public class EmployeeRepresentationModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee entity) {
        return new EntityModel<>(entity)
                .add(linkTo(methodOn(EmployeeController.class).all()).withRel("employees"))
                .add(linkTo(methodOn(EmployeeController.class).one(entity.getId())).withSelfRel());
    }

    @Override

    public CollectionModel<EntityModel<Employee>> toCollectionModel(Iterable<? extends Employee> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
