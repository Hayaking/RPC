package com.haya.service;

import com.haya.entity.Product;

/**
 * @author haya
 */
public interface IProductService {
    Product findProductByName(String name);
}
