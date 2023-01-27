package br.com.bugbuilder.chamado.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.models.RoleModel;
import br.com.bugbuilder.chamado.models.RoleName;
import br.com.bugbuilder.chamado.requests.ChamadoListGetRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoClassificationPutRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoPostRequestBody;
import br.com.bugbuilder.chamado.service.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/chamados")
public class ChamadoController {
	
	@Autowired
	private ChamadoService chamadoService;
	
	private RoleModel roleSupporter = new RoleModel();
	private RoleModel roleAdmin = new RoleModel();
	
	{
		this.roleSupporter.setRoleId(UUID.fromString("4093b733-2498-44b7-8ce9-e5a24685fc8d"));
		this.roleSupporter.setRoleName(RoleName.ROLE_SUPPORTER);
		this.roleAdmin.setRoleId(UUID.fromString("c0b5c616-6f47-46b6-bd20-162e25b509e4"));
		this.roleAdmin.setRoleName(RoleName.ROLE_ADMIN);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path = "/all")
	@Operation(summary = "List all calls registered for user with role ADMIN",
			description = "Return all calls as a PageObject", tags = {"chamado"})
	public ResponseEntity<Page<ChamadoModel>> listAll(Pageable pageable){
		Page<ChamadoModel> chamadosPage = chamadoService.listAll(pageable);
		chamadosPage.forEach(item -> {
			item.add(
					WebMvcLinkBuilder.linkTo(ChamadoController.class)
							.slash(item.getId()).withRel("chamado"));
		});
		return ResponseEntity.ok(chamadosPage);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMMUN_USER', 'ROLE_SUPPORTER')")
	@GetMapping(path = "/{uuid}")
	public ResponseEntity<ChamadoModel> findById(@PathVariable UUID uuid, 
			@AuthenticationPrincipal UserDetails user){
		ChamadoModel chamado = chamadoService.findByIdOrElseThrowBadRequest(uuid);
		
		chamado.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
						.findById(uuid, user)).withSelfRel());
		
