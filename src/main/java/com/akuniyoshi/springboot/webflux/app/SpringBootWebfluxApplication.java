package com.akuniyoshi.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.akuniyoshi.springboot.webflux.app.models.dao.ProductoDao;
import com.akuniyoshi.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Drop the table on mongoDB the colecction "producto"
		reactiveMongoTemplate.dropCollection("producto").subscribe();

		Flux.just(new Producto("TV Panasonic 43'", 425.2), new Producto("Camara Sony HD", 500.8),
				new Producto("Lenovo Idea Pad 300", 425.2)).flatMap(producto -> {
					producto.setCreateAt(new Date());
					return productoDao.save(producto);
				}).subscribe(producto -> log.info("Insert :" + producto));
	}

}
