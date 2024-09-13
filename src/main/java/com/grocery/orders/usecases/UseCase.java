package com.grocery.orders.usecases;

@FunctionalInterface
public interface UseCase<I, O> {
    O execute(I input);
}
