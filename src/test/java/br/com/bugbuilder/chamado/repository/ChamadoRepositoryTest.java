package br.com.bugbuilder.chamado.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.models.ClassificationName;
import br.com.bugbuilder.chamado.models.UserChamadoModel;
import br.com.bugbuilder.chamado.util.DateTimeUtil;
import br.com.bugbuilder.chamado.util.GeneralCreator;

@DataJpaTest
@DisplayName("Test for Chamado repository")
public class ChamadoRepositoryTest {
	private final DateTimeUtil dateTimeUtil = new DateTimeUtil();
	
	@Autowired
	private ChamadoRepository chamadoRepository;	
	
	@Test
	@DisplayName("Save a new call")
	void save_persistChamado_WhenSuccessfull() {
		ChamadoModel chamadoToBeSaved = GeneralCreator.chamadoToBeSaved();
		ChamadoModel chamadoSaved = chamadoRepository.save(chamadoToBeSaved);
		
		Assertions.assertThat(chamadoSaved).isNotNull();
		Assertions.assertThat(chamadoSaved.getId()).isNotNull();
		Assertions.assertThat(chamadoSaved.getRequesterUser()).isNotNull();
		Assertions.assertThat(chamadoSaved.getRequesterUser()).isEqualTo(chamadoSaved.getRequesterUser());
		Assertions.assertThat(chamadoSaved.getAttendantUser()).isNull();
		Assertions.assertThat(chamadoSaved.getTitle()).isEqualTo(chamadoToBeSaved.getTitle());
		Assertions.assertThat(chamadoSaved.isOpen()).isTrue();
	}
	
	@Test
	@DisplayName("Update a call with a classification")
	void save_classificateChamado_whenSuccessfull() {
		ChamadoModel chamadoToBeSaved = GeneralCreator.chamadoToBeSaved();
		ChamadoModel chamadoSaved = chamadoRepository.save(chamadoToBeSaved);
		
		chamadoSaved.setClassificationName(ClassificationName.URGENTE);
		chamadoSaved.setClassificationCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now()));
		
		ChamadoModel chamadoClassificated = chamadoRepository.save(chamadoSaved);
		
		Assertions.assertThat(chamadoClassificated).isNotNull();
		Assertions.assertThat(chamadoClassificated.getId()).isNotNull();
		Assertions.assertThat(chamadoClassificated.getClassificationCallDate()).isNotNull();
		Assertions.assertThat(chamadoClassificated.getClassificationName()).isEqualTo(chamadoSaved.getClassificationName());
		Assertions.assertThat(chamadoClassificated.getClassificationCallDate()).isEqualTo(chamadoSaved.getClassificationCallDate());
	}
	
	@Test
	@DisplayName("Update a call to a closed call")
	void save_closeChamado_whenSuccessfull() {
		ChamadoModel chamadoToBeSaved = GeneralCreator.chamadoToBeSaved();
		ChamadoModel chamadoSaved = chamadoRepository.save(chamadoToBeSaved);
		UserChamadoModel userChamado = GeneralCreator.userChamadoToBeSaved("Spporter01", UUID.randomUUID());
		
		chamadoSaved.setOpen(false);;
		chamadoSaved.setAttendantUser(userChamado);
		chamadoSaved.setClosingCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now()));
		
		ChamadoModel chamadoClosed = chamadoRepository.save(chamadoSaved);
		
		Assertions.assertThat(chamadoClosed).isNotNull();
		Assertions.assertThat(chamadoClosed.getId()).isNotNull();
		Assertions.assertThat(chamadoClosed.getAttendantUser()).isNotNull();
		Assertions.assertThat(chamadoClosed.getAttendantUser()).isEqualTo(chamadoSaved.getAttendantUser());
		Assertions.assertThat(chamadoClosed.getClosingCallDate()).isNotNull();
		Assertions.assertThat(chamadoClosed.getClosingCallDate()).isEqualTo(chamadoSaved.getClosingCallDate());
		Assertions.assertThat(chamadoClosed.isOpen()).isFalse();
	}
	
	@Test
	@DisplayName("Delete a call")
	void delete_removeChamado_whenSuccessfull() {
		ChamadoModel chamadoToBeSaved = GeneralCreator.chamadoToBeSaved();
		ChamadoModel chamadoSaved = chamadoRepository.save(chamadoToBeSaved);
		
		chamadoRepository.delete(chamadoSaved);
		
		Optional<ChamadoModel> chamadOptional = chamadoRepository.findById(chamadoSaved.getId());
		
		Assertions.assertThat(chamadOptional).isEmpty();
	}
	
}
