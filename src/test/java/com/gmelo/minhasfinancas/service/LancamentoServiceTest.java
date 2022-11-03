package com.gmelo.minhasfinancas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gmelo.minhasfinancas.exception.RegraNegocioException;
import com.gmelo.minhasfinancas.model.entity.Lancamento;
import com.gmelo.minhasfinancas.model.enums.StatusLancamento;
import com.gmelo.minhasfinancas.model.repository.LancamentoRepository;
import com.gmelo.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.gmelo.minhasfinancas.service.impl.LancamentoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificação
		assertThat( lancamento.getId() ).isEqualTo(lancamentoSalvo.getId());
		assertThat( lancamento.getStatus() ).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		doThrow( RegraNegocioException.class ).when(service).validar(lancamentoASalvar);
		
		//execucao e verificacao
		catchThrowableOfType( () -> service.salvar(lancamentoASalvar), RegraNegocioException.class );
		verify(repository, never()).save(lancamentoASalvar);
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//cenário
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);

		doNothing().when(service).validar(lancamentoSalvo);
		
		when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execucao
		service.atualizar(lancamentoSalvo);
		
		//verificação
		verify(repository, times(1)).save(lancamentoSalvo);
		
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//execucao e verificacao
		catchThrowableOfType( () -> service.atualizar(lancamento), NullPointerException.class );
		verify(repository, never()).save(lancamento);
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execucao
		service.deletar(lancamento);
		
		//verificacao
		verify( repository ).delete(lancamento);
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		//execucao
		catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class );
		
		//verificacao
		verify( repository, never() ).delete(lancamento);
	}	

}
