package il.asulin.NoteApp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import il.asulin.NoteApp.beans.Note;
import il.asulin.NoteApp.beans.User;
import il.asulin.NoteApp.repos.NoteRepo;
import il.asulin.NoteApp.repos.UserRepo;

@Service
public class NoteService {

	@Autowired
	private NoteRepo noteRepo;

	@Autowired
	private UserRepo userRepo;


	public void addNote(Note note, String email) {
		User user = this.userRepo.findByEmail(email);
		user.addNote(note);
		this.noteRepo.save(note);
	}

	public void updateNoteColor(Note note) {
		List<Note> allNotes = (List<Note>) this.noteRepo.findAll();
		for(Note n : allNotes) {
			if(n.getId() == note.getId()) {
				n.setColor(note.getColor());
				this.noteRepo.save(n);
				return;
			}
		}
	}

	public void updateNoteIcon(Note note) {
		List<Note> allNotes = (List<Note>) this.noteRepo.findAll();
		for(Note n : allNotes) {
			if(n.getId() == note.getId()) {
				n.setIcon(note.getIcon());
				this.noteRepo.save(n);
				return;
			}
		}
	}

	public void deleteNote(int id, String email) {
		User user = this.userRepo.findByEmail(email);
		if(user != null) {
			Optional<Note> optNote = this.noteRepo.findById(id);
			user.getNotes().remove(optNote.get());
			this.noteRepo.deleteById(id);
		}
	}

	public Note getNote(int id) {
		Optional<Note> noteFromDB =  this.noteRepo.findById(id);
		if(noteFromDB.isEmpty()) return null;
		Note note = noteFromDB.get();
		note.setRead(true);
		this.noteRepo.save(note);
		return note;	
	}


	public List<Note> getAllNotes(String email){
		User user = this.userRepo.findByEmail(email);
		List<Note> notes = new ArrayList<Note>();
		if(user != null) {
			List<Note> dbNotes = (List<Note>)this.noteRepo.findAll();
			for(Note n : dbNotes) {
				if(user.getNotes().contains(n)) {
					notes.add(n);
				}
			}
		}
		return notes;
	}
}
