package br.com.bugbuilder.chamado.controller;

import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.requests.ChamadoListGetRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoPostRequestBody;
import br.com.bugbuilder.chamado.service.ChamadoService;
import br.com.bugbuilder.chamado.util.GeneralCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for the controller chamados")
public class ChamdoControllerTest {
	
	@InjectMocks
	private ChamadoController chamadoController;
	
	@Mock
	private ChamadoService chamadoServiceMock;
	
	@BeforeEach
	void setUP() {
		PageImpl<ChamadoModel> chamadoPage = new PageImpl<>(List.of(GeneralCreator.chamadoJustCreatedValid()));
		BDDMockito.when(chamadoServiceMock.listAll(ArgumentMatchers.any(PageRequest.class)))
			.thenReturn(chamadoPage);
		
		BDDMockito.when(chamadoServiceMock.findByIdOrElseThrowBadRequest(ArgumentMatchers.any(UUID.class)))
			.thenReturn(GeneralCreator.chamadoJustCreatedValid());
		
		BDDMockito.when(chamadoServiceMock.listAllByRequestUser(ArgumentMatchers.any(UserDetails.class)))
			.thenReturn(List.of(GeneralCreator.chamadoDTOJustCreatedValid()));
		
		BDDMockito.when(chamadoServiceMock.listAllByAttendantUser(ArgumentMatchers.any(UserDetails.class)))
			.thenReturn(List.of(GeneralCreator.chamadoDTOClosedValid()));
		
		BDDMockito.when(chamadoServiceMock.listAllByAttendantOrOpen(ArgumentMatchers.any(UserDetails.class)))
			.thenReturn(List.of(GeneralCreator.chamadoDTOClosedValid(), GeneralCreator.chamadoDTOJustCreatedValid()));
		
		BDDMockito.when(chamadoServiceMock.listAllByOpen())
			.thenReturn(List.of(GeneralCreator.chamadoDTOClassificatedValid()));
		
		BDDMockito.when(chamadoServiceMock.save(ArgumentMatchers.any(ChamadoPostRequestBody.class), ArgumentMatchers.any(UserDetails.class)))
			 .thenReturn(GeneralCreator.chamadoJustCreatedValid());
		
		BDDMockito.doNothing().when(chamadoServiceMock).delete(ArgumentMatchers.any(UUID.class));
	}
	
	@Test
	@DisplayName("Return a Page as a body of request with a list of all of the calls")
	void list_ReturnReturnListOfChamadosInsidePageObject_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		Link link = (WebMvcLinkBuilder.linkTo(ChamadoController.class).slash(expectedChamado.getId()).withRel("chamado"));
		
		Page<ChamadoModel> chamadoPage = chamadoController.listAll(PageRequest.of(1, 1)).getBody();
		
		Assertions.assertThat(chamadoPage).isNotEmpty();
		
		Assertions.assertThat(chamadoPage.toList()).isNotEmpty();
		Assertions.assertThat(chamadoPage.toList()).hasSize(1);
		
