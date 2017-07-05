package regexTraining;
/*
A line terminator is a one- or two-character sequence that marks the end of a line of the input character sequence. 
The following are recognized as line terminators:

A newline (line feed) character ('\n'),
A carriage-return character followed immediately by a newline character ("\r\n"),
A standalone carriage-return character ('\r'), 
A next-line character ('\u0085'),
A line-separator character ('\u2028'), or
A paragraph-separator character ('\u2029).

If UNIX_LINES mode is activated, then the only line terminators recognized are newline characters.

The regular expression . matches any character except a line terminator unless the DOTALL flag is specified.

By default, the regular expressions ^ and $ ignore line terminators and only match at the beginning and the end, 
respectively, of the entire input sequence. If MULTILINE mode is activated then ^ matches at the beginning of input 
and after any line terminator except at the end of input. When in MULTILINE mode $ matches just before a line 
terminator or the end of the input sequence.
 */
public class LineTerminators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
