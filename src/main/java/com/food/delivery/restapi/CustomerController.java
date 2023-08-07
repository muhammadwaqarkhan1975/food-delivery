package com.food.delivery.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.food.delivery.domain.Customer;
import com.food.delivery.model.CustomerModel;
import com.food.delivery.service.CustomerService;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController
{
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerModel>> getCustomers(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) Customer.CustomerStatus customerStatus,
                                                             @RequestParam(required = false) Customer.CustomerType type,
                                                             Pageable pageable)
    {
        return ResponseEntity.ok(customerService.fetchAll(search,customerStatus, type, pageable));
    }

    @GetMapping("/{key}")
    public ResponseEntity<CustomerModel> getCustomer(@PathVariable String key)
    {
        return ResponseEntity.ok(customerService.fetch(key));
    }

    @PostMapping
    public ResponseEntity<CustomerModel> create(@RequestBody CustomerModel customerModel)
    {
        return ResponseEntity.ok(customerService.create(customerModel));
    }

    @PutMapping("/{key}")
    public ResponseEntity<CustomerModel> update(@PathVariable String key, @RequestBody CustomerModel customerModel)
    {
        return ResponseEntity.ok(customerService.update(key,customerModel));
    }

    @PatchMapping("/{key}")
    public ResponseEntity<CustomerModel> patch(@PathVariable String key, @RequestBody JsonPatch customerModel) throws JsonPatchException, JsonProcessingException {
        return ResponseEntity.ok(customerService.patch(key, customerModel));
    }
}