		Assertions.assertThat(chamadoPage.toList().get(0).getTitle()).isEqualTo(expectedChamado.getTitle());
		Assertions.assertThat(chamadoPage.toList().get(0).isOpen()).isTrue();
		Assertions.assertThat(chamadoPage.toList().get(0).getLinks()).isNotEmpty();
		Assertions.assertThat(chamadoPage.toList().get(0).getLinks().contains(link)).isTrue();
	}
	
	@Test
	@DisplayName("Return a call as a body of request")
	void findById_ReturnChamado_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
				.findById(expectedChamado.getId(), GeneralCreator.userDetailCommunValid())).withSelfRel();
		Link rellLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ChamadoController.class)
				.listAllByUser(GeneralCreator.userDetailCommunValid())).withRel("chamados");
		
		ChamadoModel chamado = chamadoController.findById(expectedChamado.getId(), GeneralCreator.userDetailCommunValid()).getBody();
		
		Assertions.assertThat(chamado).isNotNull();
		Assertions.assertThat(chamado.getId()).isNotNull().isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamado.getLinks()).isNotEmpty();
		Assertions.assertThat(chamado.getLinks().contains(selfLink)).isTrue();
		Assertions.assertThat(chamado.getLinks().contains(rellLink)).isTrue();
	}
	
	@Test
	@DisplayName("Return a CollectionModel of calls from a CommunUser as a body of request")
	void listAllByCommunUser_ReturnLisOfCalls_WhenSuccessfull() {
		ChamadoListGetRequestBody expectedChamado = GeneralCreator.chamadoDTOJustCreatedValid();
		Link selfLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).withSelfRel();
		Link suppoLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("supporter").withRel("supporter-calls");
		Link previusLink = WebMvcLinkBuilder.linkTo(RootEntryPointController.class).withRel("root-entry-point");
		
		CollectionModel<ChamadoListGetRequestBody> chamadoList = chamadoController.listAllByUser(GeneralCreator.userDetailCommunValid()).getBody();
		
		Assertions.assertThat(chamadoList).isNotEmpty().isNotNull();
		Assertions.assertThat(chamadoList.getContent().contains(expectedChamado));
		Assertions.assertThat(chamadoList.getLinks().contains(selfLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(previusLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(suppoLink)).isFalse();
	}
	
	@Test
	@DisplayName("Return a CollectionModel of calls from a AttendantUser as a body of request")
	void listAllByAttendantUserAndOpen_ReturnListOfCalls_WhenSuccessfull() {
		var expectedChamado = List.of(GeneralCreator.chamadoDTOClosedValid(), GeneralCreator.chamadoDTOJustCreatedValid());
		Link selfLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).withSelfRel();
		Link supportLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("supporter").withRel("supporter-calls");
		Link adminLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("all").withRel("all-calls");
		Link previusLink = WebMvcLinkBuilder.linkTo(RootEntryPointController.class).withRel("root-entry-point");
		
		CollectionModel<ChamadoListGetRequestBody> chamadoList = chamadoController.listAllByUser(GeneralCreator.userDetailSupporterValid()).getBody();
		
		Assertions.assertThat(chamadoList).isNotEmpty().isNotNull();
		Assertions.assertThat(chamadoList.getContent().contains(expectedChamado));
		Assertions.assertThat(chamadoList.getLinks().contains(selfLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(previusLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(supportLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(adminLink)).isFalse();
	}
	
	@Test
	@DisplayName("Return a CollectionModel of calls open as a body of request")
	void listAllByOpen_ReturnListOfCallsOpen_WhenSuccessfull() {
		ChamadoListGetRequestBody expectedChamado = GeneralCreator.chamadoDTOClassificatedValid();
		Link selfLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).slash("open").withSelfRel();
		Link previusLink = WebMvcLinkBuilder.linkTo(ChamadoController.class).withRel("Chamados");
		
		CollectionModel<ChamadoListGetRequestBody> chamadoList = chamadoController.listAllByOpen().getBody();
		
		Assertions.assertThat(chamadoList).isNotEmpty().isNotNull();
		Assertions.assertThat(chamadoList.getContent().contains(expectedChamado));
		Assertions.assertThat(chamadoList.getLinks().contains(selfLink)).isTrue();
		Assertions.assertThat(chamadoList.getLinks().contains(previusLink)).isTrue();
	}
	
	@Test
	@DisplayName("Save and return a ChamadoModel as a body of request")
	void openCall_ReturnChamado_WhenSuccessfull() {
		ChamadoModel chamado = chamadoController.openCall(GeneralCreator.chamadoPostValid(), GeneralCreator.userDetailCommunValid()).getBody();
		
		Assertions.assertThat(chamado).isNotNull().isEqualTo(GeneralCreator.chamadoJustCreatedValid());
	}
	
	@Test
	@DisplayName("Classificate a call")
	void classificateCall_ClassificateChamado_WhenSuccessfull() {
		Assertions.assertThatCode(() -> chamadoController.classifcateCall(GeneralCreator.chamadoClassificationPut()))
			.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = chamadoController.classifcateCall(GeneralCreator.chamadoClassificationPut());
		
		Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Close a call")
	void closingCall_CloseChamado_WhenSuccessfull() {
		Assertions.assertThatCode(() -> chamadoController.closingCall(GeneralCreator.chamadoClosingPut(), GeneralCreator.userDetailSupporterValid()))
			.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = chamadoController.closingCall(GeneralCreator.chamadoClosingPut(), GeneralCreator.userDetailSupporterValid());
		
		Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("Delete a call")
	void delete_CloseChamado_WhenSuccessfull() {
		Assertions.assertThatCode(() -> chamadoController.delete(GeneralCreator.chamadoJustCreatedValid().getId()))
			.doesNotThrowAnyException();
		
		ResponseEntity<Void> entity = chamadoController.delete(GeneralCreator.chamadoJustCreatedValid().getId());
		
		Assertions.assertThat(entity).isNotNull();
		Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
