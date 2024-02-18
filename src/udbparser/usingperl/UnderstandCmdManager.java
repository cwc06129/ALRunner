package udbparser.usingperl;

import java.util.ArrayList;
import java.util.List;

public class UnderstandCmdManager {
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static final String winPath = "\"C:\\Program Files\\SciTools\\bin\\pc-win64\\";
	private static final String macPath = "\"/Applications/Understand.app/Content/MacOS/";
	private static final String linuxPath = "\"/usr/local/bin/scitools/bin/linux64/";

	public static List<String> createCmd(String toolName, String command) {
		List<String> commandList = new ArrayList<String>();

		String toolPath = toolName + "\"";

		if (isWindows()) {
			commandList.add("cmd.exe");
			commandList.add("/c");
			commandList.add(winPath + toolPath + " " + command);
		} else if (isMac()) {
			commandList.add("/bin/sh");
			commandList.add("-c");
			commandList.add(macPath + toolPath + " " + command);
		} else if (isLinux()) {
			commandList.add("/bin/sh");
			commandList.add("-c");
			commandList.add(linuxPath + toolPath + " " + command);
		} else {
			throw new RuntimeException("# UnderstandCmdManager # We do not support your OS.");
		}

		return commandList;
	}

	public static boolean isWindows() {
		return OS.contains("win");
	}

	public static boolean isMac() {
		return OS.contains("mac");
	}

	public static boolean isLinux() {
		return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
	}

}
