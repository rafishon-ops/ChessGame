package Utilities;

import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int turn = 0;
		int fiftyMoveRule = 0;
		boolean fiftyMoveReset;
		boolean gameOver = false;
		Position position = new Position();
		position.setUp();
		while (!gameOver) {
			fiftyMoveReset = Move.getMove(position, scan);
			fiftyMoveRule++;
			if (fiftyMoveReset) {
				fiftyMoveRule = 0;
			}
			if (position.checkmate()) {
				gameOver = true;
				if (turn == 0) {
					System.out.println("Checkmate. White wins.");
				} else {
					System.out.println("Checkmate. Black wins.");
				}
			}
			if (position.stalemate()) {
				gameOver = true;
				System.out.println("Draw by stalemate");
			}
			if (Position.repetition()) {
				gameOver = true;
				System.out.println("Draw by repetition");
			}
			if (position.insufficientMaterial()) {
				gameOver = true;
				System.out.println("Draw by insufficient material");
			}
			if (fiftyMoveRule == 100) {
				gameOver = true;
				System.out.println("Draw by fifty move rule");
			}
			turn = ((turn + 1) % 2);
			String result = "";
			for (int i = 7; i > -1; i--) {
				for (int j = 0; j < 8; j++) {
					result += '|';
					result += position.getBoard()[i][j];
				}
				result += "|\n";
			}
			System.out.println(result);

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 8; j++) {
					if (position.getPieces()[i][j] != null) {
						if (position.getPieces()[i][j].getEnPassant() == "Target Saved") {
							position.getPieces()[i][j].setEnPassant("Safe");
						}
						if (position.getPieces()[i][j].getEnPassant() == "Target Acquired") {
							position.getPieces()[i][j].setEnPassant("Target Saved");
						}
					}
				}
			}
		}
		scan.close();

	}

}
