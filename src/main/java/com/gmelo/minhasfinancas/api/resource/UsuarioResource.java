package com.gmelo.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmelo.minhasfinancas.api.dto.UsuarioDTO;
import com.gmelo.minhasfinancas.exception.ErroAutenticacao;
import com.gmelo.minhasfinancas.exception.RegraNegocioException;
import com.gmelo.minhasfinancas.model.entity.Usuario;
import com.gmelo.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {
	
	private final UsuarioService service;
	//private final PasswordEncoder encoder;
	
	@PostMapping("/autenticar")
	public ResponseEntity<?> autenticar( @RequestBody UsuarioDTO dto ) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
//			String token = jwtService.gerarToken(usuarioAutenticado);
//			TokenDTO tokenDTO = new TokenDTO( usuarioAutenticado.getNome(), token);
//			return ResponseEntity.ok(tokenDTO);
			return ResponseEntity.ok(usuarioAutenticado);
		}catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity salvar( @RequestBody UsuarioDTO dto ) {
		
//		String senhaCodificada = encoder.encode(dto.getSenha());
		
		Usuario usuario = Usuario.builder()
					.nome(dto.getNome())
					.email(dto.getEmail())
//					.senha(senhaCodificada)
					.senha(dto.getSenha())
					.build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario( usuario );
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

}
