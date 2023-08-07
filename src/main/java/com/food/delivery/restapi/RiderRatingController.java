package com.food.delivery.restapi;

import com.food.delivery.model.RiderRatingListModel;
import com.food.delivery.model.RiderRatingModel;
import com.food.delivery.service.RiderRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/riderRating")
public class RiderRatingController
{
    private final RiderRatingService riderRatingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<RiderRatingListModel>> getRiderRating(@RequestParam(required = false) String search,
                                                                     @RequestParam(required = false) String riderKey,
                                                                     @RequestParam(required = false) String orderKey,
                                                                     @RequestParam(required = false) String customerKey,
                                                                     @RequestParam(required = false) String restaurantKey,
                                                                     Pageable pageable)
    {
        return ResponseEntity.ok(riderRatingService.fetchAll(search, riderKey, orderKey, customerKey, restaurantKey,pageable));
    }

    @GetMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderRatingListModel> getRiderRating(@PathVariable String key)
    {
        return ResponseEntity.ok(riderRatingService.fetch(key));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderRatingModel> create(@RequestBody RiderRatingModel riderRatingModel)
    {
        return ResponseEntity.ok(riderRatingService.create(riderRatingModel));
    }

    @PutMapping(value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiderRatingModel> update(@PathVariable String key, @RequestBody RiderRatingModel riderRatingModel)
    {
        return ResponseEntity.ok(riderRatingService.update(key,riderRatingModel));
    }
}
