/**
 * 
 */
package application;

import java.util.HashMap;

/**
 * evaluates all given Args of the Applications main method at runtime.
 * rudimental alternative to JCommander using HashMap
 * 
 * @author mosea3@bfh.ch
 * 
 * @usage use "--" as prefix for a key, after a blank space indicate value
 *        escape spaces inside the desired value by enclosing the whole value
 *        with a double Quotation Mark Example "--printer", will set a boolean
 *        value true
 * 
 *        All Settings are stored as general Strings, you'll have to convert
 *        them into your needed Datatypes yourself. 42 != "42" ;-)
 */
public class Args {
	/**
	 * stores the Data
	 */
	private HashMap<String, String> arguments = new HashMap();

	/**
	 * creates an Args Object ready to store Application Environment Data, parses
	 * all arguments into key->value paired settings.
	 * 
	 * @param String[] args - the one of the initial method
	 */
	public Args(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				if (args[i].substring(0, 2).contentEquals("--")) {

					// if a setter parameter without assigned content
					set(args[i].substring(2), "true");

					String content;
					// wenn nächster args kein -- als parameterschlüssel enthält, nutze als
					// parameterinhalt
					try {
						if (!args[i + 1].substring(0, 2).contentEquals("--")) {
							set(args[i].substring(2), args[i + 1]);
						}

					} catch (ArrayIndexOutOfBoundsException e) {
						// catching the last standalone argument
					}
				}

			}
		} catch (Exception e) {
			System.out.println("Houston! we have a problem: " + e.getClass() + ":" + e.getLocalizedMessage());
		}
	}

	/**
	 * returns the value paired to the key
	 * 
	 * @param key
	 * @return String - the Settings value
	 */
	public String get(String key) {
		return this.arguments.get(key);
	}

	/**
	 * sets a Setting with a key and value
	 * 
	 * @param key
	 * @param value
	 * @return returns previous value under that key, or - if none set or set to
	 *         NULL - a NULL
	 */
	public String set(String key, String value) {
		return arguments.put(key, value);
	}

	/**
	 * get all set values as HashMap
	 * 
	 * @return
	 */
	public HashMap<String, String> integrate() {
		return this.arguments;
	}

}