		chamado.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
						.listAllByUser(user)).withRel("chamados"));
		
		if (chamado.isOpen() && (user.getAuthorities().contains(this.roleAdmin) || user.getAuthorities().contains(this.roleSupporter))) {
			chamado.add(
					WebMvcLinkBuilder.linkTo(ChamadoController.class)
							.slash("close").slash(chamado.getId()).withRel("close"));
			chamado.add(
					WebMvcLinkBuilder.linkTo(ChamadoController.class)
							.slash("classificate").slash(chamado.getId()).withRel("classificate"));
		}
		
		if (user.getAuthorities().contains(this.roleAdmin))
			chamado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
					.delete(chamado.getId())).withRel("delete"));
		
		if (chamado.getAttendantUser() == null) {
			if ((chamado.isOpen() && (user.getAuthorities().contains(this.roleAdmin) || user.getAuthorities().contains(this.roleSupporter)))
					|| chamado.getRequesterUser().getUsername().equals(user.getUsername()) || 
					user.getAuthorities().contains(this.roleAdmin))
				return ResponseEntity.ok(chamado);
			else
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		else {
			if ((chamado.isOpen() && (user.getAuthorities().contains(this.roleAdmin) || user.getAuthorities().contains(this.roleSupporter)))
					|| chamado.getRequesterUser().getUsername().equals(user.getUsername()) || 
					chamado.getAttendantUser().getUsername().equals(user.getUsername()) || user.getAuthorities().contains(this.roleAdmin))
				return ResponseEntity.ok(chamado);
			else
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); 
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORTER','ROLE_COMMUN_USER')")
	@GetMapping
	public ResponseEntity<CollectionModel<ChamadoListGetRequestBody>> listAllByUser(@AuthenticationPrincipal UserDetails user) {
		if(user.getAuthorities().contains(this.roleSupporter)) {
			List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByAttendantOrOpen(user);
			chamadoList.forEach(item -> {
				item.add(WebMvcLinkBuilder.linkTo(ChamadoController.class)
						.slash(item.getId()).withRel("chamado"));
			});
			CollectionModel<ChamadoListGetRequestBody> chamadoCollection = CollectionModel.of(chamadoList);
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).withSelfRel());
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("open").withRel("open-calls"));
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("supporter").withRel("supporter-calls"));
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(RootEntryPointController.class).withRel("root-entry-point"));
			return ResponseEntity.ok(chamadoCollection);
		}
		else {
			List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByRequestUser(user);
			chamadoList.forEach(item -> {
				item.add(WebMvcLinkBuilder.linkTo(ChamadoController.class)
						.slash(item.getId()).withRel("chamado"));
			});
			CollectionModel<ChamadoListGetRequestBody> chamadoCollection = CollectionModel.of(chamadoList);
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).withSelfRel());
			if(user.getAuthorities().contains(this.roleAdmin)) {
				chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("open").withRel("open-calls"));
				chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("supporter").withRel("supporter-calls"));
				chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("all").withRel("all-calls"));
			}
			chamadoCollection.add(WebMvcLinkBuilder.linkTo(RootEntryPointController.class).withRel("root-entry-point"));
			return ResponseEntity.ok(chamadoCollection);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORTER')")
	@GetMapping(path = "/supporter")
	public ResponseEntity<CollectionModel<ChamadoListGetRequestBody>> listAllByAttendantUser(@AuthenticationPrincipal UserDetails user) {
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByAttendantUser(user);
		chamadoList.forEach(item -> {
			item.add(WebMvcLinkBuilder.linkTo(ChamadoController.class)
					.slash(item.getId()).withRel("chamdo"));
		});
		CollectionModel<ChamadoListGetRequestBody> chamadoCollection = CollectionModel.of(chamadoList);
		chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("supporter").withSelfRel());
		chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).withRel("Chamados"));
		return ResponseEntity.ok(chamadoCollection);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORTER')")
	@GetMapping(path = "/open")
	public ResponseEntity<CollectionModel<ChamadoListGetRequestBody>> listAllByOpen() {
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByOpen();
		chamadoList.forEach(item -> {
			item.add(WebMvcLinkBuilder.linkTo(ChamadoController.class)
					.slash(item.getId()).withRel("chamado"));
		});
		CollectionModel<ChamadoListGetRequestBody> chamadoCollection = CollectionModel.of(chamadoList);
		chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("open").withSelfRel());
		chamadoCollection.add(WebMvcLinkBuilder.linkTo(ChamadoController.class).withRel("Chamados"));
		return ResponseEntity.ok(chamadoCollection);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COMMUN_USER')")
	@PostMapping
	public ResponseEntity<ChamadoModel> openCall(@RequestBody @Valid ChamadoPostRequestBody chamadoPost, 
			@AuthenticationPrincipal UserDetails user) {
		ChamadoModel chamado = chamadoService.save(chamadoPost, user);
		chamado.add(WebMvcLinkBuilder.linkTo(ChamadoController.class)
				.slash(chamado.getId()).withSelfRel());
		chamado.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
				.listAllByUser(user)).withRel("chamados"));
		return new ResponseEntity<>(chamado, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(path = "/{id}")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfull."),
            @ApiResponse(responseCode = "400", description = "Id does not macth with none saved id")
    })
	public ResponseEntity<Void> delete(@PathVariable UUID id){
		chamadoService.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORTER')")
	@PatchMapping(path = "/classificate/{id}")
	public ResponseEntity<Void> classifcateCall(@RequestBody ChamadoClassificationPutRequestBody chamadoClassification,
			@PathVariable UUID id){
		chamadoService.replaceClassificationCall(chamadoClassification, id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPPORTER')")
	@PatchMapping(path = "/close/{id}")
	@ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfull."),
            @ApiResponse(responseCode = "400", description = "Id does not macth with none saved id")
    })
	public ResponseEntity<Void> closingCall(@PathVariable UUID id, 
			@AuthenticationPrincipal UserDetails user) {
		chamadoService.replaceClosingCall(id, user);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
