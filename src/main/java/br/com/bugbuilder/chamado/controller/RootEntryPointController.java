package br.com.bugbuilder.chamado.controller;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bugbuilder.chamado.models.RootEntryPointModel;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RootEntryPointController {
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMMUN_USER', 'ROLE_SUPPORTER')")
	@GetMapping
	public ResponseEntity<RootEntryPointModel> getRoot() {
		RootEntryPointModel rootEntryPoint = new RootEntryPointModel();
		rootEntryPoint.add(WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RootEntryPointController.class).getRoot()).withSelfRel());
		rootEntryPoint.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).withRel("chamados"));
		return ResponseEntity.ok(rootEntryPoint);
	}
}
