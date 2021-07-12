package il.asulin.NoteApp.repos;

import org.springframework.data.repository.CrudRepository;

import il.asulin.NoteApp.beans.Note;

public interface NoteRepo extends CrudRepository<Note, Integer> {
}