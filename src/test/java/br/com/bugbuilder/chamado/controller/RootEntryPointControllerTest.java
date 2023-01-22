package br.com.bugbuilder.chamado.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.bugbuilder.chamado.models.RootEntryPointModel;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for the controller RootEntryPoint")
public class RootEntryPointControllerTest {
	
	@InjectMocks
	private RootEntryPointController rootEntryPointController;
	
	@Test
	@DisplayName("Return a rootEntry point for the application")
	void getRoot_ReeturnRoot_WhenSuccessfull() {
		Link selfRel = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(RootEntryPointController.class).getRoot()).withSelfRel();
		Link nextRel = WebMvcLinkBuilder.linkTo(ChamadoController.class).withRel("chamados");
		
		RootEntryPointModel rootEntryPoint = rootEntryPointController.getRoot().getBody();
		
		Assertions.assertThat(rootEntryPoint.getLinks().contains(selfRel)).isTrue();
		Assertions.assertThat(rootEntryPoint.getLinks().contains(nextRel)).isTrue();
	}
}
