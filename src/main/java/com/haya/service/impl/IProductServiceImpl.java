package com.haya.service.impl;

import com.haya.entity.Product;
import com.haya.service.IProductService;

/**
 * @author haya
 */
public class IProductServiceImpl implements IProductService {
    @Override
    public Product findProductByName(String name) {
        return new Product( 1, name, 1 );
    }
}
