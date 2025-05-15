package com.spring.examenhexagonal.infrastructure.adapters;

import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaRequestDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;
import com.spring.examenhexagonal.domain.ports.out.EmpresaServiceOut;
import com.spring.examenhexagonal.infrastructure.entity.EmpresaEntity;
import com.spring.examenhexagonal.infrastructure.exception.ConsultaSunatException;
import com.spring.examenhexagonal.infrastructure.exception.DuplicateResourceException;
import com.spring.examenhexagonal.infrastructure.exception.ResourceNotFoundException;
import com.spring.examenhexagonal.infrastructure.repository.EmpresaRepository;
import com.spring.examenhexagonal.infrastructure.response.ResponseSunat;
import com.spring.examenhexagonal.infrastructure.rest.SunatClient;
import com.spring.examenhexagonal.infrastructure.retrofit.ClientRetrofit;
import com.spring.examenhexagonal.infrastructure.retrofit.ClientSunatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
public class EmpresaAdapter implements EmpresaServiceOut {

    private final SunatClient sunatClient;
    private final EmpresaRepository empresaRepository;
    private final RestTemplate restTemplate;

    ClientSunatService retrofitPreConfig = ClientRetrofit.getRetrofit().create(ClientSunatService.class);

    @Value("${api.token}")
    private String token;

    @Override
    public ResponseBase<EmpresaDTO> getByIdOut(Long id) {
        EmpresaEntity empresaEntity = empresaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Empresa no encontrada con el id:" + id));

        EmpresaDTO empresaDTO = toDto(empresaEntity);

        return new ResponseBase<>("Empresa encontrada",200,Optional.of(empresaDTO));
    }

    @Override
    public ResponseBase<List<EmpresaDTO>> getAllOut() {
        List<EmpresaEntity> empresaEntities = empresaRepository.findAll();
        if(empresaEntities.isEmpty()){
            throw new ResourceNotFoundException("No hay empresas registradas");
        }
        List<EmpresaDTO> empresaDTOS = empresaEntities.stream().map(this::toDto).toList();

        return new ResponseBase<>("Lista de empresas",200,Optional.of(empresaDTOS));
    }

    @Override
    public ResponseBase<EmpresaDTO> saveOut(String numeroDocumento) {
        validarNumeroDocumentDuplicado(numeroDocumento);

        ResponseSunat responseSunat = executeSunat(numeroDocumento);

        if(responseSunat == null){
            throw new ResourceNotFoundException("La empresa no tiene datos registrados");
        }

        EmpresaEntity empresaEntity = mapResponseToEntity(responseSunat);
        empresaRepository.save(empresaEntity);

        EmpresaDTO empresaDTO = toDto(empresaEntity);
        return new ResponseBase<>("Empresa creada mediante FeignClient",200,Optional.of(empresaDTO));

    }

    @Override
    public ResponseBase<EmpresaDTO> saveOutByRestTemplate(String numeroDocumento) {
       validarNumeroDocumentDuplicado(numeroDocumento);

        ResponseSunat responseSunat = executeSunatByRestTemplate(numeroDocumento);

        if(responseSunat == null){
            throw new ResourceNotFoundException("La empresa no tiene datos registrados");
        }

        EmpresaEntity empresaEntity = mapResponseToEntity(responseSunat);
        empresaRepository.save(empresaEntity);
        EmpresaDTO empresaDTO = toDto(empresaEntity);

        return new ResponseBase<>("Empresa creada mediante RestTemplate",200,Optional.of(empresaDTO));
    }

    @Override
    public ResponseBase<EmpresaDTO> saveOutByRetrofit(String numeroDocumento) throws IOException {
        validarNumeroDocumentDuplicado(numeroDocumento);

        Response<ResponseSunat> response = preparedClient(numeroDocumento).execute();
        if(response.isSuccessful() && response.body() != null){
            EmpresaEntity empresaEntity = mapResponseToEntity(response.body());
            empresaRepository.save(empresaEntity);
            EmpresaDTO empresaDTO = toDto(empresaEntity);
            return new ResponseBase<>("Empresa creada mediante Retrofit",200,Optional.of(empresaDTO));

        }else {
            throw new ConsultaSunatException("Ocurrio un error al consultar sunat");
        }

 }

