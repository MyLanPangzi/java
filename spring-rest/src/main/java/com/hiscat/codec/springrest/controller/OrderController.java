package com.hiscat.codec.springrest.controller;

import com.hiscat.codec.springrest.assembler.OrderRepresentationModelAssembler;
import com.hiscat.codec.springrest.entity.Order;
import com.hiscat.codec.springrest.entity.Status;
import com.hiscat.codec.springrest.exception.OrderNotFoundException;
import com.hiscat.codec.springrest.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * @author Administrator
 */
@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderRepresentationModelAssembler orderRepresentationModelAssembler;

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> all() {
        return this.orderRepresentationModelAssembler.toCollectionModel(this.orderRepository.findAll());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> one(@PathVariable Long id) {
        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return this.orderRepresentationModelAssembler.toModel(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<?> newOrder(@RequestBody Order order) {
        order.setStatus(Status.IN_PROGRESS);
        Order save = this.orderRepository.save(order);
        EntityModel<Order> model = this.orderRepresentationModelAssembler.toModel(save);
        return ResponseEntity
                .created(URI.create(model.getRequiredLink(LinkRelation.of("self")).getHref()))
                .body(model);
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (Status.IN_PROGRESS.equals(order.getStatus())) {
            order.setStatus(Status.CANCELED);
            return ResponseEntity.ok(this.orderRepresentationModelAssembler.toModel(this.orderRepository.save(order)));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is " +
                        "in the " + order.getStatus() + " status"));

    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = this.orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (Status.IN_PROGRESS.equals(order.getStatus())) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(this.orderRepresentationModelAssembler.toModel(this.orderRepository.save(order)));
        }
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is " +
                        "in the " + order.getStatus() + " status"));

    }

}
