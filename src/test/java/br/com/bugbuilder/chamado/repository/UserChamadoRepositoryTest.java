package br.com.bugbuilder.chamado.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.bugbuilder.chamado.models.UserChamadoModel;
import br.com.bugbuilder.chamado.util.GeneralCreator;

@DataJpaTest
@DisplayName("Test for UserChamadoRepository")
public class UserChamadoRepositoryTest {
	
	@Autowired
	private UserChamadoRepository userChamadoRepository;
	
	@Test
	@DisplayName("Return a list of userChamdoModel")
	void findByUserName_ReturnListOfUserChamado_WhenSuccessfull() {
		UserChamadoModel userChamado = GeneralCreator.userChamadoToBeSaved();
		UserChamadoModel userChamadoSaved = userChamadoRepository.save(userChamado);
		
		List<UserChamadoModel> userChamadoList = userChamadoRepository.findByUsername(userChamadoSaved.getUsername());
		
		Assertions.assertThat(userChamadoList).isNotEmpty();
		Assertions.assertThat(userChamadoList).contains(userChamadoSaved);
	}
	
	@Test
	@DisplayName("Return a empty list")
	void findByUserName_ReturnEmptyList_WhenNotSuccessfull() {
		List<UserChamadoModel> userChamadoList = userChamadoRepository.findByUsername("User");
		
		Assertions.assertThat(userChamadoList).isEmpty();
	}
}
