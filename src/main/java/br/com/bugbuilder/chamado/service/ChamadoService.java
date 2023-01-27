package br.com.bugbuilder.chamado.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.bugbuilder.chamado.exceptions.BadRequestException;
import br.com.bugbuilder.chamado.models.ChamadoModel;
import br.com.bugbuilder.chamado.models.ClassificationName;
import br.com.bugbuilder.chamado.models.UserChamadoModel;
import br.com.bugbuilder.chamado.repository.ChamadoRepository;
import br.com.bugbuilder.chamado.repository.UserChamadoRepository;
import br.com.bugbuilder.chamado.requests.ChamadoListGetRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoClassificationPutRequestBody;
import br.com.bugbuilder.chamado.requests.ChamadoPostRequestBody;
import br.com.bugbuilder.chamado.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChamadoService {
	
	@Autowired
	private final DateTimeUtil dateTimeUtil;
	private final ChamadoRepository chamadoRepository;
	private final UserChamadoRepository userChamadoRepository;
	
	public Page<ChamadoModel> listAll(Pageable pageable) {
		return chamadoRepository.findAll(pageable);
	}

	public ChamadoModel findByIdOrElseThrowBadRequest(UUID uuid) {
		
		return chamadoRepository.findById(uuid)
				.orElseThrow(() -> new BadRequestException("Chamado com ID " + uuid + " não encontrado."));
	}
	
	@Transactional
	public ChamadoModel save(ChamadoPostRequestBody chamadoPost, UserDetails userDetails) {
		UserChamadoModel user = this.getUserModelByUserDetails(userDetails);
		return chamadoRepository.save(ChamadoModel.builder()
				.title(chamadoPost.getTitle()).description(chamadoPost.getDescription())
				.open(true).classificationName(ClassificationName.NAO_CLASSIFICADO)
				.openingCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now()))
				.attendantUser(null).closingCallDate(null).requesterUser(user)
				.classificationCallDate(null).build());
	}

	public void delete(UUID id) {
		chamadoRepository.delete(this.findByIdOrElseThrowBadRequest(id));
	}

	public void replaceClassificationCall(ChamadoClassificationPutRequestBody chamadoClassification, UUID id) {
		ChamadoModel saveCall = this.findByIdOrElseThrowBadRequest(id);
		ChamadoModel chamado = ChamadoModel.builder()
				.id(saveCall.getId()).title(saveCall.getTitle()).description(saveCall.getDescription())
				.open(saveCall.isOpen()).classificationName(chamadoClassification.getClassificationName())
				.openingCallDate(saveCall.getOpeningCallDate()).closingCallDate(null).attendantUser(null)
				.requesterUser(saveCall.getRequesterUser())
				.classificationCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now())).build();
		chamadoRepository.save(chamado);
	}

	public void replaceClosingCall(UUID id, UserDetails userDetails) {
		UserChamadoModel user = this.getUserModelByUserDetails(userDetails);
		ChamadoModel saveCall = this.findByIdOrElseThrowBadRequest(id);
		ChamadoModel chamado = ChamadoModel.builder()
				.id(saveCall.getId()).title(saveCall.getTitle()).description(saveCall.getDescription()).open(false)
				.classificationName(saveCall.getClassificationName()).openingCallDate(saveCall.getOpeningCallDate())
				.closingCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now()))
				.attendantUser(user).requesterUser(saveCall.getRequesterUser())
				.classificationCallDate(dateTimeUtil.formatLocalDateTimeBaseStyle(LocalDateTime.now())).build();
		chamadoRepository.save(chamado);
	}
	
	UserChamadoModel getUserModelByUserDetails(UserDetails userDetails) {
		List<UserChamadoModel> userChamado = userChamadoRepository.findByUsername(userDetails.getUsername());
		if(userChamado.isEmpty()) {
			return null;
		}else {
			return userChamado.get(0);
		}
	}
	
	@Transactional
	public List<ChamadoListGetRequestBody> listAllByRequestUser(UserDetails userDetails) {
		UserChamadoModel user = this.getUserModelByUserDetails(userDetails);
		ChamadoListGetRequestBody chamadoDTO = new ChamadoListGetRequestBody();
		return chamadoDTO.convertList(chamadoRepository.findAllByRequesterUser(user.getUserId()));
	}
	
	@Transactional
	public List<ChamadoListGetRequestBody> listAllByAttendantUser(UserDetails userDetails) {
		UserChamadoModel user = this.getUserModelByUserDetails(userDetails);
		ChamadoListGetRequestBody chamadoDTO = new ChamadoListGetRequestBody();
		return chamadoDTO.convertList(chamadoRepository.findAllByAttendantUser(user.getUserId()));
	}
	
	@Transactional
	public List<ChamadoListGetRequestBody> listAllByOpen() {
		ChamadoListGetRequestBody chamadoDTO = new ChamadoListGetRequestBody();
		return chamadoDTO.convertList(chamadoRepository.findAllByOpenCall());
	}
	
	@Transactional
	public List<ChamadoListGetRequestBody> listAllByAttendantOrOpen(UserDetails userDetails) {
		UserChamadoModel user = this.getUserModelByUserDetails(userDetails);
		ChamadoListGetRequestBody chamadoDTO = new ChamadoListGetRequestBody();
		return chamadoDTO.convertList(chamadoRepository.findAllByAttendantOrOpen(user.getUserId()));
	}
		
}
