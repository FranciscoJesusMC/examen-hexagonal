package com.spring.examenhexagonal.domain.ports.in;

import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaRequestDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;

import java.io.IOException;
import java.util.List;

public interface EmpresaServiceIn {

    ResponseBase<EmpresaDTO> getByIdIn(Long id);

    ResponseBase<List<EmpresaDTO>> getAllIn();

    ResponseBase<EmpresaDTO> saveIn(String numeroDocumento);

    ResponseBase<EmpresaDTO> saveInByRestTemplate(String numeroDocumento);

    ResponseBase<EmpresaDTO> saveInByRetrofit(String numeroDocumento) throws IOException;

    ResponseBase<EmpresaDTO> updateIn(Long id, EmpresaRequestDTO empresaRequestDTO);

    ResponseBase<String> deleteIn(Long id);


}
