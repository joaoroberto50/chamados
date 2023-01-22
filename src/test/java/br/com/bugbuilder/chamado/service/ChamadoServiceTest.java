package br.com.bugbuilder.chamado.service;

import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.domain.Page;

import br.com.bugbuilder.chamado.exceptions.BadRequestException;
import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.repository.ChamadoRepository;
import br.com.bugbuilder.chamado.repository.UserChamadoRepository;
import br.com.bugbuilder.chamado.requests.ChamadoListGetRequestBody;
import br.com.bugbuilder.chamado.util.GeneralCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("Test for the service chamados")
public class ChamadoServiceTest {
	
	@InjectMocks
	private ChamadoService chamadoService;
	
	@Mock
	private UserChamadoRepository userChamadoRepository;
	
	@Mock
	private ChamadoRepository chamadoRepositoryMock;
	
	@BeforeEach
	void setUP() {
		PageImpl<ChamadoModel> chamadoPage = new PageImpl<>(List.of(GeneralCreator.chamadoJustCreatedValid()));
		BDDMockito.when(chamadoRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
			.thenReturn(chamadoPage);
		
		BDDMockito.when(chamadoRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
			.thenReturn(Optional.of(GeneralCreator.chamadoJustCreatedValid()));
		
		BDDMockito.when(userChamadoRepository.findByUsername(ArgumentMatchers.anyString()))
			.thenReturn(List.of(GeneralCreator.userChamadoToBeSaved()));
		
		BDDMockito.when(chamadoRepositoryMock.findAllByRequesterUser(ArgumentMatchers.any(UUID.class)))
			.thenReturn(List.of(GeneralCreator.chamadoJustCreatedValid()));
		
		BDDMockito.when(chamadoRepositoryMock.findAllByAttendantUser(ArgumentMatchers.any(UUID.class)))
			.thenReturn(List.of(GeneralCreator.chamdoClosedValid()));
		
		BDDMockito.when(chamadoRepositoryMock.findAllByAttendantOrOpen(ArgumentMatchers.any(UUID.class)))
			.thenReturn(List.of(GeneralCreator.chamdoClosedValid(), GeneralCreator.chamadoJustClassificatedValid()));
		
		BDDMockito.when(chamadoRepositoryMock.findAllByOpenCall())
			.thenReturn(List.of(GeneralCreator.chamadoJustCreatedValid()));
		
		BDDMockito.when(chamadoRepositoryMock.save(ArgumentMatchers.any(ChamadoModel.class)))
			 .thenReturn(GeneralCreator.chamadoJustCreatedValid());
		
		BDDMockito.doNothing().when(chamadoRepositoryMock).delete(ArgumentMatchers.any(ChamadoModel.class));
	}
	
	@Test
	@DisplayName("Return a Page with a lis of all of the calls")
	void list_ReturnReturnListOfChamadosInsidePageObject_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		
		Page<ChamadoModel> chamadoPage = chamadoService.listAll(PageRequest.of(1, 1));
		
		Assertions.assertThat(chamadoPage).isNotEmpty();
		
		Assertions.assertThat(chamadoPage.toList()).isNotEmpty();
		Assertions.assertThat(chamadoPage.toList()).hasSize(1);
		
		Assertions.assertThat(chamadoPage.toList().get(0).getTitle()).isEqualTo(expectedChamado.getTitle());
		Assertions.assertThat(chamadoPage.toList().get(0).isOpen()).isTrue();
	}
	
	@Test
	@DisplayName("Return a Call")
	void findById_ReturnChamado_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		
		ChamadoModel chamado = chamadoService.findByIdOrElseThrowBadRequest(expectedChamado.getId());
		
		Assertions.assertThat(chamado).isNotNull();
		Assertions.assertThat(chamado.getId()).isNotNull().isEqualTo(expectedChamado.getId());
	}
	
	@Test
	@DisplayName("Throws BadRequestException")
	void findById_ThrowsBadRequestException_WhenChamadoIsNotFound() {
		BDDMockito.when(chamadoRepositoryMock.findById(ArgumentMatchers.any(UUID.class)))
			.thenReturn(Optional.empty());
		
		Assertions.assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> chamadoService.findByIdOrElseThrowBadRequest(UUID.randomUUID()));
	}
	
	@Test
	@DisplayName("Return a List of calls from a certain user")
	void findAllByRequesterUser_ReturnListWithAllCallByRequesterUser_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByRequestUser(GeneralCreator.userDetailCommunValid());
		
		Assertions.assertThat(chamadoList).isNotEmpty();
		Assertions.assertThat(chamadoList.get(0).getId()).isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamadoList.get(0).getRequesterUser()).isEqualTo(expectedChamado.getRequesterUser());
	}
	
	@Test
	@DisplayName("Return a List of calls from a certain supporter")
	void findAllByAttendantUser_ReturnListWithAllCallByAttendantUser_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamdoClosedValid();
		
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByAttendantUser(GeneralCreator.userDetailSupporterValid());
		
		Assertions.assertThat(chamadoList).isNotEmpty();
		Assertions.assertThat(chamadoList.get(0).getId()).isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamadoList.get(0).getRequesterUser()).isEqualTo(expectedChamado.getRequesterUser());
		Assertions.assertThat(chamadoList.get(0).isOpen()).isFalse();
	}
	
	@Test
	@DisplayName("Return a List of calls from a certain supporter or calls the are open")
	void findAllByAttendantOrOpen_ReturnListWithAllCallByAttendantOrOpen_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamdoClosedValid();
		
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByAttendantOrOpen(GeneralCreator.userDetailSupporterValid());
		
		Assertions.assertThat(chamadoList).isNotEmpty();
		Assertions.assertThat(chamadoList.get(0).getId()).isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamadoList.get(0).getRequesterUser()).isEqualTo(expectedChamado.getRequesterUser());
		Assertions.assertThat(chamadoList.get(0).isOpen()).isFalse();
		Assertions.assertThat(chamadoList.get(1).getId()).isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamadoList.get(1).getRequesterUser()).isEqualTo(expectedChamado.getRequesterUser());
		Assertions.assertThat(chamadoList.get(1).isOpen()).isTrue();
	}
	
	@Test
	@DisplayName("Return a List of calls the are open")
	void findAllByOpenCall_ReturnListWithAllCallOpen_WhenSuccessfull() {
		ChamadoModel expectedChamado = GeneralCreator.chamadoJustCreatedValid();
		
		List<ChamadoListGetRequestBody> chamadoList = chamadoService.listAllByOpen();
		
		Assertions.assertThat(chamadoList).isNotEmpty();
		Assertions.assertThat(chamadoList.get(0).getId()).isEqualTo(expectedChamado.getId());
		Assertions.assertThat(chamadoList.get(0).getRequesterUser()).isEqualTo(expectedChamado.getRequesterUser());
		Assertions.assertThat(chamadoList.get(0).isOpen()).isTrue();
	}
	
	@Test
	@DisplayName("Delete a call")
	void delete_RemoveChamado_WhenSuccessfull() {
		Assertions.assertThatCode(() -> chamadoService.delete(UUID.randomUUID()))
			.doesNotThrowAnyException();
	}
}
