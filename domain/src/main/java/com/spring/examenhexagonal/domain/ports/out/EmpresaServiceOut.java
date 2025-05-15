package com.spring.examenhexagonal.domain.ports.out;

import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaRequestDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;

import java.io.IOException;
import java.util.List;

public interface EmpresaServiceOut {

    ResponseBase<EmpresaDTO> getByIdOut(Long id);

    ResponseBase<List<EmpresaDTO>> getAllOut();

    ResponseBase<EmpresaDTO> saveOut(String numeroDocumento);

    ResponseBase<EmpresaDTO> saveOutByRestTemplate(String numeroDocumento);

    ResponseBase<EmpresaDTO> saveOutByRetrofit(String numeroDocumento) throws IOException;

    ResponseBase<EmpresaDTO> updateOut(Long id, EmpresaRequestDTO empresaRequestDTO);

    ResponseBase<String> deleteOut(Long id);
}
