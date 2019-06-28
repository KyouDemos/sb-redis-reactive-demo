package cn.ok.sbredisreactive.controller;

import cn.ok.sbredisreactive.entities.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author kyou on 2019-06-27 19:10
 */
@Slf4j
@RestController
public class CoffeeController {
    private final ReactiveRedisOperations<String, Coffee> coffeeOps;

    CoffeeController(ReactiveRedisOperations<String, Coffee> coffeeOps) {
        this.coffeeOps = coffeeOps;
    }

    @GetMapping("/coffees/{key}")
    public Flux<Coffee> getAll(@PathVariable("key") String key) {
        return coffeeOps.keys(key + "*").flatMap(coffeeOps.opsForValue()::get);
    }

    @GetMapping("/addCoffee/{key}/{value}")
    public Flux<Coffee> addCoffee(@PathVariable("key") String key, @PathVariable("value") String value) {

        for (int i = 1; i <= 10; i++) {
            String key_i = key + i;
            String value_i = value + i;

            coffeeOps.opsForValue().set(key_i, new Coffee(key_i, value_i)).subscribe();
        }

        return getAll(key);
    }
}
