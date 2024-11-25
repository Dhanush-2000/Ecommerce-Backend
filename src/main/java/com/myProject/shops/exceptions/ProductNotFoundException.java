package com.myProject.shops.exceptions;

public class ProductNotFoundException extends RuntimeException{
     public ProductNotFoundException(String message){
         super(message);
     }
}
