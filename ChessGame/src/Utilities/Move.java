package Utilities;

import java.util.ArrayList;

import java.util.Scanner;

public class Move {
	private char piece;
	private String specifier, promotion, castle;
	private int rank, file, pawn;
	private boolean valid, capture, check, checkmate;
	private static int moveNum = 1;

	public Move() {
		valid = true;
		capture = false;
		check = false;
	}

	public static boolean getMove(Position position, Scanner scan) {
		boolean legal = false;
		String command;
		Move move = null;
		ArrayList<Integer> candidates = new ArrayList<>();
		while (!legal) {
			candidates.clear();
			if (position.getTurn() == 0) {
				System.out.print(moveNum + ".");
			} else {
				System.out.print(moveNum + "...");
			}
			command = scan.next();
			move = parse(command);
			if (!move.valid) {
				System.out.println("Invalid input. Try again.");
				continue;
			}

			Position trial = new Position(position);
			boolean enPassant = false;
			if (move.castle == "short") {
				int rank = position.getTurn() == 0 ? 1 : 8;
				if (position.getPieces()[position.getTurn()][9].hasMoved()
						|| position.getPieces()[position.getTurn()][15].hasMoved() || position.occupied(rank, 6)
						|| position.occupied(rank, 7) || position.check()) {
					System.out.println("Illegal move. Try again.");
					continue;
				}
				trial.move(position.getTurn(), 15, rank, 6, null);
				if (trial.check()) {
					System.out.println("Illegal move. Try again.");
					continue;
				}
				trial.move(position.getTurn(), 15, rank, 7, null);
				trial.move(position.getTurn(), 9, rank, 6, null);
				if (trial.check()) {
					System.out.println("Illegal move. Try again.");
				}
				trial.changeTurn();
				if (!checkChecker(move, trial)) {
					continue;
				}
				position.move(position.getTurn(), 9, rank, 6, null);
				position.executeMove(position.getTurn(), 15, rank, 7, null);
				position.changeTurn();
				return false;
			} else if (move.castle == "long") {
				int rank = position.getTurn() == 0 ? 1 : 8;
				if (position.getPieces()[position.getTurn()][8].hasMoved()
						|| position.getPieces()[position.getTurn()][15].hasMoved() || position.occupied(rank, 2)
						|| position.occupied(rank, 3) || position.occupied(rank, 4) || position.check()) {
					System.out.println("Illegal move. Try again.");
					continue;
				}
				trial.move(position.getTurn(), 15, rank, 4, null);
				if (trial.check()) {
					System.out.println("Illegal move. Try again.");
					continue;
				}
				trial.move(position.getTurn(), 15, rank, 3, null);
				trial.move(position.getTurn(), 8, rank, 4, null);
				if (trial.check()) {
					System.out.println("Illegal move. Try again.");
				}
				trial.changeTurn();
				if (!checkChecker(move, trial)) {
					continue;
				}
				position.move(position.getTurn(), 8, rank, 4, null);
				position.executeMove(position.getTurn(), 15, rank, 3, null);
				position.changeTurn();
				return false;
			} else {
				if (move.piece == 'P' && move.capture && move.rank == (position.getTurn() == 0 ? 6 : 3)
						&& Math.abs(move.file - move.pawn) == 1 && !position.occupied(move.rank, move.file)) {
					Piece target = null;
					int index = -1;
					for (int i = 0; i < 8; i++) {
						if (position.getPieces()[position.getTurn() == 0 ? 1 : 0][i] != null
								&& position.getPieces()[position.getTurn() == 0 ? 1 : 0][i]
										.getEnPassant() == "Target Saved") {
							target = position.getPieces()[position.getTurn() == 0 ? 1 : 0][i];
							index = i;
							break;
						}
					}
					if (target == null || target.getFile() != move.file) {
						System.out.println("Illegal move. Try again.");
						continue;
					}
					for (int i = 0; i < 8; i++) {
						if (trial.getPieces()[position.getTurn()][i] != null
								&& trial.getPieces()[position.getTurn()][i].getRank() == (position.getTurn() == 0 ? 5 : 4)
								&& trial.getPieces()[position.getTurn()][i].getFile() == move.pawn) {
							trial.move(position.getTurn() == 0 ? 1 : 0, index, move.rank, move.file, null);
							enPassant = true;
							break;
						}
					}
				}
				for (int i = 0; i < 16; i++) {
					if (trial.getPieces()[position.getTurn()][i] != null
							&& trial.getPieces()[position.getTurn()][i].getPiece() == move.piece
							&& trial.getPieces()[position.getTurn()][i].hasAccessTo(move.rank, move.file, trial)
							&& (move.pawn == 0 || move.pawn == trial.getPieces()[position.getTurn()][i].getFile())) {
						Position candidateTest = new Position(trial);
						candidateTest.move(position.getTurn(), i, move.rank, move.file, null);
						if (!candidateTest.check()) {
							candidates.add(i);
						}
					}
				}
				if (candidates.size() == 0) {
					System.out.println("Illegal move. Try again.");
					continue;
				}
				if (candidates.size() > 1) {
					if (move.specifier == null) {
						System.out.println("Not specific enough. Try again.");
						continue;
					}
					int fileSpec = 0, rankSpec = 0;
					if (move.specifier.length() == 1) {
						if (move.specifier.charAt(0) >= 97 && move.specifier.charAt(0) <= 104) {
							fileSpec = move.specifier.charAt(0) - 96;
						} else if (move.specifier.charAt(0) >= 49 && move.specifier.charAt(0) <= 56) {
							rankSpec = move.specifier.charAt(0) - 48;
						} else {
							System.out.println("Invalid Specifier. Try again.");
							continue;
						}
					}
					if (move.specifier.length() == 2) {
						if (move.specifier.charAt(0) >= 97 && move.specifier.charAt(0) <= 104) {
							fileSpec = move.specifier.charAt(0) - 96;
						} else {
							System.out.println("Invalid Specifier. Try again.");
							continue;
						}
						if (move.specifier.charAt(1) >= 49 && move.specifier.charAt(1) <= 56) {
							rankSpec = move.specifier.charAt(1) - 48;
						} else {
							System.out.println("Invalid Specifier. Try again.");
							continue;
						}
					}

					boolean removed = false;
					if (fileSpec != 0) {
						for (int i = candidates.size() - 1; i >= 0; i--) {
							if (trial.getPieces()[position.getTurn()][candidates.get(i)].getFile() != fileSpec) {
								candidates.remove(i);
								removed = true;
							}
						}
						if (!removed) {
							System.out.println("No need to specify file. Try again.");
							continue;
						}
					}
					removed = false;
					if (rankSpec != 0) {
						for (int i = candidates.size() - 1; i >= 0; i--) {
							if (trial.getPieces()[position.getTurn()][candidates.get(i)].getRank() != rankSpec) {
								candidates.remove(i);
								removed = true;
							}
						}
						if (!removed) {
							System.out.println("No need to specify rank. Try again.");
							continue;
						}
					}
					if (candidates.size() != 1) {
						System.out.println("Specifier failed. Try again.");
						continue;
					}
				} else if (move.specifier != null) {
					System.out.println("No need for specifier. Try again.");
					continue;
				}
				if (move.capture && !trial.occupied(move.rank, move.file)) {
					System.out.println("This move is not a capture. Try again.");
					continue;
				}
				if (!move.capture && trial.occupied(move.rank, move.file)) {
					System.out.println("This move is a capture. Try again.");
					continue;
				}
				if (move.piece == 'P' && (move.rank == 8 || move.rank == 1) && move.promotion == null) {
					System.out.println("Please choose something to promote to. Try again.");
					continue;
				}
				if (move.promotion != null && move.rank > 1 && move.rank < 8) {
					System.out.println("You don't get to promote yet. Try again.");
					continue;
				}
				trial.move(position.getTurn(), candidates.get(0), move.rank, move.file, move.promotion);
			}
			trial.changeTurn();
			if (!checkChecker(move, trial)) {
				continue;
			}
			if (enPassant) {
				position.move(position.getTurn() == 0 ? 1 : 0, move.file - 1, move.rank, move.file, null);
			}
			position.executeMove(position.getTurn(), candidates.get(0), move.rank, move.file, move.promotion);
			if (position.getTurn() == 1) {
				moveNum++;
			}

			position.changeTurn();
			legal = true;

		}

		return (move.piece == 'P' || move.capture);
	}

