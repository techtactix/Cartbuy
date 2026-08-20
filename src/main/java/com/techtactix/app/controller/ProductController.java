package com.techtactix.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable("id") int id,@RequestPart Product product, @RequestPart MultipartFile imageFile){
		Product updatedProduct=null;
		try {
			updatedProduct=productService.updateProduct(product, imageFile);
			return new ResponseEntity<String>("Updated",HttpStatus.OK);
			
		}
		catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") int id){
		Product product=productService.getProductById(id);
		if(product!=null) {
			productService.deleteProduct(id);
			return new ResponseEntity<>("Deleted",HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("products/search")
	public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
		List<Product> products=productService.searchProducts(keyword);
		System.out.println("searchin with "+keyword);
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	}
	
	
	
	
	
}