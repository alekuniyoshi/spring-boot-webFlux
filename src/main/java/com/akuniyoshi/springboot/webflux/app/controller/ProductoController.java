package com.akuniyoshi.springboot.webflux.app.controller;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.akuniyoshi.springboot.webflux.app.models.dao.ProductoDao;
import com.akuniyoshi.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@Controller
public class ProductoController {

	@Autowired
	private ProductoDao productoDao;

	private static Logger log = LoggerFactory.getLogger(ProductoController.class);

	@GetMapping({ "/listar", "/" })
	public String listar(Model model) {

		Flux<Producto> listProducto = productoDao.findAll().map(producto -> {
			producto.setNombreProducto(producto.getNombreProducto().toUpperCase());
			return producto;
		});

		listProducto.subscribe(prod -> log.info(prod.getNombreProducto()));

		model.addAttribute("listProducto", listProducto);
		model.addAttribute("titulo", "Lista de productos con webFlux");

		return "listar";
	}

	@GetMapping("/listar-datadriver")
	public String listarDataDriver(Model model) {

		Flux<Producto> listProducto = productoDao.findAll().map(producto -> {
			producto.setNombreProducto(producto.getNombreProducto().toUpperCase());
			return producto;
		}).delayElements(Duration.ofSeconds(1));

		listProducto.subscribe(prod -> log.info(prod.getNombreProducto()));

		model.addAttribute("listProducto", new ReactiveDataDriverContextVariable(listProducto, 1));
		model.addAttribute("titulo", "Lista de productos con webFlux");

		return "listar";
	}

	@GetMapping("/listar-full")
	public String listarFull(Model model) {

		Flux<Producto> listProducto = productoDao.findAll().map(producto -> {
			producto.setNombreProducto(producto.getNombreProducto().toUpperCase());
			return producto;
		}).repeat(5000);

		model.addAttribute("listProducto", listProducto);
		model.addAttribute("titulo", "Lista de productos con webFlux");

		return "listar";
	}
	
	
	@GetMapping("/listar-chunked")
	public String listarChunked(Model model) {

		Flux<Producto> listProducto = productoDao.findAll().map(producto -> {
			producto.setNombreProducto(producto.getNombreProducto().toUpperCase());
			return producto;
		}).repeat(5000);

		model.addAttribute("listProducto", listProducto);
		model.addAttribute("titulo", "Lista de productos con webFlux");

		return "listar-chunked";
	}
}
