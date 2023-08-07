package com.food.delivery.factory;

import com.food.delivery.domain.Food;
import com.food.delivery.domain.OrderHeader;
import com.food.delivery.domain.OrderSegment;
import com.food.delivery.domain.RestaurantFood;

public class SegmentFactory
{

    private static final SegmentFactory FACTORY	= new SegmentFactory();

    /**
     * Returns an instance of the factory
     *
     * @return
     */
    public static SegmentFactory getInstance ()
    {
        return FACTORY;
    }

    private SegmentFactory()
    {
    }



    public OrderSegment newSegment (OrderHeader orderHeader, RestaurantFood restaurantFood)
    {
        return   newSegment(orderHeader, restaurantFood.getFood(), restaurantFood.getDiscountRate(), restaurantFood.getPrice());
    }

    public OrderSegment newSegment (OrderHeader orderHeader, Food food, Double discountRate, Double price)
    {
      return  new SegmentBuilder()
              .withFood(food)
              .withOrderHeader(orderHeader)
              .withStatus(OrderSegment.OrderSegmentStatus.ACTIVE)
              .withDiscount(discountRate)
              .withPrice(price)
              .build();
    }

    private static class SegmentBuilder {
        private OrderHeader orderHeader;
        private Food food;
        private OrderSegment.OrderSegmentStatus status;
        private Double discountRate;
        private Double price;
        private SegmentBuilder() {
        }

        public SegmentBuilder withOrderHeader(OrderHeader orderHeader) {
            this.orderHeader = orderHeader;
            return this;
        }

        public SegmentBuilder withFood(Food food) {
            this.food = food;
            return this;
        }

        public SegmentBuilder withStatus(OrderSegment.OrderSegmentStatus status) {
            this.status = status;
            return this;
        }

        public SegmentBuilder withDiscount(Double discount) {
            this.discountRate = discount;
            return this;
        }

        public SegmentBuilder withPrice(Double price) {
            this.price = price;
            return this;
        }
        private OrderSegment build()
        {
            OrderSegment orderSegment = new OrderSegment();
            orderSegment.setOrderHeader(this.orderHeader);
            orderSegment.setFood(this.food);
            orderSegment.setStatus(this.status);
            orderSegment.setDiscountRate(this.discountRate);
            orderSegment.setPrice(this.price);
            return orderSegment;
        }
    }
}
