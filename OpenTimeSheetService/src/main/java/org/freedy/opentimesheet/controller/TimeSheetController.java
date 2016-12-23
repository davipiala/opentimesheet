package org.freedy.opentimesheet.controller;

import java.util.List;

import javax.validation.Valid;

import org.freedy.opentimesheet.model.Colaborador;
import org.freeedy.opentimesheet.persistence.TimeSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/freedy/timesheets")
public class TimeSheetController {

	private TimeSheetRepository repository;
	private MongoOperations mongoOperations;

	@Autowired
	public TimeSheetController(TimeSheetRepository repository, MongoOperations mongoOperations) {
		this.repository = repository;
		this.mongoOperations = mongoOperations;
	}

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public List<Colaborador> list() {
		return repository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public Colaborador findById(@PathVariable String id) {
		return repository.findOne(id);
	}

	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.CREATED)
	public Colaborador create(@Valid @RequestBody Colaborador Colaborador) {
		return repository.save(Colaborador);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public Colaborador update(@Valid @PathVariable String id, @RequestBody Colaborador Colaborador) {
		return repository.save(Colaborador);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable String id) {
		repository.delete(id);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(value = HttpStatus.OK)
	public List<Colaborador> search(String nome, String descricao, String categoria, Double precoDe, Double precoAte,
			String ordemPor) {
		return repository.findByCriteria(nome, descricao, categoria, precoDe, precoAte, ordemPor, mongoOperations);
	}

}
