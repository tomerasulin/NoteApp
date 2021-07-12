package il.asulin.NoteApp.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import il.asulin.NoteApp.beans.Note;
import il.asulin.NoteApp.security.JwtTokenUtil;
import il.asulin.NoteApp.services.NoteService;


@CrossOrigin
@RestController
@RequestMapping("note")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private Gson gson;
	private GsonBuilder builder = new GsonBuilder();

	//function that convert json like string into Note.class
	public Note getNote(String str) {
		gson = builder.create();
		return gson.fromJson(str, Note.class);
	}

	//function that makes authenticate infront of the server on each HTTP request
	public String authenticate() {
		String token = this.request.getHeader("Authorization");
		String email = null;
		if(token != null && token.startsWith("Bearer ")) {
			email = this.jwtTokenUtil.getUsernameFromToken(token.substring(7));
		}		
		return email;	
	}

	@PostMapping("add")
	public ResponseEntity<Void> add(@RequestBody String response) {
		String email = authenticate();
		if(email != null) {
			Note noteToAdd = getNote(response);
			//when adding a new note we set to default values the isRead and color attributes 
			noteToAdd.setRead(false);
			noteToAdd.setColor("black");
			this.noteService.addNote(noteToAdd,email);
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("updateColor")
	public ResponseEntity<Void> updateColor(@RequestBody String response) {
		String email = authenticate();
		if(email != null) {
			Note noteToUpdate = getNote(response);
			this.noteService.updateNoteColor(noteToUpdate);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	@PutMapping("updateIcon")
	public ResponseEntity<Void> updateIcon(@RequestBody String response) {
		String email = authenticate();
		if(email != null) {
			Note noteToUpdate = getNote(response);
			this.noteService.updateNoteIcon(noteToUpdate);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("delete")
	public ResponseEntity<Void> delete(@RequestParam int id) {
		String email = authenticate();
		if(email != null) {
			this.noteService.deleteNote(id, email);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("getNote")
	public ResponseEntity<?> getNote(@RequestParam int id) {
		Note note = this.noteService.getNote(id);
		if(note != null) {
			return new ResponseEntity<Note>(note,HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("ID does not exists",HttpStatus.BAD_REQUEST);
	}

	@GetMapping("getAll")
	public ResponseEntity<?> getAll(){
		String email = authenticate();
		if(email != null) {
			List<Note> notes = new ArrayList<Note>();
			notes = this.noteService.getAllNotes(email);
			return new ResponseEntity<List<Note>>(notes,HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
}
