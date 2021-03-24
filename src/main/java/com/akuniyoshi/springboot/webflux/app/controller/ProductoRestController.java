package com.akuniyoshi.springboot.webflux.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akuniyoshi.springboot.webflux.app.models.dao.ProductoDao;
import com.akuniyoshi.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

	@Autowired
	private ProductoDao productoDao;

	private static Logger log = LoggerFactory.getLogger(ProductoRestController.class);

	@GetMapping
	public Flux<Producto> index() {

		Flux<Producto> productos = productoDao.findAll().map(pro -> {
			pro.setNombreProducto(pro.getNombreProducto().toUpperCase());
			return pro;
		}).doOnNext(pro -> log.info(pro.getNombreProducto()));

		return productos;
	}

	@GetMapping("/{id}")
	public Mono<Producto> show(@PathVariable String id) {

		//Simple way to do it
		// Mono <Producto> producto=productoDao.findById(id);

		//Another way, more dificult but here use more things
		Mono<Producto> producto = productoDao.findAll().filter(prod -> prod.getId().equals(id)).next()
				.doOnNext(prod -> log.info(prod.getNombreProducto()));

		return producto;

	}

}
