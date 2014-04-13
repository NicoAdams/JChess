package dtadams.chess.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class AdjectiveGenerator {
	
	public static String random() {
		Path adjectivesFilePath = Paths.get("lib/adjectives.txt").toAbsolutePath();

		List<String> lines;

		try {
			lines =	Files.lines(adjectivesFilePath).collect(
				Collectors.toList()
			);
		} catch(IOException e) {
			System.out.println(e);
			return "(adjective not found)";
		}

		int random = (int)(Math.random() * lines.size());
		return lines.get(random);
	}
}