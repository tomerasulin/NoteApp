package il.asulin.NoteApp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import il.asulin.NoteApp.beans.User;
import il.asulin.NoteApp.repos.UserRepo;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email + "\n please register");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	public User loadUserById(int id) throws IllegalArgumentException{
		return this.userRepo.findById(id).get();
	}

	public void save(User user) throws Exception {
		//gets all users in DB 
		List<User> usersInDB = (List<User>)this.userRepo.findAll();
		for(User u : usersInDB) { //in case email(user) already exists 
			if(u.getEmail().equals(user.getEmail())) {
				throw new Exception("email already exists, Please login...");
			}
		}
		//creating a new user and save him in the DB
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		this.userRepo.save(newUser);
	}

	public List<User> getAllUsers(){
		return (List<User>) this.userRepo.findAll();
	}

	public void deleteUser(int id) throws IllegalArgumentException {
		this.userRepo.deleteById(id);
	}

}