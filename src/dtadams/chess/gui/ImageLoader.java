package dtadams.chess.gui;

import dtadams.chess.PieceColor;
import dtadams.chess.piece.PieceType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ImageLoader {
	
	static String loadPath = "lib/graphics/";

	static BufferedImage	background,
							whiteSquare,
							blackSquare,
							highlightSquare,
							selectedSquare;

	static HashMap<PieceColor, HashMap<PieceType, BufferedImage>> pieces =
		new HashMap<>();

	static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File(loadPath + path));
		} catch(Exception e) {
			// Fail silently
		}

		return null;
	}

	public static BufferedImage getBackground() {
		String path = "background.png";

		if(background == null) background = loadImage(path);
		return background;
	}

	public static BufferedImage getWhiteSquare() {
		String path = "white.png";

		if(whiteSquare == null) whiteSquare = loadImage(path);
		return whiteSquare;
	}

	public static BufferedImage getBlackSquare() {
		String path = "black.png";

		if(blackSquare == null) blackSquare = loadImage(path);
		return blackSquare;
	}

	public static BufferedImage getHighlightSquare() {
		String path = "highlight.png";

		if(highlightSquare == null) highlightSquare = loadImage(path);
		return highlightSquare;
	}

	public static BufferedImage getSelectedSquare() {
		String path = "selected.png";

		if(selectedSquare == null) selectedSquare = loadImage(path);
		return selectedSquare;
	}

	public static BufferedImage getPiece(PieceType t, PieceColor c) {
		String path = "pieces/"+c+"/"+t+".png";

		if(!pieces.containsKey(c)) {
			pieces.put(c, new HashMap<>());
		}
		HashMap<PieceType, BufferedImage> map = pieces.get(c);
		if(!map.containsKey(t)) {
			pieces.get(c).put(t, loadImage(path));
		}

		return pieces.get(c).get(t);
	}
}