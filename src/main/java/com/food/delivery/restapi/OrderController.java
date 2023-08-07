package com.food.delivery.restapi;

import com.food.delivery.domain.OrderHeader;
import com.food.delivery.model.OrderHeaderModel;
import com.food.delivery.model.OrderModel;
import com.food.delivery.model.OrderTicketModel;
import com.food.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController
{
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<Page<OrderHeaderModel>> getOrders(@RequestParam(required = false) String search,
                                                            @RequestParam(required = false)Integer priority,
                                                            @RequestParam(required = false)OrderHeader.OrderHeaderStatus status,
                                                            @RequestParam(required = false) List<String> customerKeys,
                                                            @RequestParam(required = false) List<String> restaurantKeys,
                                                            Pageable pageable)
    {
        return ResponseEntity.ok(orderService.fetchAll(search, priority, status, customerKeys, restaurantKeys, pageable));
    }

    @GetMapping("/{key}/status")
    public ResponseEntity<OrderHeader.OrderHeaderStatus> getOrderStatus(@PathVariable String key)
    {
        return ResponseEntity.ok(orderService.fetch(key).status());
    }

    @PostMapping
    public ResponseEntity<OrderTicketModel> create(@RequestBody OrderModel orderModel)
    {
        return ResponseEntity.ok(orderService.create(orderModel));
    }

    @GetMapping("/{key}")
    public ResponseEntity<OrderHeaderModel> getOrder(@PathVariable String key)
    {
        return ResponseEntity.ok(orderService.fetch(key));
    }
    @PatchMapping("/{key}/status/{status}")
    public ResponseEntity<OrderHeader.OrderHeaderStatus> patchOrderStatus(@PathVariable String key,
                                                                           @PathVariable OrderHeader.OrderHeaderStatus status)
    {
        orderService.changeStatus(key, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{key}/status/{status}")
    public ResponseEntity<OrderHeader.OrderHeaderStatus> putOrderStatus(@PathVariable String key,
                                                                          @PathVariable OrderHeader.OrderHeaderStatus status)
    {
        orderService.changeStatus(key, status);
        return ResponseEntity.ok().build();
    }
}
