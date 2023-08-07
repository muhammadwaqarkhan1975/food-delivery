package com.food.delivery.restapi;

import com.food.delivery.model.RiderLocationModel;
import com.food.delivery.service.RiderLocationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/riderLocation")
public class RiderLocationController
{
    private final RiderLocationService riderLocationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RiderLocationModel>> getRiderRating(@RequestParam(required = false) String search,
                                                                   @RequestParam(required = false) String riderKey,
                                                                   Pageable pageable)
    {
        return ResponseEntity.ok(riderLocationService.fetchAll(search, riderKey,pageable));
    }

    @GetMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderLocationModel> getRiderRating(@PathVariable String key)
    {
        return ResponseEntity.ok(riderLocationService.fetch(key));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderLocationModel> create(@RequestBody RiderLocationModel riderLocationModel)
    {
        return ResponseEntity.ok(riderLocationService.create(riderLocationModel));
    }

    @PutMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderLocationModel> update(@PathVariable String key, @RequestBody RiderLocationModel riderLocationModel)
    {
        return ResponseEntity.ok(riderLocationService.update(key,riderLocationModel));
    }
}