	/*
	 * Parses a move command; Determines whether the input is valid, and all
	 * relevant components of what the user believes the move will do.
	 */
	private static Move parse(String command) {
		Move move = new Move();
		int index = command.length() - 1;
		if (index < 1) {
			move.valid = false;
			return move;
		}
		if (command.charAt(index) == '+') {
			move.check = true;
			index--;
		} else if (command.charAt(index) == '#') {
			move.checkmate = true;
			index--;
		}
		if (command.equals("0-0-0") || command.equals("0-0-0+")) {
			move.castle = "long";
			return move;
		}
		if (command.equals("0-0") || command.equals("0-0+")) {
			move.castle = "short";
			return move;
		}
		if ((int) command.charAt(0) >= 97 && command.charAt(0) <= 104) {
			move.piece = 'P';
			move.pawn = command.charAt(0) - 96;
		}
		if (command.charAt(index) == 'R' || command.charAt(index) == 'N' || command.charAt(index) == 'B'
				|| command.charAt(index) == 'Q') {
			if (move.piece == 'P') {
				move.promotion = ("" + command.charAt(index));
				index--;
			} else {
				move.valid = false;
				return move;
			}
		}
		if (index < 0 || command.charAt(index) < 49 || command.charAt(index) > 56) {
			move.valid = false;
			return move;
		}
		move.rank = command.charAt(index) - 48;
		index--;
		if (index < 0 || command.charAt(index) < 97 || command.charAt(index) > 104) {
			move.valid = false;
			return move;
		}
		move.file = (int) command.charAt(index) - 96;
		if (index == 0) {
			return move;
		}
		index--;
		if (command.charAt(index) == 'x') {
			move.capture = true;
			index--;
			if (index < 0) {
				move.valid = false;
				return move;
			}
			if (move.piece == 'P') {
				if (index != 0) {
					move.valid = false;
				}
				return move;
			}
		}
		if (index == 2) {
			move.specifier = (command.substring(1, 3));
			index = 0;
		}

		if (index == 1) {
			move.specifier = (command.substring(1, 2));
			index = 0;
		}

		if (index != 0) {
			move.valid = false;
		}

		if (command.charAt(0) != 'K' && command.charAt(0) != 'Q' && command.charAt(0) != 'B' && command.charAt(0) != 'N'
				&& command.charAt(0) != 'R') {
			move.valid = false;
		} else {
			move.piece = command.charAt(0);
		}

		return move;
	}

	public static boolean checkChecker(Move move, Position trial) {
		if (move.checkmate && !trial.checkmate()) {
			System.out.println("This move is not checkmate. Try again.");
			return false;
		}
		if (!move.checkmate && trial.checkmate()) {
			System.out.println("This move is checkmate. Try again.");
			return false;
		}
		if (move.check && !trial.check()) {
			System.out.println("This move is not check. Try again.");
			return false;
		}
		if (!move.check && trial.check() && !trial.checkmate()) {
			System.out.println("This move is check. Try again.");
			return false;
		}

		return true;
	}

}
