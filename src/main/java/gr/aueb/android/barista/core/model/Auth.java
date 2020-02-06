package gr.aueb.android.barista.core.model;


import gr.aueb.android.barista.utilities.BaristaCommandPrefixes;

/**
 * Cloned From  https://github.com/bzafiris/barrista.git
 *
 *  Used for executiong auth telnet command. Auth command is used to authnticate a connection
 *  in order to execute telnet commands that require elevated privilleges.For the authorization
 *  an authorization token is required that is generated by telnet at the home directory of the user.
 *
 */
public class Auth extends AbstractTelnetCommand {

	/**
	 *  The string representaion of the authorization token
	 */
	private String authToken;

	/**
	 *  Default constructor tha requires the authorization token
	 *
	 * @param authToken The authorizationToken
	 */
	public Auth(String authToken) {
		super();
		this.authToken = authToken;
	}

	/**
	 *  The function that returns the actual telnet command to be executed.
	 *  In this case the command is 'auth [token]'
	 *
	 * @return
	 */
	@Override
	public String getCommandString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(BaristaCommandPrefixes.AUTH)
				.append(" ")
				.append(authToken)
				.append("\r\n");
		return buffer.toString();
	}

}