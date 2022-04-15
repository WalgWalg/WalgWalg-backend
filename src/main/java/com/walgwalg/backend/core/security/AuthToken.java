package com.walgwalg.backend.core.security;

public interface AuthToken<T> {
    boolean validate();
    T getClaims();
}
