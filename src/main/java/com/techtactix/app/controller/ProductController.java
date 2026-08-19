package com.techtactix.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techtactix.app.model.Product;
import com.techtactix.app.service.ProductService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts() {
		return new ResponseEntity<List<Product>>(productService.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") int id){
		Product p= productService.getProductById(id);
		if(p.getId()>0) {
			return new ResponseEntity<Product>(p, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
		}
	
	}
	
	@GetMapping("product/{productId}/image")
	public ResponseEntity<byte[]> getProductImageById(@PathVariable("productId") int id){
		Product p = productService.getProductById(id);
		if(p.getId()>0) {
			return new ResponseEntity<byte[]>(p.getImgData(), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
		Product savedProduct;
		try {
			savedProduct = productService.addProduct(product, imageFile);
			return new ResponseEntity<>(savedProduct,HttpStatus.CREATED);

		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
}