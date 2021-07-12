package il.asulin.NoteApp.repos;

import org.springframework.data.repository.CrudRepository;

import il.asulin.NoteApp.beans.User;

public interface UserRepo extends CrudRepository<User, Integer> {

	User findByEmail(String email);
}
