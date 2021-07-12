package il.asulin.NoteApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import il.asulin.NoteApp.services.JwtUserDetailsService;
import il.asulin.NoteApp.security.JwtTokenUtil;
import il.asulin.NoteApp.beans.JwtRequest;
import il.asulin.NoteApp.beans.JwtResponse;
import il.asulin.NoteApp.beans.User;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	//handle registration
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) {
		try {
			this.userDetailsService.save(user);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
	}

	//authenticate the user to the server
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String email, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	//getting user details
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public ResponseEntity<?> getUSer(@RequestBody int id){
		try {
			return new ResponseEntity<User>(this.userDetailsService.loadUserById(id),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>("ID doesnt exists",HttpStatus.BAD_REQUEST);
		}
	}


	//getting all users details
	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers(){
		return new ResponseEntity<List<User>>(this.userDetailsService.getAllUsers(),HttpStatus.OK);
	}

	//delete a user by given an id
	@RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@RequestParam int id){
		try {
			this.userDetailsService.deleteUser(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<String>("User ID does not exists",HttpStatus.BAD_REQUEST);
		}

	}
}
