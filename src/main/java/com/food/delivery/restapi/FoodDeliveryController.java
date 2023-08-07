package com.food.delivery.restapi;


import com.food.delivery.domain.FoodDelivery;
import com.food.delivery.model.FoodDeliveryModel;
import com.food.delivery.service.FoodDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foodDelivery")
public class FoodDeliveryController
{
    private final FoodDeliveryService foodDeliveryService;

    @GetMapping
    public ResponseEntity<Page<FoodDeliveryModel>> getFoodDeliveries(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String deliveryId,
                                                             @RequestParam(required = false) String customer,
                                                             @RequestParam(required = false) FoodDelivery.DeliveryStatus deliveryStatus,
                                                             @RequestParam(required = false) Integer priority,
                                                             Pageable pageable)
    {
        return ResponseEntity.ok(foodDeliveryService.fetchAll(search, deliveryId, customer, deliveryStatus, priority,pageable));
    }

    @GetMapping(value = "/priority")
    public ResponseEntity<List<FoodDeliveryModel>> getFoodDeliveries()
    {
        return ResponseEntity.ok(foodDeliveryService.fetchPriorityTicket());
    }

    @GetMapping("/{key}")
    public ResponseEntity<FoodDeliveryModel> getFoodDelivery(@PathVariable String key)
    {
        return ResponseEntity.ok(foodDeliveryService.fetch(key));
    }

    @PostMapping
    public ResponseEntity<FoodDeliveryModel> create(@RequestBody FoodDeliveryModel foodDeliveryModel)
    {
        return ResponseEntity.ok(foodDeliveryService.create(foodDeliveryModel));
    }

}
