package br.com.bugbuilder.chamado.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.RepresentationModel;

import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.models.ClassificationName;
import br.com.bugbuilder.chamado.models.UserChamadoModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChamadoListGetRequestBody extends RepresentationModel<ChamadoListGetRequestBody>{
	private UUID id;
	private String title;
	private String openingCallDate;
	private boolean open;
	private ClassificationName classificationName;
	private UserChamadoModel requesterUser;
	
	public ChamadoListGetRequestBody convert(ChamadoModel chamadoModel) {
		BeanUtils.copyProperties(chamadoModel, this, "descripyion", "attendantUser", 
				"closingCallDate", "classificationCallDate");
		return this;
	}
	
	public List<ChamadoListGetRequestBody> convertList(List<ChamadoModel> listChamado) {
		
		List<ChamadoListGetRequestBody> chamadoDTOList = new ArrayList<>();
		listChamado.forEach(item -> {
			ChamadoListGetRequestBody chamado = new ChamadoListGetRequestBody();
			chamadoDTOList.add(chamado.convert(item));
		});
		
		return chamadoDTOList;
	}
}
