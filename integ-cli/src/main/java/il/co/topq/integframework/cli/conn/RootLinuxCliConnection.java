package il.co.topq.integframework.cli.conn;

import java.util.ArrayList;
import java.util.Arrays;

import il.co.topq.integframework.cli.terminal.Prompt;
import il.co.topq.integframework.utils.StringUtils;

public class RootLinuxCliConnection extends LinuxDefaultCliConnection {

	private String suUser = ""; 
	
	@Override
	public Prompt[] getPrompts() {
		ArrayList<Prompt> prompts = new ArrayList<Prompt>();
		prompts.addAll(Arrays.asList(super.getPrompts()));
		
		Prompt rootPrompt = new Prompt();
		rootPrompt.setPrompt("# ");
		if (StringUtils.isEmpty(suUser)){
			rootPrompt.setCommandEnd(true);
		}
		else {
			rootPrompt.setStringToSend("su -l " + getSuUser());
		}
		prompts.add(rootPrompt );
		return prompts.toArray(new Prompt[prompts.size()]);
		
		
	}

	public String getSuUser() {
		return suUser;
	}

	public void setSuUser(String suUser) {
		this.suUser = suUser;
	}
}
