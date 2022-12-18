package com.santos.cliente.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.santos.cliente.dto.ClienteDTO;
import com.santos.cliente.entities.Cliente;
import com.santos.cliente.repositories.ClienteRepository;
import com.santos.cliente.services.exception.DataBaseException;
import com.santos.cliente.services.exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

	
	@Autowired
	private ClienteRepository repository;
	
	@Transactional(readOnly = true)
	public List<ClienteDTO> findAll() {
		List<Cliente> list= repository.findAll();
		return list.stream().map(x -> new ClienteDTO(x)).collect(Collectors.toList());
	}

	//Busca Paginada
		@Transactional(readOnly = true)
		public Page<ClienteDTO> findAllPaged(PageRequest pageResquest) {
			Page<Cliente> list = repository.findAll(pageResquest);

			// Convertendo lista de Cliente, para lista de categoriaDTO
			return list.map(x -> new ClienteDTO(x));
		}
		
		
		
		
		//Recebe um cliente  DTO e copia para o cliente
		private void copyDtoToEntity(ClienteDTO dto, Cliente entity) {
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setIncome(dto.getIncome());
			entity.setBirthDate(dto.getBirthDate());
			entity.setChildren(dto.getChildren());
		}

	
		@Transactional(readOnly = true)
		public ClienteDTO findById(Long id) {
			Optional<Cliente> obj = repository.findById(id);
			Cliente entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada!"));
			return new ClienteDTO(entity);
		
		}

		
		@Transactional
		public ClienteDTO insert(ClienteDTO dto) {
			Cliente entity = new Cliente();
			copyDtoToEntity(dto, entity);	
			entity = repository.save(entity);
			return new ClienteDTO(entity);
		}

		
		@Transactional
		public ClienteDTO update(Long id, ClienteDTO dto) {
			
			try {
				Cliente entity = repository.getReferenceById(id);
				copyDtoToEntity(dto, entity);
				entity = repository.save(entity);
				return new ClienteDTO(entity);
			}
			
			catch (EntityNotFoundException  e) {
				throw new ResourceNotFoundException("Id não encontrado: " + id);
			}
		}

		
		public void delete(Long id) {

			try {
				repository.deleteById(id);

			} catch (EmptyResultDataAccessException e) {
				throw new ResourceNotFoundException("Id não encontrado: " + id);
			}

			catch (DataIntegrityViolationException e) {
				throw new DataBaseException("Violação de integridade de dados.");
			}

		}

				
}
