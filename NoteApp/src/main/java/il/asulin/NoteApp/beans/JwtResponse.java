package il.asulin.NoteApp.beans;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -6422890155545052549L;
	private final String jwttoken;
	
}
