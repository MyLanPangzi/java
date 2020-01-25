package com.hiscat.springrest.assembler;

import com.hiscat.springrest.controller.OrderController;
import com.hiscat.springrest.entity.Order;
import com.hiscat.springrest.entity.Status;
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
public class OrderRepresentationModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {
    @Override
    public EntityModel<Order> toModel(Order entity) {
        EntityModel<Order> model = new EntityModel<>(entity)
                .add(linkTo(methodOn(OrderController.class).all()).withRel("orders"))
                .add(linkTo(methodOn(OrderController.class).one(entity.getId())).withSelfRel());
        if (Status.IN_PROGRESS.equals(entity.getStatus())) {
            model.add(linkTo(methodOn(OrderController.class).cancel(entity.getId())).withRel("cancel"));
            model.add(linkTo(methodOn(OrderController.class).complete(entity.getId())).withRel("complete"));
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<Order>> toCollectionModel(Iterable<? extends Order> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
