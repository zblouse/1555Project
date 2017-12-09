import java.util.regex.*;
public class scrubber{
	public boolean scrubbers(String input)
	{
		Pattern p = Pattern.compile("\\w*+");
		Matcher m = p.matcher(input);
		return m.matches();
	}
	public static void main(String[] args)
	{
		scrubber s = new scrubber();
		System.out.println(s.scrubbers(args[0]));
		
	}
}