    @Override
    public ResponseBase<EmpresaDTO> updateOut(Long id, EmpresaRequestDTO empresaRequestDTO) {
        EmpresaEntity empresaEntity = empresaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Empresa no encontrado con el id:" + id));

        empresaEntity.setRazonSocial(empresaRequestDTO.getRazonSocial());
        empresaEntity.setEstado(empresaRequestDTO.getEstado());
        empresaEntity.setCondicion(empresaRequestDTO.getCondicion());
        empresaEntity.setDireccion(empresaRequestDTO.getDistrito());
        empresaEntity.setDepartamento(empresaRequestDTO.getDepartamento());
        empresaEntity.setProvincia(empresaRequestDTO.getProvincia());
        empresaEntity.setDistrito(empresaRequestDTO.getDistrito());
        empresaEntity.setActividadEconomica(empresaRequestDTO.getActividadEconomica());
        empresaEntity.setNumeroTrabajadores(empresaRequestDTO.getNumeroTrabajadores());

        EmpresaEntity updateEmpresa = empresaRepository.save(empresaEntity);

        EmpresaDTO empresaDTO = toDto(updateEmpresa);

        return new ResponseBase<>("Empresa actualizada",200,Optional.of(empresaDTO)) ;
    }

    @Override
    public ResponseBase<String> deleteOut(Long id) {
        EmpresaEntity empresaEntity = empresaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Empresa no encontrada con el id:" + id));
        empresaRepository.delete(empresaEntity);
        return new ResponseBase<>("Empresa eliminada con el id : " + id,200,Optional.empty());

    }

    //*****************METODOS DE APOYO*******************************

    private ResponseSunat executeSunat(String numeroDocumento) {
        try {
            String header = "Bearer " + token;
            return sunatClient.getInfoSunat(numeroDocumento, header);
        }catch (Exception e){
            throw new ConsultaSunatException("Ocurrio un error inesperado al consultar sunat  : " + e.getMessage());
        }
    }

    private ResponseSunat executeSunatByRestTemplate(String numeroDocumento) {
        try {
            String url ="https://api.apis.net.pe/v2/sunat/ruc/full?numero="+numeroDocumento;
            ResponseEntity<ResponseSunat> response = restTemplate.exchange(url,HttpMethod.GET,new HttpEntity<>(createHeaders()),ResponseSunat.class);
            return response.getBody();
        }catch (Exception e){
            throw new ConsultaSunatException("Ocurrio un error inesperado al consultar sunat  : " + e.getMessage());
        }
    }

    private Call<ResponseSunat> preparedClient(String numeroDocumento) {
        return retrofitPreConfig.findSunat("Bearer " + token, numeroDocumento);
    }

    private void validarNumeroDocumentDuplicado(String numeroDocumento) {
        if (empresaRepository.existsByNumeroDocumento(numeroDocumento)) {
            throw new DuplicateResourceException("El numero de documento ya esta registrado : " + numeroDocumento);
        }
    }

    private EmpresaEntity mapResponseToEntity(ResponseSunat response){
        return EmpresaEntity.builder()
                .razonSocial(response.getRazonSocial())
                .numeroDocumento(response.getNumeroDocumento())
                .estado(response.getEstado())
                .condicion(response.getCondicion())
                .direccion(response.getDireccion())
                .departamento(response.getDepartamento())
                .provincia(response.getProvincia())
                .distrito(response.getDistrito())
                .actividadEconomica(response.getActividadEconomica())
                .numeroTrabajadores(response.getNumeroTrabajadores())
                .build();

    }

    private EmpresaDTO toDto(EmpresaEntity entity) {
        EmpresaDTO dto = new EmpresaDTO();
        dto.setId(entity.getId());
        dto.setRazonSocial(entity.getRazonSocial());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setEstado(entity.getEstado());
        dto.setCondicion(entity.getCondicion());
        dto.setDireccion(entity.getDireccion());
        dto.setDepartamento(entity.getDepartamento());
        dto.setProvincia(entity.getProvincia());
        dto.setDistrito(entity.getDistrito());
        dto.setActividadEconomica(entity.getActividadEconomica());
        dto.setNumeroTrabajadores(entity.getNumeroTrabajadores());
        return dto;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization","Bearer "+token);
        return header;
    }
}
