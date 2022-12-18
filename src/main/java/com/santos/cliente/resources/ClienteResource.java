package com.santos.cliente.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.santos.cliente.dto.ClienteDTO;
import com.santos.cliente.services.ClienteService;




@RestController
@RequestMapping(value = "/clients")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;

	// Métodos get
		// Método que lista tudo sem paginação
	
	/*
		@GetMapping(value = "all")
		public ResponseEntity<List<ClienteDTO>> findAll() {
			List<ClienteDTO> list = service.findAll();
			return ResponseEntity.ok().body(list);
		}
		*/
		
		@GetMapping()
		public ResponseEntity<Page<ClienteDTO>>findAllWhitPage(
		
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy)
		{
			PageRequest pageResquest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
			Page<ClienteDTO> list = service.findAllPaged(pageResquest);
			return ResponseEntity.ok().body(list);

		}
		
		@GetMapping(value = "{id}")
		public ResponseEntity<ClienteDTO>findById(@PathVariable Long id ){
			ClienteDTO dto =  service.findById(id);
			return ResponseEntity.ok().body(dto);
		}
		
		
		@PostMapping
		public ResponseEntity<ClienteDTO> inserirCliente(@RequestBody ClienteDTO dto) {
			dto = service.insert(dto);
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
			return ResponseEntity.created(uri).body(dto);
		
		}
		
		@PutMapping(value = "{id}")
		public ResponseEntity<ClienteDTO> uptadeCliente(@PathVariable Long id, @RequestBody ClienteDTO dto ){
			dto = service.update(id, dto);
			return ResponseEntity.ok().body(dto);
		}
		
		@DeleteMapping(value = "{id}")
		public ResponseEntity<ClienteDTO> deleteCliente(@PathVariable Long id){
			
			service.delete(id);
			return ResponseEntity.noContent().build();
			
		}
		
		
		
}
