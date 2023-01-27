package br.com.bugbuilder.chamado.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.models.ClassificationName;
import br.com.bugbuilder.chamado.models.RoleModel;
import br.com.bugbuilder.chamado.models.RoleName;
import br.com.bugbuilder.chamado.models.UserChamadoModel;
import br.com.bugbuilder.chamado.models.UserModel;
import br.com.bugbuilder.chamado.requests.ChamadoClassificationPutRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoClosingPutRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoListGetRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoPostRequestBody;

public class GeneralCreator {
	
	public static RoleModel roleAdminValid() {
		RoleModel roleAdmin = new RoleModel();
		roleAdmin.setRoleId(UUID.fromString("c0b5c616-6f47-46b6-bd20-162e25b509e4"));
		roleAdmin.setRoleName(RoleName.ROLE_ADMIN);
		return roleAdmin;
	}
	
	public static RoleModel roleCommunValid() {
		RoleModel roleAdmin = new RoleModel();
		roleAdmin.setRoleId(UUID.fromString("a0b5c616-6f47-46b6-bd20-162e25b509e4"));
		roleAdmin.setRoleName(RoleName.ROLE_COMMUN_USER);
		return roleAdmin;
	}
	
	public static RoleModel rolesupporterValid() {
		RoleModel roleAdmin = new RoleModel();
		roleAdmin.setRoleId(UUID.fromString("4093b733-2498-44b7-8ce9-e5a24685fc8d"));
		roleAdmin.setRoleName(RoleName.ROLE_SUPPORTER);
		return roleAdmin;
	}
	
	public static UserModel userValid() {
		UserModel user = new UserModel();
		user.setUserId(UUID.fromString("7f385bba-1696-4ebe-8ad8-1649ea6b48d7"));
		user.setPass("SecretPass");
		user.setUsername("User01");
		return user;
	}
	
	public static UserDetails userDetailAdminValid() {
		List<RoleModel> list = new ArrayList<>();
		list.add(roleAdminValid());
		Collection<? extends GrantedAuthority> authorities = list;
		User user = new User("User01", "SecretPass", true, true, true, true, authorities);
		UserDetails userDetails = user;
		return userDetails;
	}
	
	public static UserDetails userDetailCommunValid() {
		List<RoleModel> list = new ArrayList<>();
		list.add(roleCommunValid());
		Collection<? extends GrantedAuthority> authorities = list;
		User user = new User("User01", "SecretPass", true, true, true, true, authorities);
		UserDetails userDetails = user;
		return userDetails;
	}
	
	public static UserDetails userDetailSupporterValid() {
		List<RoleModel> list = new ArrayList<>();
		list.add(rolesupporterValid());
		Collection<? extends GrantedAuthority> authorities = list;
		User user = new User("User01", "SecretPass", true, true, true, true, authorities);
		UserDetails userDetails = user;
		return userDetails;
	}

	public static UserChamadoModel userChamadoToBeSaved() {
		return UserChamadoModel.builder()
				.userId(UUID.fromString("7f385bba-1696-4ebe-8ad8-1649ea6b48d7"))
				.username("User01").build();
	}
	
	public static UserChamadoModel userChamadoToBeSaved(String name, UUID id) {
		return UserChamadoModel.builder()
				.userId(id)
				.username(name).build();
	}
	
	public static ChamadoModel chamadoToBeSaved() {
		return ChamadoModel.builder()
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(true)
				.classificationName(ClassificationName.NAO_CLASSIFICADO)
				.closingCallDate(null)
				.classificationCallDate(null)
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(null)
				.requesterUser(userChamadoToBeSaved())
				.build();
	}
	
	public static ChamadoModel chamadoToBeSaved(UserChamadoModel requesterUser) {
		return ChamadoModel.builder()
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(true)
				.classificationName(ClassificationName.NAO_CLASSIFICADO)
				.closingCallDate(null)
				.classificationCallDate(null)
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(null)
				.requesterUser(requesterUser)
				.build();
	}
	
	public static ChamadoModel chamadoJustCreatedValid() {
		return ChamadoModel.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(true)
				.classificationName(ClassificationName.NAO_CLASSIFICADO)
				.closingCallDate(null)
				.classificationCallDate(null)
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(null)
				.requesterUser(userChamadoToBeSaved())
				.build();
	}
	
	public static ChamadoModel chamadoJustClassificatedValid() {
		return ChamadoModel.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(true)
				.classificationName(ClassificationName.URGENTE)
				.closingCallDate(null)
				.classificationCallDate("2023-01-15 15:01:09")
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(null)
				.requesterUser(userChamadoToBeSaved())
				.build();
	}
	
	public static ChamadoModel chamdoClosedValid() {
		return ChamadoModel.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(false)
				.classificationName(ClassificationName.URGENTE)
				.closingCallDate("2023-01-15 15:19:53")
				.classificationCallDate("2023-01-15 15:01:09")
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(userChamadoToBeSaved())//"Test02", UUID.fromString("f16fddd6-1efc-474f-85fd-dec18a1b1945"))
				.requesterUser(userChamadoToBeSaved())
				.build();
		
	}
	
	public static ChamadoModel chamdoClosedValid(UserChamadoModel attendanteUser) {
		return ChamadoModel.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.description("Test 01, description 01.")
				.open(false)
				.classificationName(ClassificationName.URGENTE)
				.closingCallDate("2023-01-15 15:19:53")
				.classificationCallDate("2023-01-15 15:01:09")
				.openingCallDate("2023-01-15 14:42:31")
				.attendantUser(attendanteUser)
				.requesterUser(userChamadoToBeSaved())
				.build();
	}
	
	public static ChamadoListGetRequestBody chamadoDTOJustCreatedValid() {
		return ChamadoListGetRequestBody.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.open(true)
				.classificationName(ClassificationName.NAO_CLASSIFICADO)
				.openingCallDate("2023-01-15 15:19:53")
				.requesterUser(userChamadoToBeSaved()).build();
	}
	
	public static ChamadoListGetRequestBody chamadoDTOClassificatedValid() {
		return ChamadoListGetRequestBody.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.open(true)
				.classificationName(ClassificationName.URGENTE)
				.openingCallDate("2023-01-15 15:19:53")
				.requesterUser(userChamadoToBeSaved()).build();
	}
	
	public static ChamadoListGetRequestBody chamadoDTOClosedValid() {
		return ChamadoListGetRequestBody.builder()
				.id(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"))
				.title("Test 01")
				.open(false)
				.classificationName(ClassificationName.URGENTE)
				.openingCallDate("2023-01-15 15:19:53")
				.requesterUser(userChamadoToBeSaved()).build();
	}
	
	public static ChamadoPostRequestBody chamadoPostValid() {
		ChamadoPostRequestBody chamadoPost = new ChamadoPostRequestBody();
		chamadoPost.setTitle("Test 01");
		chamadoPost.setDescription("Test 01, description 01.");
		return chamadoPost;
	}
	
	public static ChamadoClassificationPutRequestBody chamadoClassificationPut() {
		return ChamadoClassificationPutRequestBody.builder()
				.classificationName(ClassificationName.URGENTE).build();
	}
	
	public static ChamadoClosingPutRequestBody chamadoClosingPut() {
		ChamadoClosingPutRequestBody chamado = new ChamadoClosingPutRequestBody();
		chamado.setId(UUID.fromString("f11caf35-57d7-455c-8a8a-b01019ee835c"));
		return chamado;
	}
}
