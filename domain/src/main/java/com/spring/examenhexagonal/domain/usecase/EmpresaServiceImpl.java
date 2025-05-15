package com.spring.examenhexagonal.domain.usecase;

import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.EmpresaRequestDTO;
import com.spring.examenhexagonal.domain.aggregates.dto.ResponseBase;
import com.spring.examenhexagonal.domain.ports.in.EmpresaServiceIn;
import com.spring.examenhexagonal.domain.ports.out.EmpresaServiceOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaServiceIn {

    private final EmpresaServiceOut empresaServiceOut;

    @Override
    public ResponseBase<EmpresaDTO> getByIdIn(Long id) {
        return empresaServiceOut.getByIdOut(id);
    }

    @Override
    public ResponseBase<List<EmpresaDTO>> getAllIn() {
        return empresaServiceOut.getAllOut();
    }

    @Override
    public ResponseBase<EmpresaDTO> saveIn(String numeroDocumento) {
        return empresaServiceOut.saveOut(numeroDocumento);
    }

    @Override
    public ResponseBase<EmpresaDTO> saveInByRestTemplate(String numeroDocumento) {
        return empresaServiceOut.saveOutByRestTemplate(numeroDocumento);
    }

    @Override
    public ResponseBase<EmpresaDTO> saveInByRetrofit(String numeroDocumento) throws IOException {
        return empresaServiceOut.saveOutByRetrofit(numeroDocumento);
    }

    @Override
    public ResponseBase<EmpresaDTO> updateIn(Long id, EmpresaRequestDTO empresaRequestDTO) {
        return empresaServiceOut.updateOut(id, empresaRequestDTO);
    }

    @Override
    public ResponseBase<String> deleteIn(Long id) {
        return empresaServiceOut.deleteOut(id);

    }
}
