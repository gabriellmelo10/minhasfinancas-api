package com.gmelo.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import com.gmelo.minhasfinancas.exception.ErroAutenticacao;
import com.gmelo.minhasfinancas.exception.RegraNegocioException;
import com.gmelo.minhasfinancas.model.entity.Usuario;
import com.gmelo.minhasfinancas.model.repository.UsuarioRepository;
import com.gmelo.minhasfinancas.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		//boolean senhasBatem = encoder.matches(senha, usuario.get().getSenha());
		
		//if(!senhasBatem) {
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}

		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		this.validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

}
