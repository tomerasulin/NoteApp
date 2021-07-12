package il.asulin.NoteApp.beans;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtRequest implements Serializable {
	private static final long serialVersionUID = 6379321618584392814L;

	private String email;
	private String password;
	
}
